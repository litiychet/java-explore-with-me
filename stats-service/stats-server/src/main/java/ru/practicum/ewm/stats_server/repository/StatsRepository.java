package ru.practicum.ewm.stats_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.stats_dto.StatsDto;
import ru.practicum.ewm.stats_server.model.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<Event, Long> {
    @Query("select new ru.practicum.ewm.stats_dto.StatsDto(app, uri, count(*)) " +
            "from Event " +
            "where dt between :start and :end and uri in :uris " +
            "group by app, uri " +
            "order by count(*) desc")
    List<StatsDto> findStatsDtoByUris(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("select new ru.practicum.ewm.stats_dto.StatsDto(app, uri, count(distinct ip)) " +
            "from Event " +
            "where dt between :start and :end and uri in :uris " +
            "group by app, uri " +
            "order by count(distinct ip) desc")
    List<StatsDto> findUniqueStatsDtoByUris(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("select new ru.practicum.ewm.stats_dto.StatsDto(app, uri, count(*)) " +
            "from Event " +
            "where dt between :start and :end " +
            "group by app, uri " +
            "order by count(*) desc")
    List<StatsDto> findStatsDto(LocalDateTime start, LocalDateTime end);

    @Query("select new ru.practicum.ewm.stats_dto.StatsDto(app, uri, count(distinct ip)) " +
            "from Event " +
            "where dt between :start and :end " +
            "group by app, uri " +
            "order by count(distinct ip) desc")
    List<StatsDto> findUniqueStatsDto(LocalDateTime start, LocalDateTime end);
}