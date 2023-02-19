package ru.edvaleev.CarBook.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public class CarDTO {

    @Pattern(regexp = "[А-Я]{2}\\d{3}[A-Я]\\d{2,3}",
            message = "Регистрационный номер должен быть в следующем формате: АА999А96 или АА999А996")
    private String registrationNumber;

    @NotEmpty(message="Марка не должна быть пустой")
    private String brand;

    @NotEmpty(message="Цвет не должен быть пустым")
    private String color;

    @Min(value = 1885, message = "Год выпуска не должен быть раньше 1886 года")
    @Max(value = 2024, message = "Год выпуска не должен быть позже 2023 года")
    private int yearOfManufacture;

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getYearOfManufacture() {
        return yearOfManufacture;
    }

    public void setYearOfManufacture(int yearOfManufacture) {
        this.yearOfManufacture = yearOfManufacture;
    }
}
