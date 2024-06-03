package com.tien.lakesidehotelnewversion.exception;

public class UserAlreadyExistException extends RuntimeException {
    public UserAlreadyExistException(String message)  {
        super(message);
    }
}
