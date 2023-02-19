package ru.edvaleev.CarBook.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.edvaleev.CarBook.models.Car;
import ru.edvaleev.CarBook.repositories.CarsRepository;
import ru.edvaleev.CarBook.util.CarNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class CarsService {

    private final CarsRepository carsRepository;

    @Autowired
    public CarsService(CarsRepository carsRepository) {
        this.carsRepository = carsRepository;
    }

    public List<Car> findAll() {
        return carsRepository.findAll();
    }

    public Car findOne(int id) {
        Optional<Car> foundCar = carsRepository.findById(id);

        return foundCar.orElseThrow(CarNotFoundException::new);
    }

    @Transactional
    public void save(Car car) {
        enrichCar(car);

        carsRepository.save(car);
    }

    @Transactional
    public void delete(int id) {
        Optional<Car> carToDelete = carsRepository.findById(id);
        if(carToDelete.isPresent()){
            carsRepository.delete(carToDelete.get());
        } else {
            throw new CarNotFoundException();
        }
    }

    public long count() {
        return carsRepository.count();
    }

    public LocalDateTime getFirstEntryDate() {
        Optional<Car> firstCar = carsRepository.findFirstByOrderByIdDesc();
        if(firstCar.isPresent()){
            return firstCar.get().getCreatedAt();
        } else {
            throw new CarNotFoundException();
        }
    }

    public LocalDateTime getLastEntryDate() {
        Optional<Car> lastCar = carsRepository.findFirstByOrderByIdAsc();
        if(lastCar.isPresent()){
            return lastCar.get().getCreatedAt();
        } else {
            throw new CarNotFoundException();
        }
    }

    private void enrichCar(Car car) {
        car.setCreatedAt(LocalDateTime.now());
    }
}
