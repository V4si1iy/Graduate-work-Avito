package ru.skypro.homework.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.exception.EntityExistsException;
import ru.skypro.homework.exception.InvalidLoginPasswordException;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.model.dto.Login;
import ru.skypro.homework.model.dto.Register;
import ru.skypro.homework.model.dto.Role;
import ru.skypro.homework.model.entity.UserModel;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AuthService;
import ru.skypro.homework.service.CustomUserDetailsService;
import ru.skypro.homework.service.UserService;

import java.util.Objects;

@Service
@AllArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final UserRepository repository;
    private final UserMapper userMapper;
    private final CustomUserDetailsService manager;
    private final PasswordEncoder encoder;


    /**
     * авторизация пользователя
     *
     * @param login
     * @return true or false
     */
    @Override
    public boolean login(Login login) throws InvalidLoginPasswordException {

        if (!repository.findByUsername(login.getUsername()).isPresent()) {
            log.error("AuthServiceImpl: login: 'username' LoginNoExist");
            throw new InvalidLoginPasswordException("AuthServiceImpl: login: 'username' LoginNoExist");
        }
        UserDetails userDetails = manager.loadUserByUsername(login.getUsername());
        return encoder.matches(login.getPassword(), userDetails.getPassword());
    }



    /**
     * регистрация пользователя
     * @param register
     * @return
     */
    @Override
    @Transactional
    public boolean register(Register register, Role role) throws InvalidLoginPasswordException {

        if (repository.findByUsername(register.getUsername()).isPresent()) {
            log.error("AuthServiceImpl: register: 'username' InvalidLogin");
            throw new InvalidLoginPasswordException("AuthServiceImpl: register: 'username' InvalidLogin");
        }
        UserModel userModel = userMapper.mapRegisterToUserModel(register, new UserModel());
        userModel.setPassword(encoder.encode(register.getPassword()));
        userModel.setRole(Objects.requireNonNullElse(role, Role.USER));
        repository.save(userModel);
        if(register.getRole()==Role.ADMIN){
            log.info("New ADMIN was created with username - {}", register.getUsername());
        } else {
            log.debug("New USER was created with username - {}", register.getUsername());

        }
        return true;
    }



}
