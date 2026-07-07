package com.example.petclinic.api.mapper;

import com.example.petclinic.api.dto.PetRequest;
import com.example.petclinic.api.dto.PetResponse;
import com.example.petclinic.api.dto.PetTypeRequest;
import com.example.petclinic.domain.entity.Pet;
import com.example.petclinic.domain.entity.PetType;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {VisitMapper.class})
public interface PetMapper {

    PetResponse toPetResponse(Pet pet);

    Pet toPet(PetRequest petRequest);

    default PetTypeRequest mapPetType(PetType petType) {
        return petType != null ? PetTypeRequest.valueOf(petType.name()) : null;
    }

    default PetType mapPetTypeRequest(PetTypeRequest petTypeRequest) {
        return petTypeRequest != null ? PetType.valueOf(petTypeRequest.name()) : null;
    }
}
