package com.devinhouse.labsky.controllers;

import com.devinhouse.labsky.dtos.PassageiroResponseDto;
import com.devinhouse.labsky.models.Passageiro;
import com.devinhouse.labsky.services.PassageiroService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
    public ResponseEntity<List<PassageiroResponseDto>> getPassageiros() {
        List<Passageiro> passageiros = service.getPassageiros();

        List<PassageiroResponseDto> passageirosResponse = passageiros.stream().map(passageiro -> modelMapper.map(passageiro, PassageiroResponseDto.class)).toList();

        return ResponseEntity.ok(passageirosResponse);
    }
}
