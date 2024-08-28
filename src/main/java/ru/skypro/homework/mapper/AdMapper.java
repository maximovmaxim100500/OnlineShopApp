package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import ru.skypro.homework.controller.dto.AdDto;
import ru.skypro.homework.controller.dto.CreateOrUpdateAd;
import ru.skypro.homework.db.entity.Ad;

@Mapper(componentModel = "spring")
public interface AdMapper {
    @Mapping(source = "id", target = "pk")
    @Mapping(source = "user.id", target = "author")
    AdDto toDto(Ad ad);

    @Mapping(source = "pk", target = "id")
    @Mapping(source = "author", target = "user.id")
    Ad toEntity(AdDto adDto);

    Ad toAdsFromCreateAds(CreateOrUpdateAd createOrUpdateAdDto);

//    @Mapping(source = "pk", target = "id")
//    @Mapping(source = "author", target = "user.id")
//    void updateEntityFromDto(AdDto adDto, @MappingTarget Ad ad);

    void updateAd(CreateOrUpdateAd createOrUpdateAd, @MappingTarget Ad ad);

}
