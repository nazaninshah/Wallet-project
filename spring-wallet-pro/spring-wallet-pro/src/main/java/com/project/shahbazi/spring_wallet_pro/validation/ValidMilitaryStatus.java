package com.project.shahbazi.spring_wallet_pro.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE}) // Apply at the class level (CustomerEntity)
@Retention(RetentionPolicy.RUNTIME) // the annotation is available at runtime
@Constraint(validatedBy = MilitaryStatusValidator.class) //uses the MilitaryStatusValidator class to perform the validation logic
public @interface ValidMilitaryStatus {
    String message() default "Military status must be clarified for men aged 18 or more";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
