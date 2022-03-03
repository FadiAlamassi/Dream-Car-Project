package com.home.dreamcarproject.repository;

import com.home.dreamcarproject.model.Parts;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


public interface PartsRepository extends CrudRepository<Parts, Long> {
    Parts save(Parts parts);

    void deleteById(Long id);

    Parts getById(Long id);

    Parts findByName(String name);
}
