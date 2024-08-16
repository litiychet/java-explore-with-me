package ru.practicum.ewm.stats_server.service;

import ru.practicum.ewm.stats_dto.EventCreateDto;
import ru.practicum.ewm.stats_dto.StatsDto;
import ru.practicum.ewm.stats_server.model.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    public Event addEvent(EventCreateDto event);

    public List<StatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
}
