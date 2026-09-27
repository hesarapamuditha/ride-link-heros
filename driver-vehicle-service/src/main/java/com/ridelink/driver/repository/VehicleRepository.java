package com.ridelink.driver.repository;

import com.ridelink.driver.model.Vehicle;
import com.ridelink.driver.model.VehicleStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends MongoRepository<Vehicle, String> {

    List<Vehicle> findByDriverId(String driverId);

    List<Vehicle> findByDriverIdAndStatus(String driverId, VehicleStatus status);

    Optional<Vehicle> findByPlateNumber(String plateNumber);

    Optional<Vehicle> findByIdAndDriverId(String id, String driverId);

    boolean existsByPlateNumber(String plateNumber);
}
