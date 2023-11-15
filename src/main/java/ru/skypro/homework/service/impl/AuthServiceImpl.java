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
import ru.skypro.homework.model.dto.Role;
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
    public boolean register(Register register, Role role) {
        if (manager.userExists(register.getUsername())) {
            return false;
        }
        userService.create(register, role);
        return true;
    }

}
