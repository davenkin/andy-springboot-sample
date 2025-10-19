package com.company.andy.common.util;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static lombok.AccessLevel.PROTECTED;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.fromOptionalString;
import static org.springframework.data.domain.Sort.Order.asc;
import static org.springframework.data.domain.Sort.by;
import static org.springframework.data.domain.Sort.unsorted;

@Getter
@SuperBuilder
@EqualsAndHashCode
@NoArgsConstructor(access = PROTECTED)
public abstract class PageQuery {
    private static final String PROPERTY_DIRECTION_DELIMITER = ",";
    private static final int MAX_SORT_PROPERTIES = 3;
    private static final int DEFAULT_PAGE_SIZE = 25;
    private static final int DEFAULT_PAGE_NUMBER = 0;

    @Schema(description = "Zero-based page index (0..N).", defaultValue = "0")
    @Min(0)
    @Max(value = 10000)
    private int pageNumber;

    @Schema(description = "The size of the page to be returned, default is 25.", defaultValue = "25")
    @Min(0)
    @Max(value = 1000)
    private int pageSize;

    @Schema(description = "Array of sorting criteria. Format for each criteria: property,(asc|desc). Default sort order is ascending.", defaultValue = "[]")
    private List<String> pageSort;

    public Pageable pageable() {
        int pageNumber = this.pageNumber > 0 ? this.pageNumber : DEFAULT_PAGE_NUMBER;
        int pageSize = this.pageSize > 0 ? this.pageSize : DEFAULT_PAGE_SIZE;
        return PageRequest.of(pageNumber, pageSize, this.calculateSort());
    }

    private Sort calculateSort() {
        if (isEmpty(this.pageSort)) {
            return unsorted();
        }

        List<Order> orders = pageSort.stream()
                .filter(StringUtils::isNotBlank)
                .map(it -> {
                    String[] split = it.trim().split(PROPERTY_DIRECTION_DELIMITER);
                    String property = split[0].trim();
                    if (isBlank(property)) {
                        return null;
                    }

                    if (split.length == 1) {
                        return asc(property);
                    }

                    String directionStr = split[1].trim();
                    Sort.Direction direction = fromOptionalString(directionStr).orElse(ASC);
                    return new Order(direction, property);
                })
                .filter(Objects::nonNull)
                .collect(groupingBy(
                        Order::getProperty,
                        LinkedHashMap::new,   // preserves order of first occurrence
                        toList()
                )).values().stream()
                .map(theOrders -> theOrders.get(0))
                .limit(MAX_SORT_PROPERTIES)
                .toList();

        return isNotEmpty(orders) ? by(orders) : unsorted();
    }
}
