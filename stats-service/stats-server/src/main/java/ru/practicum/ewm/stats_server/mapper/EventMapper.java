package ru.practicum.ewm.stats_server.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.stats_dto.EventCreateDto;
import ru.practicum.ewm.stats_server.model.Event;

@UtilityClass
public class EventMapper {
    public Event eventCreateDtoToEvent(EventCreateDto eventCreateDto) {
        return Event.builder()
                .app(eventCreateDto.getApp())
                .uri(eventCreateDto.getUri())
                .ip(eventCreateDto.getIp())
                .dt(eventCreateDto.getTimestamp())
                .build();
    }
}
