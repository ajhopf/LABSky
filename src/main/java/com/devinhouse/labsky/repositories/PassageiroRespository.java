package com.devinhouse.labsky.repositories;

import com.devinhouse.labsky.models.Passageiro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PassageiroRespository extends JpaRepository<Passageiro, String> {
}
