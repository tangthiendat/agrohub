package com.ttdat.productservice.infrastructure.utils;

@FunctionalInterface
public interface ValidationCallback <T>{
    boolean validate(T t);
}
