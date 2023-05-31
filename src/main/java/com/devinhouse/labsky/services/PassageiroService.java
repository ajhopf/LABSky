package com.devinhouse.labsky.services;

import com.devinhouse.labsky.models.Passageiro;
import com.devinhouse.labsky.repositories.PassageiroRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PassageiroService {
    @Autowired
    PassageiroRespository respository;

    public List<Passageiro> getPassageiros() {
        return respository.findAll();
    }
}
