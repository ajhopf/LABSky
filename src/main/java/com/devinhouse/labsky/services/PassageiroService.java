package com.devinhouse.labsky.services;

import com.devinhouse.labsky.dtos.checkin.CheckinRequestDto;
import com.devinhouse.labsky.dtos.checkin.CheckinResponseDto;
import com.devinhouse.labsky.exceptions.PassageiroJaRealizouCheckinException;
import com.devinhouse.labsky.exceptions.PassageiroNaoEncontradoException;
import com.devinhouse.labsky.models.Assento;
import com.devinhouse.labsky.models.BilheteDeEmbarque;
import com.devinhouse.labsky.models.Passageiro;
import com.devinhouse.labsky.repositories.PassageiroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PassageiroService {
    @Autowired
    PassageiroRepository repository;
    @Autowired
    private BilheteDeEmbarqueService bilheteDeEmbarqueService;
    @Autowired
    private AssentoService assentoService;

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
        Boolean malasDespachadas = requestDto.getMalasDespachadas();

        if (bilheteDeEmbarqueService.cpfJaTemBilhete(passageiro.getCpf())) {
            throw new PassageiroJaRealizouCheckinException(passageiro.getCpf());
        }

        Assento assento = assentoService
                .reservarAssento(
                        requestDto.getAssento().toUpperCase(),
                        passageiro.getDataDeNascimento(),
                        malasDespachadas);

        Integer milhasAcumuladas = acumularMilhas(passageiro.getMilhas(), passageiro.getClassificacao());
        passageiro.setMilhas(milhasAcumuladas);
        passageiro = repository.save(passageiro);

        BilheteDeEmbarque bilheteDeEmbarque = bilheteDeEmbarqueService.gerarBilhete(passageiro, assento.getAssento(), malasDespachadas);

        System.out.printf("Confirmação feita pelo passageiro de CPF %s com e-ticket %s", passageiro.getCpf(), bilheteDeEmbarque.getEticket());

        return new CheckinResponseDto(bilheteDeEmbarque.getEticket(), bilheteDeEmbarque.getDataHoraConfirmacao());
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
