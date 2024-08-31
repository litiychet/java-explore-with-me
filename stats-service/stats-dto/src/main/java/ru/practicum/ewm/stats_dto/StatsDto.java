package ru.practicum.ewm.stats_dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class StatsDto {
    private String app;

    private String uri;

    private Long hits;
}
