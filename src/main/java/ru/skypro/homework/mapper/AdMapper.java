package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import ru.skypro.homework.controller.dto.AdDto;
import ru.skypro.homework.db.entity.Ad;

@Mapper(componentModel = "spring")
public interface AdMapper {
    AdDto toDto(Ad ad);
    Ad toEntity(AdDto adDto);
    void updateEntityFromDto(AdDto adDto, @MappingTarget Ad ad);
}
