package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import ru.skypro.homework.controller.dto.MyUserDetailsDto;
import ru.skypro.homework.controller.dto.Register;
import ru.skypro.homework.controller.dto.UpdateUser;
import ru.skypro.homework.controller.dto.UserDto;
import ru.skypro.homework.db.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "username", target = "email")
    User toUser(Register register);

    MyUserDetailsDto toMyUserDetailsDto(User user);

    UserDto toDto(User user);

    User toEntity(UserDto userDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(source = "role", target = "role")
    void updateEntityFromDto(UserDto userDto, @MappingTarget User user);

    void updateUser(UpdateUser updateAdDto, @MappingTarget User user);
}
