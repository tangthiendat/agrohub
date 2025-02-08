package com.ttdat.core.infrastructure.utils;

@FunctionalInterface
public interface ValidationCallback <T>{
    boolean validate(T t);
}
