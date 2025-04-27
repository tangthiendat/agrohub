package com.ttdat.core.api.dto.response;

import com.ttdat.core.domain.entities.TrendType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StatsCardValue {
    BigDecimal value;

    BigDecimal changePercentage;

    TrendType trend;
}
