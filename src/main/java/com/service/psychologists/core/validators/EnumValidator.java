package com.service.psychologists.core.validators;

import com.service.psychologists.core.annotations.Enum;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.HashSet;
import java.util.Set;

public class EnumValidator implements ConstraintValidator<Enum, String> {
    private final Set<String> acceptedValues = new HashSet<>();


    @Override
    public void initialize(Enum constraintAnnotation) {
        Class<? extends java.lang.Enum<?>> enumClass = constraintAnnotation.enumClass();
        for (java.lang.Enum<?> enumVal : enumClass.getEnumConstants()) {
            acceptedValues.add(enumVal.name());
        }
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // use @NotNull to check for null values
        }
        return acceptedValues.contains(value);
    }
}
