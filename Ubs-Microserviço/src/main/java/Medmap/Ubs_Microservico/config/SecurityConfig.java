package Medmap.Ubs_Microservico.config;

import Medmap.Ubs_Microservico.security.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${server.servlet.context-path:}")
    private String ctx;

    @Value("${swagger-open:false}")
    private boolean openEverything;

    @Bean
    public AuthenticationEntryPoint customEntryPoint() {
        return (req, res, ex) ->
                res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Acesso não autorizado");
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();
        cfg.setAllowedOriginPatterns(List.of("*"));
        cfg.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        cfg.setAllowedHeaders(List.of("*"));
        cfg.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource src = new UrlBasedCorsConfigurationSource();
        src.registerCorsConfiguration("/**", cfg);
        return src;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           JwtAuthenticationFilter jwt) throws Exception {

        if (openEverything) {
            http
                    .csrf(csrf -> csrf.disable())
                    .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                    .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
            return http.build();
        }

        // Caminhos públicos do Swagger (considerando context-path)
        String SWAGGER_UI      = ctx + "/swagger-ui/**";
        String SWAGGER_UI_HTML = ctx + "/swagger-ui.html";
        String API_DOCS        = ctx + "/v3/api-docs/**";
        String API_DOCS_ROOT   = ctx + "/v3/api-docs";
        String SWAGGER_CONFIG  = ctx + "/v3/api-docs/swagger-config";

        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(SWAGGER_UI, SWAGGER_UI_HTML, API_DOCS, API_DOCS_ROOT, SWAGGER_CONFIG)
                        .permitAll()
                        .requestMatchers(HttpMethod.POST, "/ubs").permitAll()
                        .requestMatchers(HttpMethod.GET, "/ubs/**").permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex.authenticationEntryPoint(customEntryPoint()))
                .addFilterBefore(jwt, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authManager(AuthenticationConfiguration cfg) throws Exception {
        return cfg.getAuthenticationManager();
    }
}
