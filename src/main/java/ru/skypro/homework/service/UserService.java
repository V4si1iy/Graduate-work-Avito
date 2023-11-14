package ru.skypro.homework.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.skypro.homework.exception.EntityExistsException;
import ru.skypro.homework.exception.EntityNotFoundException;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.model.dto.User;
import ru.skypro.homework.model.entity.UserModel;
import ru.skypro.homework.repository.UserRepository;

import java.util.Objects;

@Service
@Slf4j
@AllArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final UserMapper mapper;

    public User create(User user) throws EntityExistsException {
        log.info("Executing the method to create a new User");

        // Преобразование DTO в сущность
        UserModel userModel = mapper.mapUserDtoToUserModel(user);

        // Проверка наличия объявления по уникальному идентификатору
        if (userModel.getId() != null && repository.existsById(userModel.getId())) {
            log.error("Failed to create User. User with id {} already exists.", userModel.getId());
            throw new EntityExistsException("User with id " + userModel.getId() + " already exists");
        }

        // Сохранение сущности
        UserModel savedUserModel = repository.save(userModel);

        // Преобразование сохраненной сущности обратно в DTO и возврат
        User savedComment = mapper.mapToUserDto(savedUserModel);

        log.info("User with id {} created successfully", savedComment.getId());
        return savedComment;
    }

    public User getUserById(Long uesrId) throws EntityNotFoundException {
        log.info("Fetching User with id {}", uesrId);

        try {
            UserModel userModel = repository.findById(uesrId)
                    .orElseThrow(() -> new EntityNotFoundException("User with id " + uesrId + " not found"));

            log.info("Fetched User: {}", userModel);
            return mapper.mapToUserDto(userModel);
        } catch (Exception e) {
            log.error("Error fetching User with id {}", uesrId, e);
            throw e;
        }
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

    public void deleteUser(Long userId) throws EntityNotFoundException {
        log.info("Deleting Comment with id {}", userId);
        try {
            UserModel existingCommentModel = repository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("Ad with id " + userId + " not found"));

            repository.delete(existingCommentModel);

            log.info("Comment with id {} deleted successfully", userId);
        } catch (Exception e) {
            log.error("Error deleting Comment with id {}", userId, e);
            throw e;
        }
    }

}
}
