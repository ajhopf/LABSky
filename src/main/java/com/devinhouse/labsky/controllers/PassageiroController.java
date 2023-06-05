package com.devinhouse.labsky.controllers;

import com.devinhouse.labsky.dtos.checkin.CheckinRequestDto;
import com.devinhouse.labsky.dtos.checkin.CheckinResponseDto;
import com.devinhouse.labsky.dtos.passageiro.PassageiroCompletoResponseDto;
import com.devinhouse.labsky.dtos.passageiro.PassageiroSimplesResponseDto;
import com.devinhouse.labsky.models.BilheteDeEmbarque;
import com.devinhouse.labsky.models.Passageiro;
import com.devinhouse.labsky.services.BilheteDeEmbarqueService;
import com.devinhouse.labsky.services.PassageiroService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/passageiros")
public class PassageiroController {
    @Autowired
    private PassageiroService service;
    @Autowired
    private BilheteDeEmbarqueService bilheteDeEmbarqueService;

    private final ModelMapper modelMapper = new ModelMapper();

    @GetMapping
    public ResponseEntity<List<PassageiroCompletoResponseDto>> getPassageiros() {
        List<Passageiro> passageiros = service.getPassageiros();

        List<PassageiroCompletoResponseDto> passageirosResponse = passageiros
                .stream()
                .map(passageiro -> {
                    PassageiroCompletoResponseDto passageiroCompletoResponseDto = modelMapper
                            .map(passageiro, PassageiroCompletoResponseDto.class);

                    Optional<BilheteDeEmbarque> bilheteDeEmbarqueOptional = bilheteDeEmbarqueService
                            .getBilheteDeEmbarque(passageiro.getCpf());

                    if (bilheteDeEmbarqueOptional.isPresent()) {
                        BilheteDeEmbarque bilheteDeEmbarque = bilheteDeEmbarqueOptional.get();

                        passageiroCompletoResponseDto.setEticket(bilheteDeEmbarque.getEticket());
                        passageiroCompletoResponseDto.setAssento(bilheteDeEmbarque.getAssento());
                        passageiroCompletoResponseDto.setDataHoraConfirmacao(bilheteDeEmbarque.getDataHoraConfirmacao());
                    }

                    return passageiroCompletoResponseDto;
                    })
                .toList();

        return ResponseEntity.ok(passageirosResponse);
    }

    @GetMapping("/{cpf}")
    public ResponseEntity<PassageiroSimplesResponseDto> getPassageiro(@PathVariable String cpf) {
        Passageiro passageiro = service.getPassageiroPeloCpf(cpf);
        PassageiroSimplesResponseDto passageiroCompletoResponseDto = modelMapper.map(passageiro, PassageiroSimplesResponseDto.class);
        return ResponseEntity.ok(passageiroCompletoResponseDto);
    }

    @PostMapping("/confirmacao")
    public ResponseEntity<CheckinResponseDto> realizarCheckin(
            @RequestBody @Valid CheckinRequestDto requestDto) {
        CheckinResponseDto response = service.realizarCheckin(requestDto);

        return ResponseEntity.ok(response);
    }
}
