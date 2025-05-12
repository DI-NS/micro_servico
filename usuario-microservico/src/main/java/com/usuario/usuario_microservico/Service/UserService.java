package com.usuario.usuario_microservico.Service;

import com.usuario.usuario_microservico.MedicamentoFeignClient;
import com.usuario.usuario_microservico.UbsFeignClient;
import com.usuario.usuario_microservico.dto.MedicamentoComUbsDTO;
import com.usuario.usuario_microservico.dto.UbsInfoDTO;
import com.usuario.usuario_microservico.Model.Medicamento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private MedicamentoFeignClient medicamentoClient;

    @Autowired
    private UbsFeignClient ubsClient;

    public List<MedicamentoComUbsDTO> getAllMedicamentos() {
        return medicamentoClient.getAllMedicamentos().stream()
                .map(med -> {
                    UbsInfoDTO ubs = ubsClient.getById(med.getUbsId());
                    MedicamentoComUbsDTO dto = new MedicamentoComUbsDTO();
                    dto.setId(med.getId());
                    dto.setNome(med.getNome());
                    dto.setInformacoes(med.getInformacoes());
                    dto.setImagemUrl(med.getImagemUrl());
                    dto.setAtivo(med.isAtivo());
                    dto.setUbsId(med.getUbsId());
                    dto.setUbsCnes(ubs.getCnes());
                    dto.setUbsNome(ubs.getNome());
                    dto.setUbsEndereco(ubs.getEndereco());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public MedicamentoComUbsDTO getMedicamentoById(Long id) {
        Medicamento med = medicamentoClient.getMedicamentoById(id);
        UbsInfoDTO ubs = ubsClient.getById(med.getUbsId());
        MedicamentoComUbsDTO dto = new MedicamentoComUbsDTO();
        dto.setId(med.getId());
        dto.setNome(med.getNome());
        dto.setInformacoes(med.getInformacoes());
        dto.setImagemUrl(med.getImagemUrl());
        dto.setAtivo(med.isAtivo());
        dto.setUbsId(med.getUbsId());
        dto.setUbsCnes(ubs.getCnes());
        dto.setUbsNome(ubs.getNome());
        dto.setUbsEndereco(ubs.getEndereco());
        return dto;
    }
}
