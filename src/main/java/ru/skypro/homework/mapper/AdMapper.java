package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;
import ru.skypro.homework.controller.dto.AdDTO;
import ru.skypro.homework.controller.dto.CreateOrUpdateAdDTO;
import ru.skypro.homework.controller.dto.ExtendedAdDTO;
import ru.skypro.homework.db.entity.Ad;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        imports = {
                ru.skypro.homework.db.entity.User.class,
                ru.skypro.homework.db.entity.Ad.class
        }
)
public interface AdMapper {

    @Mappings(value = {
            @Mapping(target = "author", expression = "java(ad.getUser().getId())"),
            @Mapping(target = "image", expression = """
                    java("/images/" + ad.getPk())
                    """)
    })
    AdDTO adToAdDTO(Ad ad);

    Ad createOrUpdateAdDTOToAd(CreateOrUpdateAdDTO adDTO);

    @Mappings(value = {
            @Mapping(target = "authorFirstName", expression = "java(ad.getUser().getFirstName())"),
            @Mapping(target = "authorLastName", expression = "java(ad.getUser().getLastName())"),
            @Mapping(target = "email", expression = "java(ad.getUser().getEmail())"),
            @Mapping(target = "image", expression = """
                    java("/images/" + ad.getPk())
                    """)
    })
    ExtendedAdDTO adToExtendedAdDTO(Ad ad);

}
