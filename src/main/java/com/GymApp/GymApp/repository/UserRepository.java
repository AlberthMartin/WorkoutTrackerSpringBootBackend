package com.GymApp.GymApp.repository;

import com.GymApp.GymApp.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    boolean existsByEmail(String email);

    User findByEmail(String email);
}
