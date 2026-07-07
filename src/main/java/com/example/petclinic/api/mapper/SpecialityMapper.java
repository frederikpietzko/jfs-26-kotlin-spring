package com.example.petclinic.api.mapper;

import com.example.petclinic.api.dto.SpecialityRequest;
import com.example.petclinic.api.dto.SpecialityResponse;
import com.example.petclinic.domain.entity.Speciality;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface SpecialityMapper {

    SpecialityResponse toSpecialityResponse(Speciality speciality);

    Speciality toSpeciality(SpecialityRequest specialityRequest);
}
