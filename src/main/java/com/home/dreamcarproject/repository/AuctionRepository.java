package com.home.dreamcarproject.repository;

import com.home.dreamcarproject.model.Auction;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


public interface AuctionRepository extends CrudRepository<Auction, Long> {
    Auction save(Auction auction);

    void deleteById(Long id);

    Auction getById(Long id);

    Iterable<Auction> findByStatus(String status);
}
