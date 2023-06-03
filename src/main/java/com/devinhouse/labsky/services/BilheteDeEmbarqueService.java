package com.devinhouse.labsky.services;

import com.devinhouse.labsky.models.BilheteDeEmbarque;
import com.devinhouse.labsky.models.Passageiro;
import com.devinhouse.labsky.repositories.BilheteDeEmbarqueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class BilheteDeEmbarqueService {
    @Autowired
    private BilheteDeEmbarqueRepository repository;
    public Optional<BilheteDeEmbarque> getBilheteDeEmbarque(String passageiroCpf) {
        return repository.findByPassageiroCpf(passageiroCpf);
    }

    public BilheteDeEmbarque gerarBilhete(Passageiro passageiro, String assento, Boolean malasDespachadas) {
        String eticket = UUID.randomUUID().toString();

        BilheteDeEmbarque bilheteDeEmbarque = new BilheteDeEmbarque(eticket, assento, malasDespachadas, passageiro);
        bilheteDeEmbarque = repository.save(bilheteDeEmbarque);

        return bilheteDeEmbarque;
    }

    public Boolean cpfJaTemBilhete(String passageiroCpf) {
        return repository.findByPassageiroCpf(passageiroCpf).isPresent();
    }
}
