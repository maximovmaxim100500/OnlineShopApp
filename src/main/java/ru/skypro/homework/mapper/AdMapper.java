package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import ru.skypro.homework.controller.dto.AdDto;
import ru.skypro.homework.controller.dto.CreateOrUpdateAd;
import ru.skypro.homework.controller.dto.ExtendedAd;
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

    @Mapping(target = "pk", source = "id")
    @Mapping(target = "authorFirstName",source = "user.firstName")
    @Mapping(target = "authorLastName",source = "user.lastName")
    @Mapping(target = "email",source = "user.email")
    @Mapping(target = "phone",source = "user.phone")
    ExtendedAd toExtendedAds(Ad ads);

    void updateAd(CreateOrUpdateAd createOrUpdateAd, @MappingTarget Ad ad);

}
