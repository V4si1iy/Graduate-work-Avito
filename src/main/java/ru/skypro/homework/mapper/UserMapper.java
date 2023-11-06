package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import ru.skypro.homework.model.dto.User;
import ru.skypro.homework.model.dto.UserModel;

@Mapper
public interface UserMapper {
    default User mapToUserDto(UserModel userModel) {
        User user = new User();
        user.setId(userModel.getId());
        user.setEmail(userModel.getUsername());
        user.setFirstName(userModel.getFirstName());
        user.setLastName(userModel.getLastName());
        user.setPhone(userModel.getPhone());
        user.setRole(userModel.getRole());
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
}
