package ru.practicum.ewm.stats_server.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.stats_dto.EventCreateDto;
import ru.practicum.ewm.stats_dto.StatsDto;
import ru.practicum.ewm.stats_server.exception.IncorrectDataException;
import ru.practicum.ewm.stats_server.model.Event;
import ru.practicum.ewm.stats_server.mapper.EventMapper;
import ru.practicum.ewm.stats_server.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;

    @Override
    @Transactional
    public Event addEvent(EventCreateDto event) {
        Event createEvent = EventMapper.eventCreateDtoToEvent(event);
        return statsRepository.save(createEvent);
    }

    @Override
    public List<StatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        if (start.isAfter(end) || start.equals(end))
            throw new IncorrectDataException("Некорректная дата поиска статистики");

        if (uris == null) {
            if (unique)
                return statsRepository.findUniqueStatsDto(start, end);

            return statsRepository.findStatsDto(start, end);
        }

        if (unique)
            return statsRepository.findUniqueStatsDtoByUris(start, end, uris);

        return statsRepository.findStatsDtoByUris(start, end, uris);
    }
}
