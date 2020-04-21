package com.user.management.repository;

import com.user.management.model.Car;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author patel
 */
@Repository
public interface CarRepository extends MongoRepository<Car, String> {

    Boolean existsCarById(String id);

}
