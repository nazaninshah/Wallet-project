package com.project.shahbazi.spring_wallet_pro.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shahbazi.spring_wallet_pro.validation.ValidMilitaryStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.util.Date;
import java.util.Set;

@ValidMilitaryStatus // Custom validation annotation applied at the class level
@Entity
@Table(name = "customer_table")
public class CustomerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "National Id cannot be null or empty")
    @Pattern(regexp = "^\\d{10}$",
            message = "National id is not valid!")
    @Column(nullable = false, unique = true)
    private String nationalId;

    @NotEmpty(message = "phone number is cannot be null or empty")
    @Pattern(regexp = "^\\d{11}$",
            message = "Your phone number is not valid!")
    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @NotEmpty(message = "name cannot be null or empty")
    @Column(nullable = false)
    private String name;

    @NotEmpty(message = "surname cannot be null or empty")
    @Column(nullable = false)
    private String surname;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date birthdate;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    // This field will be validated based on age and gender
    private String militaryServiceStatus; // Optional, depend on the gender and age

    @NotEmpty(message = "Email cannot be null or empty")
    @Email(message = "Your email is not valid!")
    @Column(nullable = false, unique = true)
    private String email;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    //@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Set<AccountEntity> accounts;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId (String nationalId) {
        this.nationalId = nationalId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber (String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname (String surname) {
        this.surname = surname;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getMilitaryServiceStatus() {
        return militaryServiceStatus;
    }

    public void setMilitaryServiceStatus(String militaryServiceStatus) {
        this.militaryServiceStatus = militaryServiceStatus;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail (String email) {
        this.email = email;
    }

    public Set<AccountEntity> getAccounts() {
        return accounts;
    }

    public void setAccounts(Set<AccountEntity> accounts) {
        this.accounts = accounts;
    }
}


