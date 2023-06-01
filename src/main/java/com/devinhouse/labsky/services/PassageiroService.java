package com.devinhouse.labsky.services;

import com.devinhouse.labsky.dtos.checkin.CheckinRequestDto;
import com.devinhouse.labsky.dtos.checkin.CheckinResponseDto;
import com.devinhouse.labsky.exceptions.PassageiroNaoEncontradoException;
import com.devinhouse.labsky.models.Assento;
import com.devinhouse.labsky.models.Passageiro;
import com.devinhouse.labsky.repositories.PassageiroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PassageiroService {
    @Autowired
    PassageiroRepository repository;
    @Autowired
    AssentoService assentoService;

    public List<Passageiro> getPassageiros() {
        return repository.findAll();
    }

    public Passageiro getPassageiroPeloCpf(String cpf) {
        Passageiro passageiro = repository.findById(cpf)
                .orElseThrow(() -> new PassageiroNaoEncontradoException(cpf));

        return passageiro;
    }

    public CheckinResponseDto realizarCheckin(CheckinRequestDto requestDto) {
        Passageiro passageiro = getPassageiroPeloCpf(requestDto.getCpf());

        Assento assento = assentoService
                .reservarAssento(
                        requestDto.getAssento().toUpperCase(),
                        passageiro.getDataDeNascimento(),
                        requestDto.getMalasDespachadas());

        String eticket = UUID.randomUUID().toString();
        Integer milhasAcumuladas = acumularMilhas(passageiro.getMilhas(), passageiro.getClassificacao());
        passageiro.setMilhas(milhasAcumuladas);
        passageiro.setEticket(eticket);
        passageiro.setMalasDespachadas(requestDto.getMalasDespachadas());
        passageiro.setAssento(assento.getAssento());
        passageiro.setDataHoraConfirmacao(LocalDateTime.now());
        passageiro = repository.save(passageiro);

        System.out.printf("Confirmação feita pelo passageiro de CPF %s com e-ticket %s", passageiro.getCpf(), eticket);

        return new CheckinResponseDto(eticket);
    }

    public Integer acumularMilhas(Integer milhas, String classificacao) {
        Integer milhasDaViagemAtual;

        switch (classificacao) {
            case "VIP":
                milhasDaViagemAtual = 100;
                break;
            case "OURO":
                milhasDaViagemAtual = 80;
                break;
            case "PRATA":
                milhasDaViagemAtual = 50;
                break;
            case "BRONZE":
                milhasDaViagemAtual = 30;
                break;
            default:
                milhasDaViagemAtual = 10;
        }

        return milhas + milhasDaViagemAtual;
    }
}
