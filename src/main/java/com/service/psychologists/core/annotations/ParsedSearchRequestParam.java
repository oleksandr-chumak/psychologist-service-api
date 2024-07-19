package com.service.psychologists.core.annotations;

import com.service.psychologists.core.repositories.SearchParameterValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ParsedSearchRequestParam {
    String name();
    Class<? extends SearchParameterValidator> validator();
}
