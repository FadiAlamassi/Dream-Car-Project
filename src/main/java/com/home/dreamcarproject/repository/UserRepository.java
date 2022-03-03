package com.home.dreamcarproject.repository;

import com.home.dreamcarproject.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


public interface UserRepository extends CrudRepository<User,Long> {
    User getById(Long user_id);
    User findByEmail(String email);
    User save(User user);
}

