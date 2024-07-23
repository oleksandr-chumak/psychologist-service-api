package com.service.psychologists.appointments.validators;

import com.service.psychologists.core.repositories.enums.EqualityOperator;
import com.service.psychologists.core.repositories.models.SearchParameterRule;
import com.service.psychologists.core.repositories.models.SearchParameterValidator;

import java.util.Date;

public class AppointmentSearchParameterValidator extends SearchParameterValidator {

    public AppointmentSearchParameterValidator() {
        super(new SearchParameterRule[]{
                new SearchParameterRule(
                        "startTime",
                        new EqualityOperator[]{
                                EqualityOperator.GE,
                                EqualityOperator.LE,
                                EqualityOperator.GT,
                                EqualityOperator.LT,
                        },
                        Date.class
                ),
                new SearchParameterRule(
                        "endTime",
                        new EqualityOperator[]{
                                EqualityOperator.GE,
                                EqualityOperator.LE,
                                EqualityOperator.GT,
                                EqualityOperator.LT,
                        },
                        Date.class
                )
        });
    }
}
