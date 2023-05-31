package com.devinhouse.labsky.mappers;

import com.devinhouse.labsky.dtos.PassageiroResponseDto;
import com.devinhouse.labsky.models.Passageiro;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PassageiroMapper {
    PassageiroResponseDto map(Passageiro map);
    List<PassageiroResponseDto>  map(List<Passageiro> map);
}
