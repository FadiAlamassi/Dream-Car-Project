package com.home.dreamcarproject.model;

//import javax.validation.constraints.NotNull;

import javax.persistence.*;

@Entity
@Table(name = "offer")
public class Offer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "offer_id")
    private Long id;

    @Column(name = "price_per_parts")
//    @NotNull(message = "*Please provide your price per parts")
    private Double pricePerParts;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    private String status;

    @ManyToOne
    @JoinColumn(name = "auction_id")
    private Auction auction;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Offer() {
    }

    public Offer(Double pricePerParts, String description, String status, Auction auction, User user) {
        this.pricePerParts = pricePerParts;
        this.description = description;
        this.status = status;
        this.auction = auction;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getPricePerParts() {
        return pricePerParts;
    }

    public void setPricePerParts(Double pricePerParts) {
        this.pricePerParts = pricePerParts;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Auction getAuction() {
        return auction;
    }

    public void setAuction(Auction auction) {
        this.auction = auction;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
