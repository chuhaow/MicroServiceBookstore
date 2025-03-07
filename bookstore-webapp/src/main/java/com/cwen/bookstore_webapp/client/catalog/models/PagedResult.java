package com.cwen.bookstore_webapp.client.catalog.models;

import java.util.List;

public record PagedResult<T> (
    List<T> data,
    long totalElements,
    int pageNumber,
    int totalPages,
    boolean isFirst,
    boolean isLast,
    boolean hasNext,
    boolean hasPrev
){

}
