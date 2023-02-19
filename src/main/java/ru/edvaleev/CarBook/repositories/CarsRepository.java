package ru.edvaleev.CarBook.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.edvaleev.CarBook.models.Car;

import java.util.Optional;

@Repository
public interface CarsRepository extends JpaRepository<Car, Integer> {
    Optional<Car> findById(int id);

    Optional<Car> findFirstByOrderByIdAsc();

    Optional<Car> findFirstByOrderByIdDesc();



}
