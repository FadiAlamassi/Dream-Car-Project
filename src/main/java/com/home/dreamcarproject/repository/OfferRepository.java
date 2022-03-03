package com.home.dreamcarproject.repository;

import com.home.dreamcarproject.model.Auction;
import com.home.dreamcarproject.model.Offer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Optional;


public interface OfferRepository extends CrudRepository<Offer, Long> {
    Offer save(Offer offer);


    @Transactional
    @Modifying
    @Query("DELETE FROM Offer o WHERE o.id = :pid")
    void deleteByPid(@Param("pid") Long theId);

    Offer getById(Long id);

    Iterable<Offer> findByAuction(Auction auction);
}