package com.example.webservices_cardealer.services;

import com.example.webservices_cardealer.entities.Car;
import com.example.webservices_cardealer.repositories.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarService {

    @Autowired
    private CarRepository carRepository;

    @Cacheable(value = "carCache")
    public List<Car> findAllCarsGuest() {
        var carList = carRepository.findAll();
        carList = carList.stream()
                .map(car -> Car.builder().brand(car.getBrand())
                        .model(car.getModel())
                        .color(car.getColor())
                        .yearModel(car.getYearModel())
                        .isInStock(car.isInStock())
                        .build())
                .collect(Collectors.toList());
        if (carList.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No cars found in the system");
        }else {
            return carList;
        }
    }

    @Cacheable(value = "carCache")
    public List<Car> findAllCars(String registrationNumber,String brand,String model,String color,boolean sortOnBrand,String engineModel,String tireBrand){
        System.out.println("Fresh Car data..."); // use only under development...
        var carList = carRepository.findAll();
        if (registrationNumber != null){
            carList = carList.stream().filter(car -> car.getRegistrationNumber().toLowerCase().equals(registrationNumber.toLowerCase()))
                    .collect(Collectors.toList());
        }
        if (brand != null){
            carList = carList.stream().filter(car -> car.getBrand().toLowerCase().equals(brand.toLowerCase()))
                    .collect(Collectors.toList());
        }
        if (model != null){
            carList = carList.stream().filter(car -> car.getModel().toLowerCase().equals(model.toLowerCase()))
                    .collect(Collectors.toList());
        }
        if (color != null){
            carList = carList.stream().filter(car -> car.getColor().toLowerCase().equals(color.toLowerCase()))
                    .collect(Collectors.toList());
        }
        if (sortOnBrand){
            carList.sort(Comparator.comparing(Car::getBrand));
        }
        if (engineModel != null ){
            carList = carList.stream()
                    .filter(car -> car.getEngine() != null && car.getEngine().getModel().toLowerCase().equals(engineModel.toLowerCase()))
                    .collect(Collectors.toList());
        }
        if (tireBrand != null){
            carList = carList.stream()
                    .filter(car ->  car.getTires() != null && car.getTires().getBrand().toLowerCase().equals(tireBrand.toLowerCase()))
                    .collect(Collectors.toList());
        }
        if (carList.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No cars found in the system");
        }else {
            return carList;
        }
    }

    @Cacheable(value = "carCache", key = "#id")
    public Car findCarById(String id) {
        return carRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                String.format("Car with this id %s. , could not be found", id)));
    }


    @CachePut(value = "carCache")
    public Car saveNewCar(Car car){
        car.setInStock(true);
        return carRepository.save(car);
    }

    @CachePut(value = "carCache", key = "#id")
    public void updateCar(String id, Car car){
        if (!carRepository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,String.format("Could not find car with id %s",id));
        }
        car.setCarId(id);
        carRepository.save(car);
    }

    @CacheEvict(value = "carCache",key="#id")
    public void deleteCar (String id){
        if (!carRepository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Could not find car with id %s",id));
        }
        carRepository.deleteById(id);
    }

}
