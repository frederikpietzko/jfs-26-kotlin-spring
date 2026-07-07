package com.example.petclinic.api.mapper;

import com.example.petclinic.api.dto.VisitRequest;
import com.example.petclinic.api.dto.VisitResponse;
import com.example.petclinic.domain.entity.Visit;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface VisitMapper {

    VisitResponse toVisitResponse(Visit visit);

    Visit toVisit(VisitRequest visitRequest);
}
