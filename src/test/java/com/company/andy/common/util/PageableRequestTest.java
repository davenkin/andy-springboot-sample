package com.company.andy.common.util;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;

class PageableRequestTest {

    @Test
    void should_get_pageable_for_empty_pagination_fields() {
        TestPageableRequest request = TestPageableRequest.builder().build();
        Pageable pageable = request.pageable();
        assertEquals(0, pageable.getPageNumber());
        assertEquals(25, pageable.getPageSize());
        assertEquals(Sort.unsorted(), pageable.getSort());
    }

    @Test
    void should_get_pageable_for_normal_request() {
        TestPageableRequest request = TestPageableRequest.builder().pageNumber(1).pageSize(30).pageSort(List.of("field1,ASC", "field2,DESC")).build();

        Pageable pageable = request.pageable();
        assertEquals(1, pageable.getPageNumber());
        assertEquals(30, pageable.getPageSize());
        Sort.Order field1Order = pageable.getSort().getOrderFor("field1");
        assertEquals(ASC, field1Order.getDirection());

        Sort.Order field2Order = pageable.getSort().getOrderFor("field2");
        assertEquals(DESC, field2Order.getDirection());
    }

    @Test
    void should_get_pageable_for_at_most_3_sorts() {
        TestPageableRequest request = TestPageableRequest.builder().pageNumber(1).pageSize(30)
                .pageSort(List.of("field1,ASC", "field2,DESC", "field3,DESC", "field4,DESC")).build();

        Pageable pageable = request.pageable();

        Sort.Order field3Order = pageable.getSort().getOrderFor("field3");
        assertEquals(DESC, field3Order.getDirection());
        assertNull(pageable.getSort().getOrderFor("field4"));
    }

    @Test
    void should_get_pageable_with_default_sort_direction_of_asc() {
        TestPageableRequest request = TestPageableRequest.builder().pageNumber(1).pageSize(30).pageSort(List.of("field1")).build();

        Pageable pageable = request.pageable();

        Sort.Order field1Order = pageable.getSort().getOrderFor("field1");
        assertEquals(ASC, field1Order.getDirection());
    }

    @Test
    void should_get_pageable_with_default_sort_direction_of_asc_for_wrong_direction_name() {
        TestPageableRequest request = TestPageableRequest.builder().pageNumber(1).pageSize(30).pageSort(List.of("field1,wrongdirection")).build();

        Pageable pageable = request.pageable();

        Sort.Order field1Order = pageable.getSort().getOrderFor("field1");
        assertEquals(ASC, field1Order.getDirection());
    }

    @Test
    void should_get_pageable_with_sortable_property_missing() {
        TestPageableRequest request = TestPageableRequest.builder().pageNumber(1).pageSize(30).pageSort(List.of(",asc")).build();

        Pageable pageable = request.pageable();

        assertEquals(Sort.unsorted(), pageable.getSort());
    }


    @Test
    void should_get_pageable_with_sortable_element_empty() {
        TestPageableRequest request = TestPageableRequest.builder().pageNumber(1).pageSize(30).pageSort(List.of("")).build();

        Pageable pageable = request.pageable();

        assertEquals(Sort.unsorted(), pageable.getSort());
    }

    @Test
    void should_deduplicate_same_property_and_keep_the_first() {
        TestPageableRequest request = TestPageableRequest.builder().pageNumber(1).pageSize(30)
                .pageSort(List.of("field1,ASC", "field1,DESC")).build();

        Pageable pageable = request.pageable();

        List<Sort.Order> orders = pageable.getSort().stream().toList();
        assertEquals(1, orders.size());
        assertEquals(ASC, orders.get(0).getDirection());
    }

    @Getter
    @SuperBuilder
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor(access = PRIVATE)
    static class TestPageableRequest extends PageableRequest {
    }
}