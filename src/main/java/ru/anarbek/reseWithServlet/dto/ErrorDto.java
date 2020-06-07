package ru.anarbek.reseWithServlet.dto;

public class ErrorDto {

    public String errorMessage;
    public int errorCode;

    public ErrorDto(String errorMessage, int errorCode) {
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }
}
