package ru.skypro.homework.exception;

public class InvalidLoginPasswordException extends Exception{
    public InvalidLoginPasswordException(String message) {
        super(message);
    }
}
