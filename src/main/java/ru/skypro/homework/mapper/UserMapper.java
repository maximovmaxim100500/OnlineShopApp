package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;
import org.springframework.security.core.userdetails.UserDetails;
import ru.skypro.homework.controller.dto.RegisterDTO;
import ru.skypro.homework.controller.dto.UpdateUserDTO;
import ru.skypro.homework.controller.dto.UserDTO;
import ru.skypro.homework.db.entity.User;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        imports = {ru.skypro.homework.controller.dto.enums.Role.class,
                ru.skypro.homework.db.entity.UserAvatar.class
        }
)
public interface UserMapper {

    @Mappings(value = {
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "email", source = "userName"),
            @Mapping(target = "image", ignore = true)
    })
    User registerDTOToUser(RegisterDTO register);

    @Mappings(value = {
            @Mapping(target = "id", constant = "0"),
            @Mapping(target = "email", expression = "java(userDetails.getUsername())"),
            @Mapping(target = "firstName", constant = "firstName"),
            @Mapping(target = "lastName", constant = "lastName"),
            @Mapping(target = "phone", constant = "+7 (111) 111-11-11"),
            @Mapping(target = "role", expression = "java(Role.USER)"),
            @Mapping(target = "image", ignore = true)
    })
    User userDetailsToUser(UserDetails userDetails);

    UserDTO userToUserDTO(User user);

    @Mappings(value = {
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "email", ignore = true),
            @Mapping(target = "password", ignore = true),
            @Mapping(target = "role", ignore = true),
            @Mapping(target = "image", ignore = true)
    })
    User updateUserDTOToUser(UpdateUserDTO userDTO);

}
