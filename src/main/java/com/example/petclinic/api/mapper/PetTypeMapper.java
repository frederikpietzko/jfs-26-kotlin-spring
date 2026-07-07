package com.example.petclinic.api.mapper;

import com.example.petclinic.api.dto.PetTypeRequest;
import com.example.petclinic.domain.entity.PetType;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PetTypeMapper {

    PetTypeRequest toPetTypeRequest(PetType petType);

    PetType toPetType(PetTypeRequest petTypeRequest);
}
