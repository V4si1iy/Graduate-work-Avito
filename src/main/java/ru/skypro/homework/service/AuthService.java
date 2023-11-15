package ru.skypro.homework.service;

import ru.skypro.homework.model.dto.Login;
import ru.skypro.homework.model.dto.Register;
import ru.skypro.homework.model.dto.Role;

public interface AuthService {

    boolean login(Login login);

    boolean register(Register register, Role role);
}
