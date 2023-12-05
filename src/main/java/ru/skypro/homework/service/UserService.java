package ru.skypro.homework.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.exception.EntityNotFoundException;
import ru.skypro.homework.exception.InvalidLoginPasswordException;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.model.dto.*;
import ru.skypro.homework.model.entity.Image;
import ru.skypro.homework.model.entity.UserModel;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.repository.UserRepository;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
public class UserService {
    private final UserRepository repository;

    private final UserMapper userMapper;
    private final CustomUserDetailsService manager;
    private final PasswordEncoder encoder;
    private final ImageRepository imageRepository;


    public User getUserByUsernameDto(String username) throws EntityNotFoundException {
        return userMapper.mapToUserDto(getUserByUsername(username));
    }

    protected UserModel getUserByUsername(String username) throws EntityNotFoundException {
        log.info("Fetching User with username {}", username);

        try {
            UserModel userModel = repository.getUserModelByUsername(username);
            if (Objects.isNull(userModel))
                throw new EntityNotFoundException("User with username " + username + " not found");

            log.info("Fetched User: {}", userModel);
            return userModel;
        } catch (Exception e) {
            log.error("Error fetching User with username {}", username, e);
            throw e;
        }
    }

    public void setNewPassword(NewPasswordDTO newPassword, String user) throws EntityNotFoundException, InvalidLoginPasswordException {
        UserDetails userDetails = manager.loadUserByUsername(user);

        if (!repository.findByUsername(user).isPresent() || !encoder.matches(newPassword.getCurrentPassword(), userDetails.getPassword())) {
            throw new InvalidLoginPasswordException("Incorrect User");
        }
        UserModel userModel = getUserByUsername(user);
        String encode = encoder.encode(newPassword.getNewPassword());
        userModel.setPassword(encode);
        repository.save(userModel);
    }

    public UpdateUser updateUser(UpdateUser user, String username) throws EntityNotFoundException {
        log.info("Updating User with username {}", username);

        // Проверка существования пользователя
        UserModel userModel = getUserByUsername(username);

        userModel.setFirstName(user.getFirstName());
        userModel.setLastName(user.getLastName());
        userModel.setPhone(user.getPhone());

        // Сохранение обновленной сущности
        UserModel updatedUserModel = repository.save(userModel);

        log.info("User with username {} updated successfully", username);
        return user;
    }

    public void updateUserImage(MultipartFile file, String username) throws EntityNotFoundException, IOException {
        // Проверка существования пользователя
        UserModel userModel = getUserByUsername(username);
        // Сохранения фотографии в бд
        Image image = imageRepository.getImageByLink(userModel.getImage());
        if (Objects.isNull(image)) {
            image = new Image();
            image.setId(UUID.randomUUID().toString());
        }
        image.setFileSize(file.getSize());
        image.setData(file.getBytes());
        image.setMediaType(file.getContentType());
        image.setLink("/image/" + image.getId());
        userModel.setImage("/image/" + image.getId());
        repository.save(userModel);
        imageRepository.save(image);

    }


}
