package ru.edvaleev.CarBook.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Car")
public class Car {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "registration_number")
    @Pattern(regexp = "[А-Я]{2}\\d{3}[A-Я]\\d{2,3}",
            message = "Регистрационный номер должен быть в следующем формате: АА999А96 или АА999А996")
    private String registrationNumber;

    @Column(name = "brand")
    @NotEmpty(message="Марка не должна быть пустой")
    private String brand;

    @Column(name = "color")
    @NotEmpty(message="Цвет не должен быть пустым")
    private String color;

    @Column(name = "year_of_manufacture")
    @Min(value = 1885, message = "Год выпуска должен быть позже 1885 года")
    @Max(value = 2024, message = "Год выпуска должен быть раньше 2024 года")
    private int yearOfManufacture;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Car() {}

    public Car(String registrationNumber, String brand, String color, int yearOfManufacture) {
        this.registrationNumber = registrationNumber;
        this.brand = brand;
        this.color = color;
        this.yearOfManufacture = yearOfManufacture;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
