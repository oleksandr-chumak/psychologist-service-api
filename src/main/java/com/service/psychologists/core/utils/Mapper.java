package com.service.psychologists.core.utils;

public interface Mapper<A,B> {

    B mapTo(A a);

    A mapFrom(B b);

}
