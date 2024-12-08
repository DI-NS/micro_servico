package MedMap.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.Key;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    private JwtSecretProvider jwtSecretProvider;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    // Subclasse estática para acessar o método protected
    static class TestableJwtAuthenticationFilter extends JwtAuthenticationFilter {
        public TestableJwtAuthenticationFilter(JwtSecretProvider jwtSecretProvider) {
            super(jwtSecretProvider);
        }

        @Override
        public void doFilterInternal(HttpServletRequest request,
                                     HttpServletResponse response,
                                     FilterChain filterChain) throws ServletException, IOException {
            super.doFilterInternal(request, response, filterChain);
        }
    }

    @InjectMocks
    private TestableJwtAuthenticationFilter jwtAuthenticationFilter;

    private Key key;
    private String jwtSecret;

    @BeforeEach
    void setUp() {
        // Gerar uma chave segura para HS256 (256 bits)
        key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        jwtSecret = java.util.Base64.getEncoder().encodeToString(key.getEncoded());

        // Configurar o mock para retornar a chave secreta como lenient
        lenient().when(jwtSecretProvider.getJwtSecret()).thenReturn(jwtSecret);

        // Limpar o contexto de segurança antes de cada teste
        SecurityContextHolder.clearContext();
    }

    /**
     * Testa a passagem por uma rota pública, garantindo que o filtro
     * não tente autenticar e apenas continue a cadeia de filtros.
     */
    @Test
    void doFilterInternal_PublicEndpoint_ShouldContinueFilterChain() throws ServletException, IOException {
        // Configurar o request para uma rota pública
        when(request.getRequestURI()).thenReturn("/swagger-ui/index.html");

        // Executar o filtro
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Verificar que o filterChain.doFilter foi chamado e nenhuma autenticação foi estabelecida
        verify(filterChain, times(1)).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    /**
     * Testa o acesso a uma rota protegida sem o cabeçalho Authorization.
     * Espera-se que retorne 401 "Acesso não autorizado".
     */
    @Test
    void doFilterInternal_ProtectedEndpoint_NoAuthorizationHeader_ShouldReturnUnauthorized() throws ServletException, IOException {
        // Configurar o request para uma rota protegida sem Authorization
        when(request.getRequestURI()).thenReturn("/protected/resource");
        when(request.getHeader("Authorization")).thenReturn(null);

        PrintWriter out = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(out);

        ArgumentCaptor<String> responseContent = ArgumentCaptor.forClass(String.class);

        // Executar o filtro
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Verificar que a resposta foi configurada com 401
        verify(response, times(1)).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(out, times(1)).write(responseContent.capture());

        // Verificar que o filterChain.doFilter NÃO foi chamado
        verify(filterChain, never()).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());

        // Verificar o conteúdo escrito na resposta
        assertEquals("Acesso não autorizado", responseContent.getValue());
    }

    /**
     * Testa o acesso a uma rota protegida com um cabeçalho Authorization inválido.
     * Espera-se que retorne 401 "Acesso não autorizado".
     */
    @Test
    void doFilterInternal_ProtectedEndpoint_InvalidAuthorizationHeader_ShouldReturnUnauthorized() throws ServletException, IOException {
        // Configurar o request para uma rota protegida com Authorization inválido
        when(request.getRequestURI()).thenReturn("/protected/resource");
        when(request.getHeader("Authorization")).thenReturn("InvalidBearer token");

        PrintWriter out = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(out);

        ArgumentCaptor<String> responseContent = ArgumentCaptor.forClass(String.class);

        // Executar o filtro
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Verificar que a resposta foi configurada com 401
        verify(response, times(1)).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(out, times(1)).write(responseContent.capture());

        // Verificar que o filterChain.doFilter NÃO foi chamado
        verify(filterChain, never()).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());

        // Verificar o conteúdo escrito na resposta
        assertEquals("Acesso não autorizado", responseContent.getValue());
    }

    /**
     * Testa o acesso a uma rota protegida com um token JWT inválido.
     * Espera-se que retorne 401 "Token JWT inválido ou expirado".
     */

    @Test
    void doFilterInternal_ProtectedEndpoint_InvalidJwt_ShouldReturnUnauthorized() throws ServletException, IOException {
        // Configurar o request para uma rota protegida com um token inválido
        when(request.getRequestURI()).thenReturn("/protected/resource");
        when(request.getHeader("Authorization")).thenReturn("Bearer invalid.token.here");

        PrintWriter out = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(out);

        ArgumentCaptor<String> responseContent = ArgumentCaptor.forClass(String.class);

        // Executar o filtro
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Verificar que a resposta foi configurada com 401
        verify(response, times(1)).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(out, times(1)).write(responseContent.capture());

        // Verificar que o filterChain.doFilter NÃO foi chamado
        verify(filterChain, never()).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());

        // Verificar o conteúdo escrito na resposta
        assertEquals("Token JWT inválido ou expirado", responseContent.getValue());
    }

    /**
     * Testa o acesso a uma rota protegida com um token JWT válido.
     * Espera-se que a autenticação seja estabelecida e o filtro continue a cadeia.
     */
    @Test
    void doFilterInternal_ProtectedEndpoint_ValidJwt_ShouldAuthenticateAndContinueFilterChain() throws ServletException, IOException {
        // Configurar o request para uma rota protegida com um token válido
        String cnes = "123456789";
        String jwt = Jwts.builder()
                .setSubject(cnes)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        when(request.getRequestURI()).thenReturn("/protected/resource");
        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwt);

        // Executar o filtro
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Verificar que o filterChain.doFilter foi chamado
        verify(filterChain, times(1)).doFilter(request, response);
        // Verificar que a autenticação foi estabelecida no contexto de segurança
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authentication);
        assertTrue(authentication instanceof UsernamePasswordAuthenticationToken);
        assertEquals(cnes, authentication.getPrincipal());
        assertNull(authentication.getCredentials());
        assertTrue(authentication.getAuthorities().isEmpty());
    }

    /**
     * Testa o acesso a uma rota protegida com um token JWT expirado.
     * Espera-se que retorne 401 "Token JWT inválido ou expirado".
     */
    @Test
    void doFilterInternal_ProtectedEndpoint_ExpiredJwt_ShouldReturnUnauthorized() throws ServletException, IOException {
        // Configurar o request para uma rota protegida com um token expirado
        String cnes = "123456789";
        // Criar um token expirado
        String expiredJwt = Jwts.builder()
                .setSubject(cnes)
                .setExpiration(new java.util.Date(System.currentTimeMillis() - 1000)) // Expirado 1 segundo atrás
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        when(request.getRequestURI()).thenReturn("/protected/resource");
        when(request.getHeader("Authorization")).thenReturn("Bearer " + expiredJwt);

        PrintWriter out = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(out);

        ArgumentCaptor<String> responseContent = ArgumentCaptor.forClass(String.class);

        // Executar o filtro
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Verificar que a resposta foi configurada com 401
        verify(response, times(1)).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(out, times(1)).write(responseContent.capture());

        // Verificar que o filterChain.doFilter NÃO foi chamado
        verify(filterChain, never()).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());

        // Verificar o conteúdo escrito na resposta
        assertEquals("Token JWT inválido ou expirado", responseContent.getValue());
    }
    // Teste para rota protegida sem token JWT
    @Test
    void doFilterInternal_ProtectedEndpoint_NoToken_ShouldReturnUnauthorized() throws ServletException, IOException {
        // Configurar o request para uma rota protegida sem um token JWT
        when(request.getRequestURI()).thenReturn("/protected/resource");
        when(request.getHeader("Authorization")).thenReturn(null);

        PrintWriter out = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(out);

        ArgumentCaptor<String> responseContent = ArgumentCaptor.forClass(String.class);

        // Executar o filtro
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Verificar que a resposta foi configurada com 401
        verify(response, times(1)).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(out, times(1)).write(responseContent.capture());

        // Verificar que o filterChain.doFilter NÃO foi chamado
        verify(filterChain, never()).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());

        // Verificar o conteúdo escrito na resposta
        assertEquals("Acesso não autorizado", responseContent.getValue());
    }


}
