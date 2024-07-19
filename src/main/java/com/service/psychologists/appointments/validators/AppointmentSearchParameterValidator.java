package com.service.psychologists.appointments.validators;

import com.service.psychologists.core.repositories.EqualityOperator;
import com.service.psychologists.core.repositories.SearchParameterRule;
import com.service.psychologists.core.repositories.SearchParameterValidator;

import java.util.Date;

public class AppointmentSearchParameterValidator extends SearchParameterValidator {

    public AppointmentSearchParameterValidator() {
        super(new SearchParameterRule[]{
                new SearchParameterRule(
                        "from",
                        new EqualityOperator[]{
                                EqualityOperator.GE,
                                EqualityOperator.LE,
                                EqualityOperator.GT,
                                EqualityOperator.LT,
                        },
                        Date.class
                ),
                new SearchParameterRule(
                        "to",
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
