package ru.skypro.homework.exception;

public class EntityExistsException extends Exception{
    public EntityExistsException(String message)
    {
        super(message);
    }
}
