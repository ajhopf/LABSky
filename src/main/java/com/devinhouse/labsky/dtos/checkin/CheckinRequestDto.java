package com.devinhouse.labsky.dtos.checkin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckinRequestDto {
    @NotBlank(message = "É necessário inserir o cpf do passageiro.")
    private String cpf;
    @NotBlank(message = "É necessário escolher um assento para realizar o checkin.")
    private String assento;
    @NotNull(message = "É necessário responder se as malas foram ou não despachadas")
    private Boolean malasDespachadas;
}
