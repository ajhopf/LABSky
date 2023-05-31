package com.devinhouse.labsky.controllers;

import com.devinhouse.labsky.dtos.PassageiroCompletoResponseDto;
import com.devinhouse.labsky.dtos.PassageiroSimplesResponseDto;
import com.devinhouse.labsky.models.Passageiro;
import com.devinhouse.labsky.services.PassageiroService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/passageiros")
public class PassageiroController {
    @Autowired
    private PassageiroService service;

    private final ModelMapper modelMapper = new ModelMapper();

    @GetMapping
    public ResponseEntity<List<PassageiroCompletoResponseDto>> getPassageiros() {
        List<Passageiro> passageiros = service.getPassageiros();

        List<PassageiroCompletoResponseDto> passageirosResponse = passageiros.stream().map(passageiro -> modelMapper.map(passageiro, PassageiroCompletoResponseDto.class)).toList();

        return ResponseEntity.ok(passageirosResponse);
    }

    @GetMapping("/{cpf}")
    public ResponseEntity<PassageiroSimplesResponseDto> getPassageiro(@PathVariable String cpf) {
        Passageiro passageiro = service.getPassageiroPeloCpf(cpf);
        PassageiroSimplesResponseDto passageiroCompletoResponseDto = modelMapper.map(passageiro, PassageiroSimplesResponseDto.class);
        return ResponseEntity.ok(passageiroCompletoResponseDto);
    }
}
