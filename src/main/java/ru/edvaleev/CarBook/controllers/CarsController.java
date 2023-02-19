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
import ru.edvaleev.CarBook.util.CarErrorResponse;
import ru.edvaleev.CarBook.util.CarNotCreatedException;
import ru.edvaleev.CarBook.util.CarNotDeletedException;
import ru.edvaleev.CarBook.util.CarNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

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

    @GetMapping
    public List<CarDTO> getCars() {
        return carsService.findAll().stream().map(this::convertToCarDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{registrationNumber}")
    public CarDTO getCar(@PathVariable("registrationNumber") String registrationNumber) {
        return convertToCarDTO(carsService.findOne(registrationNumber));
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

    @DeleteMapping("/{registrationNumber}")
    public ResponseEntity delete(@PathVariable("registrationNumber") @Valid String registrationNumber) {
        carsService.delete(registrationNumber);

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

    public Car convertToCar(CarDTO carDTO) {
        return modelMapper.map(carDTO, Car.class);
    }

    public CarDTO convertToCarDTO(Car car) {
        return modelMapper.map(car, CarDTO.class);
    }

//    public LocalDateTime convertToLocalDateTime(long timestamp) {
//        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
//    }
}
