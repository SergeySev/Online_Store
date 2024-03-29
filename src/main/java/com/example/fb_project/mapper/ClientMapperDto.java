package com.example.fb_project.mapper;

import com.example.fb_project.dto.ClientDto;
import com.example.fb_project.entity.Brand;
import com.example.fb_project.entity.Client;
import org.bson.types.ObjectId;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClientMapperDto {
    ClientDto toDto(Client client);

    default String map(ObjectId id) {
        return id.toString();
    }

    default String map(Brand brand) {
        return brand.getTitle();
    }
}
