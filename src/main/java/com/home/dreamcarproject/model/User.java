package com.home.dreamcarproject.model;




import javax.persistence.*;
import java.util.Set;
//import javax.validation.constraints.Email;
//import javax.validation.constraints.NotEmpty;
//import javax.validation.constraints.Pattern;
//import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "name")
//    @NotEmpty(message = "*Please provide your name")
    private String name;

    @Column(name = "email")
//    @Email(message = "*Please provide a valid Email")
//    @NotEmpty(message = "*Please provide an email")
    private String email;

    @Column(name = "password")
//    @Length(min = 5, message = "*Your password must have at least 5 characters")
//    @NotEmpty(message = "*Please provide your password")
    private String password;

    @Column(name = "phone_number")
//    @NotEmpty(message = "*Please provide your phone number")
//    @Pattern(regexp = "(^$|[0-9]{10})", message = "*Please provide a valid phone number")
    private String phoneNumber;

    @Column(name = "active")
    private int active;

    @Column(name = "role")
    private String role;

    @Column(name = "company_name")
    private String company;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Offer> offers;

//curl -i -X POST -d "{"role":"Admin", "username":"fadi","password":"fadi","email":"fadi@gmail.com","phoneNumber":"123456789"}" http://localhost:8080/users/ -H "Content-Type: application/json"
// curl -i -X POST -d "{\"role\":\"Admin\", \"username\":\"fadi\",\"password\":\"fadi\",\"email\":\"fadi@gmail.com\",\"phoneNumber\":\"123456789\"}" http://localhost:8080/users/ -H "Content-Type: application/json"

    public User(String name, String email, String password, String phoneNumber, int active, String role, String company, Set<Offer> offers) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.active = active;
        this.role = role;
        this.company = company;
        this.offers = offers;
    }

    public User() {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Set<Offer> getOffers() {
        return offers;
    }

    public void setOffers(Set<Offer> offers) {
        this.offers = offers;
    }

}