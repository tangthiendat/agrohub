package com.ttdat.userservice.application.mappers;


import java.util.List;

/**
 * Interface for a generic EntityMapper
 * @param <D> DTO class
 * @param <E> Entity class
 */

public interface EntityMapper <D,E>{
    D toDTO(E entity);
    List<D> toDTOs(List<E> entities);
}
