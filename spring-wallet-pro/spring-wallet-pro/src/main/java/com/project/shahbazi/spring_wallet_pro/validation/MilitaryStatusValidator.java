package com.project.shahbazi.spring_wallet_pro.validation;

import com.project.shahbazi.spring_wallet_pro.entity.CustomerEntity;
import com.project.shahbazi.spring_wallet_pro.entity.Gender;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;

public class MilitaryStatusValidator implements ConstraintValidator<ValidMilitaryStatus, CustomerEntity> {
    @Override
    public void initialize(ValidMilitaryStatus constraintAnnotation) {
        // No initialization required for this validator
    }

    @Override
    public boolean isValid(CustomerEntity customer, ConstraintValidatorContext context) {
        // Check if the customer is male and the birthdate is provided
        if (customer.getGender() == Gender.MALE && customer.getBirthdate() != null) {
            int age = Period.between(customer.getBirthdate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), LocalDate.now()).getYears();
            if (age >= 18) {
                return customer.getMilitaryServiceStatus() != null && !customer.getMilitaryServiceStatus().isEmpty();
            }
        }
        // If not male or age less than 18, return true (no validation needed)
        return true;
    }
}
