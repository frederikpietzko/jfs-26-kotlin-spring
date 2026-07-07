package com.example.petclinic.api.mapper;

import com.example.petclinic.api.dto.VetRequest;
import com.example.petclinic.api.dto.VetResponse;
import com.example.petclinic.domain.entity.Speciality;
import com.example.petclinic.domain.entity.Vet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", uses = {SpecialityMapper.class})
public interface VetMapper {

    VetResponse toVetResponse(Vet vet);

    @Mapping(source = "specialityIds", target = "specialties")
    Vet toVet(VetRequest vetRequest);

    default Set<Speciality> mapSpecialityIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return new HashSet<>();
        }
        Set<Speciality> specialities = new HashSet<>();
        ids.forEach(id -> specialities.add(new Speciality(id, null, new HashSet<>())));
        return specialities;
    }
}
