package ru.skypro.homework.mapper;


import org.hibernate.annotations.Source;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.skypro.homework.model.dto.Register;
import ru.skypro.homework.model.dto.User;
import ru.skypro.homework.model.entity.UserModel;

import java.lang.annotation.Target;

@Mapper(componentModel = "spring")
@Component
public interface UserMapper {
    default User mapToUserDto(UserModel userModel) {
    User user = new User();
        user.setId(userModel.getId());
        user.setEmail(userModel.getUsername());
        user.setFirstName(userModel.getFirstName());
        user.setLastName(userModel.getLastName());
        user.setPhone(userModel.getPhone());
        user.setRole(userModel.getRole());
        user.setImage(userModel.getImage());
        return user;
    }
   default UserModel mapUserDtoToUserModel(User user, UserModel userModel) {
        userModel.setId(user.getId());
        userModel.setUsername(user.getEmail());
        userModel.setFirstName(user.getFirstName());
        userModel.setLastName(user.getLastName());
        userModel.setPhone(user.getPhone());
        userModel.setRole(user.getRole());
        return userModel;
    }
   default UserModel mapRegisterToUserModel(Register register, UserModel userModel) {
        userModel.setUsername(register.getUsername());
        userModel.setPassword(register.getPassword());
        userModel.setFirstName(register.getFirstName());
        userModel.setLastName(register.getLastName());
        userModel.setPhone(register.getPhone());
        userModel.setRole(register.getRole());
        return userModel;
    }
}
