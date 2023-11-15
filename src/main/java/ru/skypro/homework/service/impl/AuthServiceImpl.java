package ru.skypro.homework.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import ru.skypro.homework.exception.EntityExistsException;
import ru.skypro.homework.model.dto.Login;
import ru.skypro.homework.model.dto.Register;
import ru.skypro.homework.service.AuthService;
import ru.skypro.homework.service.UserService;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    UserService userService;

    private final UserDetailsManager manager;
    private final PasswordEncoder encoder;

    @Override
    public boolean login(Login login) {
        if (!manager.userExists(login.getUsername())) {
            return false;
        }
        UserDetails userDetails = manager.loadUserByUsername(login.getUsername());
        return encoder.matches(login.getPassword(), userDetails.getPassword());
    }

    @Override
    public boolean register(Register register) {
        if (manager.userExists(register.getUsername())) {
            return false;
        }
        ru.skypro.homework.model.dto.User user = new ru.skypro.homework.model.dto.User();
        user.setRole(register.getRole());
        user.setEmail(register.getUsername());
        user.setPhone(register.getPhone());
        user.setFirstName(register.getFirstName());
        user.setLastName(register.getLastName());
        userService.create(user);
        manager.createUser(
                User.builder()
                        .passwordEncoder(this.encoder::encode)
                        .password(register.getPassword())
                        .username(register.getUsername())
                        .roles(register.getRole().name())
                        .build());
        return true;
    }

}
