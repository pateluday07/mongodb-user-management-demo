package com.user.management.repository;

import com.user.management.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author patel
 */
@Repository
public interface UserRespository extends MongoRepository<User, String> {

    Optional<User> findByContactEmail(String email);

    Optional<User> findByContactPhone(String phone);

    boolean existsByContactEmail(String email);

    boolean existsByContactPhone(String phone);
}
