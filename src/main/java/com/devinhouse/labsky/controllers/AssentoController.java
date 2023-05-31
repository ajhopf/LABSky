package com.devinhouse.labsky.controllers;

import com.devinhouse.labsky.services.AssentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/assentos")
public class AssentoController {
    @Autowired
    private AssentoService service;

    @GetMapping
    public ResponseEntity<List<String>> getAssentos() {
        List<String> assentos = service.getAssentos();

        return ResponseEntity.ok(assentos);
    }
}
