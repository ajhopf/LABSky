package com.devinhouse.labsky.services;

import com.devinhouse.labsky.dtos.checkin.CheckinRequestDto;
import com.devinhouse.labsky.dtos.checkin.CheckinResponseDto;
import com.devinhouse.labsky.exceptions.*;
import com.devinhouse.labsky.models.Assento;
import com.devinhouse.labsky.models.Passageiro;
import com.devinhouse.labsky.repositories.AssentoRepository;
import com.devinhouse.labsky.repositories.PassageiroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.UUID;

@Service
public class PassageiroService {
    @Autowired
    PassageiroRepository repository;

    @Autowired
    AssentoRepository assentoRepository;
    public List<Passageiro> getPassageiros() {
        return repository.findAll();
    }

    public Passageiro getPassageiroPeloCpf(String cpf) {
        Passageiro passageiro = repository.findById(cpf)
                .orElseThrow(() -> new PassageiroNaoEncontradoException(cpf));

        return passageiro;
    }

    public CheckinResponseDto realizarCheckin(CheckinRequestDto requestDto) {
        Passageiro passageiro = repository.findById(requestDto.getCpf())
                .orElseThrow(() -> new PassageiroNaoEncontradoException(requestDto.getCpf()));

        Assento assento = assentoRepository.findByAssento(requestDto.getAssento().toUpperCase())
                .orElseThrow(() -> new AssentoNaoEncontradoException(requestDto.getAssento()));

        if (assento.getReservado() == '1') {
            throw new AssentoReservadoException(requestDto.getAssento().toUpperCase());
        }

        Integer idadePassageiro = Period.between(passageiro.getDataDeNascimento(), LocalDate.now()).getYears();
        if (idadePassageiro < 18 && assento.getFileiraDeEmergencia() == '1') {
            throw new MenorEmFileiraDeEmergenciaException(passageiro.getCpf());
        }

        if (!requestDto.getMalasDespachadas() && assento.getFileiraDeEmergencia() == '1') {
            throw new BagagensNaoDespachadasEmFileiraDeEmergenciaException(requestDto.getAssento().toUpperCase());
        }

        assento.setReservado('1');

        assentoRepository.save(assento);

        Integer milhasAtuais = passageiro.getMilhas();
        Integer milhasAcumuladas;
        switch (passageiro.getClassificacao()) {
            case "VIP":
                milhasAcumuladas = 100;
                break;
            case "OURO":
                milhasAcumuladas = 80;
                break;
            case "PRATA":
                milhasAcumuladas = 50;
                break;
            case "BRONZE":
                milhasAcumuladas = 30;
                break;
            default:
                milhasAcumuladas = 10;
        }
        passageiro.setMilhas(milhasAtuais + milhasAcumuladas);

        repository.save(passageiro);

        String eticket = UUID.randomUUID().toString();

        System.out.printf("Confirmação feita pelo passageiro de CPF %s com e-ticket %s", passageiro.getCpf(), eticket);

        return new CheckinResponseDto(eticket);
    }
}
