package com.devinhouse.labsky.repositories;

import com.devinhouse.labsky.models.BilheteDeEmbarque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BilheteDeEmbarqueRepository extends JpaRepository<BilheteDeEmbarque, Long> {
    @Query("SELECT b From BilheteDeEmbarque b WHERE b.passageiro.cpf = :passageiroCpf")
    Optional<BilheteDeEmbarque> findByPassageiroCpf(String passageiroCpf);
}
