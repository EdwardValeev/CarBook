package ru.edvaleev.CarBook.util;

public class CarNotCreatedException extends RuntimeException{
    public CarNotCreatedException(String msg){
        super(msg);
    }
}
