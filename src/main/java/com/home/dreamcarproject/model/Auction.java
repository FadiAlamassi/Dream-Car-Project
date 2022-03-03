package com.home.dreamcarproject.model;

import org.springframework.format.annotation.DateTimeFormat;
//import javax.validation.constraints.NotNull;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "auction")
public class Auction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "target_price")
//    @NotNull(message = "*Please provide target price")
    private Double targetPricePerParts;

    @Column(name = "currency")
//    @NotNull(message = "*Please provide currency")
    private String currency;

    @Column(name = "expiration_date")
//    @NotNull(message = "*Please provide expirationDate")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date expirationDate;

    @Column(name = "number_of_parts")
//    @NotNull(message = "*Please provide number of parts")
    private Integer numberOfParts;

    @Column(name = "status")
//    @NotNull(message = "*Please provide status")
    private String status;

    @ManyToOne
    @JoinColumn(name = "parts_id")
    private Parts parts;

    @OneToMany(mappedBy = "auction", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Offer> offers;

    public Auction() {
    }

    public Auction(Double targetPricePerParts, String currency, Date expirationDate, Integer numberOfParts, String status, Parts parts, List<Offer> offers) {
        this.targetPricePerParts = targetPricePerParts;
        this.currency = currency;
        this.expirationDate = expirationDate;
        this.numberOfParts = numberOfParts;
        this.status = status;
        this.parts = parts;
        this.offers = offers;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getTargetPricePerParts() {
        return targetPricePerParts;
    }

    public void setTargetPricePerParts(Double targetPricePerParts) {
        this.targetPricePerParts = targetPricePerParts;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Integer getNumberOfParts() {
        return numberOfParts;
    }

    public void setNumberOfParts(Integer numberOfParts) {
        this.numberOfParts = numberOfParts;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Parts getParts() {
        return parts;
    }

    public void setParts(Parts parts) {
        this.parts = parts;
    }

    public List<Offer> getOffers() {
        return offers;
    }

    public void setOffers(List<Offer> offers) {
        this.offers = offers;
    }
}
