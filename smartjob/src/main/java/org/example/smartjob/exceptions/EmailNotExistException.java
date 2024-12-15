package org.example.smartjob.exceptions;

public class EmailNotExistException extends RuntimeException {
    public EmailNotExistException(String message) {
        super(message);
    }
}
