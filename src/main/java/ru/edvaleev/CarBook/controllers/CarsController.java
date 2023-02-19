package ru.edvaleev.CarBook.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.edvaleev.CarBook.dto.CarDTO;
import ru.edvaleev.CarBook.dto.DataBaseDTO;
import ru.edvaleev.CarBook.models.Car;
import ru.edvaleev.CarBook.services.CarsService;
import ru.edvaleev.CarBook.util.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/car-book")
public class CarsController {

    private final CarsService carsService;

    private final ModelMapper modelMapper;

    @Autowired
    public CarsController(CarsService carsService, ModelMapper modelMapper) {
        this.carsService = carsService;
        this.modelMapper = modelMapper;
    }

    @GetMapping()
    public List<Car> getCars(@RequestParam(required = false, name = "brand") @Valid String brand,
                          @RequestParam(required = false, name = "color") @Valid String color,
                          @RequestParam(required = false, name = "yearOfManufacture") @Valid Integer yearOfManufacture) {

        List<Car> foundCarsList = carsService.findAll();

        List<Car> foundParamCarsList = new ArrayList<>();
        if(brand != null) {
            for (Car car : foundCarsList) {
                if (car.getBrand().equals(brand))
                    foundParamCarsList.add(car);
            }
            return foundParamCarsList;
        } else if(color != null) {
            for (Car car : foundCarsList) {
                if (car.getColor().equals(color))
                    foundParamCarsList.add(car);
            }
            return foundParamCarsList;
        } else if(yearOfManufacture != null) {
            for (Car car : foundCarsList) {
                if (car.getYearOfManufacture() == yearOfManufacture)
                    foundParamCarsList.add(car);
            }
            return foundParamCarsList;
        }
        return foundCarsList;
    }

    @GetMapping("/{id}")
    public CarDTO getCar(@PathVariable("id") int id) {
        return convertToCarDTO(carsService.findOne(id));
    }

    @PostMapping
    public ResponseEntity create(@RequestBody @Valid CarDTO carDTO,
                                             BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            StringBuilder errorMsg = new StringBuilder();

            List<FieldError> errors = bindingResult.getFieldErrors();
            for(FieldError error : errors) {
                errorMsg.append(error.getField())
                        .append(" - ").append(error.getDefaultMessage())
                        .append(";");
            }

            throw new CarNotCreatedException(errorMsg.toString());
        }

        carsService.save(convertToCar(carDTO));

        return ResponseEntity.status(HttpStatus.OK).body("Автомобиль успешно добавлен в базу данных!");
    }

    @PatchMapping("/{id}")
    public ResponseEntity update(@PathVariable("id") int id, @RequestParam(required = false, name = "brand") @Valid String brand,
                                 @RequestParam(required = false, name = "color") @Valid String color,
                                 @RequestParam(required = false, name = "yearOfManufacture") @Valid Integer yearOfManufacture) {
        Car car = carsService.findOne(id);

        if(brand != null) car.setBrand(brand);
        if(color != null) car.setColor(color);
        if(yearOfManufacture != null) car.setYearOfManufacture(yearOfManufacture);

        carsService.save(car);

        return ResponseEntity.status(HttpStatus.OK).body("Автомобиль успешно обновлен!");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") int id) {
        carsService.delete(id);

        return ResponseEntity.status(HttpStatus.OK).body("Автомобиль успешно удален из базы данных!");
    }

    @GetMapping("/db-statistic")
    public DataBaseDTO getStatistic() {
        DataBaseDTO dataBaseDTO = new DataBaseDTO();

        dataBaseDTO.setCount(carsService.count());
        dataBaseDTO.setFirstEntryDate(carsService.getFirstEntryDate());
        dataBaseDTO.setLastEntryDate(carsService.getLastEntryDate());

        return dataBaseDTO;
    }

    @ExceptionHandler
    private ResponseEntity<CarErrorResponse> handleException(CarNotFoundException e) {
        CarErrorResponse response = new CarErrorResponse(
                "Автомобиль с таким регистрационным номером не был найден",
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<CarErrorResponse> handleException(CarNotCreatedException e) {
        CarErrorResponse response = new CarErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<CarErrorResponse> handleException(CarNotDeletedException e) {
        CarErrorResponse response = new CarErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler
    private ResponseEntity<CarErrorResponse> handleException(CarNotUpdatedException e) {
        CarErrorResponse response = new CarErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    public Car convertToCar(CarDTO carDTO) {
        return modelMapper.map(carDTO, Car.class);
    }

    public CarDTO convertToCarDTO(Car car) {
        return modelMapper.map(car, CarDTO.class);
    }
}
