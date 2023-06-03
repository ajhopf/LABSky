package com.devinhouse.labsky.dtos.checkin;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckinResponseDto {
    private String eticket;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime dataHoraConfirmacao;

    public CheckinResponseDto(String eticket) {
        this.eticket = eticket;
        this.dataHoraConfirmacao = LocalDateTime.now();
    }
}
