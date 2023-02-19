package ru.edvaleev.CarBook.util;

public class CarNotUpdatedException extends RuntimeException{
    public CarNotUpdatedException(String msg){
        super(msg);
    }
}
