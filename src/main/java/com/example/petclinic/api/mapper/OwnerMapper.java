package com.example.petclinic.api.mapper;

import com.example.petclinic.api.dto.OwnerRequest;
import com.example.petclinic.api.dto.OwnerResponse;
import com.example.petclinic.domain.entity.Owner;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {PetMapper.class})
public interface OwnerMapper {

    OwnerResponse toOwnerResponse(Owner owner);

    Owner toOwner(OwnerRequest ownerRequest);
}
