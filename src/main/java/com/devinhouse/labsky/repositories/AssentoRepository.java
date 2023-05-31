package com.devinhouse.labsky.repositories;

import com.devinhouse.labsky.models.Assento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssentoRepository extends JpaRepository<Assento, Long> {
}
