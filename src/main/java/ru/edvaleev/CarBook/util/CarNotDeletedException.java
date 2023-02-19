package ru.edvaleev.CarBook.util;

public class CarNotDeletedException extends RuntimeException{
    public CarNotDeletedException(String msg) {
        super(msg);
    }
}
