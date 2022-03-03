package com.home.dreamcarproject.model;

import javax.persistence.*;
import java.util.Set;
//import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "parts")
public class Parts {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "parts_id")
    private Long id;

    @Column(name = "name")
//    @NotEmpty(message = "*Please provide parts name")
    private String name;

    @Column(name = "description")
//    @NotEmpty(message = "*Please provide parts description")
    private String description;

    @Column(name = "image_url")
    private String imageUrl;

    @OneToMany(mappedBy = "parts", cascade = CascadeType.ALL)
    private Set<Auction> auctions;

    public Parts() {
    }

    public Parts(String name, String description,String imageUrl, Set<Auction> auctions) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.auctions = auctions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Set<Auction> getAuctions() {
        return auctions;
    }

    public void setAuctions(Set<Auction> auctions) {
        this.auctions = auctions;
    }
}
