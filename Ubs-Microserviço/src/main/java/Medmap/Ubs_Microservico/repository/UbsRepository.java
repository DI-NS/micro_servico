package Medmap.Ubs_Microservico.repository;

import Medmap.Ubs_Microservico.model.Ubs;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UbsRepository extends JpaRepository<Ubs, Long> {
    Optional<Ubs> findByCnes(String cnes);
    boolean existsByCnes(String cnes);
}
