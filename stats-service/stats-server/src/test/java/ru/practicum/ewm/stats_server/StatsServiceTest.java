package ru.practicum.ewm.stats_server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.ewm.stats_dto.EventCreateDto;
import ru.practicum.ewm.stats_dto.StatsDto;
import ru.practicum.ewm.stats_server.exception.IncorrectDataException;
import ru.practicum.ewm.stats_server.model.Event;
import ru.practicum.ewm.stats_server.repository.StatsRepository;
import ru.practicum.ewm.stats_server.service.StatsServiceImpl;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@ExtendWith(MockitoExtension.class)
public class StatsServiceTest {
    @Mock
    private StatsRepository statsRepository;

    @InjectMocks
    private StatsServiceImpl statsService;

    private EventCreateDto eventCreateDto;
    private Event event;
    private Event eventReturn;

    private StatsDto stats1;
    private StatsDto stats2;

    private StatsDto statsUnique1;
    private StatsDto statsUnique2;

    private LocalDateTime dt;

    @BeforeEach
    public void setUp() {
        statsRepository = Mockito.mock(StatsRepository.class);

        statsService = new StatsServiceImpl(statsRepository);

        dt = LocalDateTime.now();

        eventCreateDto = EventCreateDto.builder()
                .app("test")
                .uri("/test")
                .ip("127.0.0.1")
                .timestamp(dt)
                .build();

        event = Event.builder()
                .app("test")
                .uri("/test")
                .ip("127.0.0.1")
                .dt(dt)
                .build();

        eventReturn = Event.builder()
                .id(1L)
                .app("test")
                .uri("/test")
                .ip("127.0.0.1")
                .dt(dt)
                .build();

        stats1 = StatsDto.builder()
                .app("test")
                .uri("/test1")
                .hits(3L)
                .build();

        stats2 = StatsDto.builder()
                .app("test")
                .uri("/test2")
                .hits(2L)
                .build();

        statsUnique1 = StatsDto.builder()
                .app("test")
                .uri("/test1")
                .hits(1L)
                .build();

        statsUnique2 = StatsDto.builder()
                .app("test")
                .uri("/test2")
                .hits(1L)
                .build();
    }

    @Test
    public void addEventSuccess() {
        given(statsRepository.save(event)).willReturn(eventReturn);

        Event newEvent = statsService.addEvent(eventCreateDto);

        assertThat(newEvent.getId(), is(1L));
        assertThat(newEvent.getApp(), is("test"));
        assertThat(newEvent.getUri(), is("/test"));
        assertThat(newEvent.getDt(), is(dt));
    }

    @Test
    public void getStatWithStartAfterEnd() {
        assertThrowsExactly(IncorrectDataException.class, () -> statsService.getStats(
                LocalDateTime.now(), LocalDateTime.now().minus(3, ChronoUnit.DAYS), null, false
        ));
    }

    @Test
    public void getStatsWithStartEqualEnd() {
        assertThrowsExactly(IncorrectDataException.class, () -> statsService.getStats(
                dt, dt, null, false
        ));
    }

    @Test
    public void getStatsWithoutUrisUnique() {
        LocalDateTime start = LocalDateTime.of(2024, 1, 1, 0, 0, 0);
        LocalDateTime end = LocalDateTime.of(2024, 9, 1, 0, 0, 0);
        given(statsRepository.findUniqueStatsDto(any(), any())).willReturn(List.of(statsUnique1, statsUnique2));

        List<StatsDto> statsDtoList = statsService.getStats(start, end, null, true);

        assertThat(statsDtoList.size(), is(2));
        assertThat(statsDtoList.get(0).getUri(), is("/test1"));
        assertThat(statsDtoList.get(0).getHits(), is(1L));
        assertThat(statsDtoList.get(1).getUri(), is("/test2"));
        assertThat(statsDtoList.get(1).getHits(), is(1L));
    }

    @Test
    public void getStatsWithoutUris() {
        LocalDateTime start = LocalDateTime.of(2024, 1, 1, 0, 0, 0);
        LocalDateTime end = LocalDateTime.of(2024, 9, 1, 0, 0, 0);
        given(statsRepository.findStatsDto(any(), any())).willReturn(List.of(stats1, stats2));

        List<StatsDto> statsDtoList = statsService.getStats(start, end, null, false);

        assertThat(statsDtoList.size(), is(2));
        assertThat(statsDtoList.get(0).getUri(), is("/test1"));
        assertThat(statsDtoList.get(0).getHits(), is(3L));
        assertThat(statsDtoList.get(1).getUri(), is("/test2"));
        assertThat(statsDtoList.get(1).getHits(), is(2L));
    }

    @Test
    public void getStatsWithUrisUnique() {
        LocalDateTime start = LocalDateTime.of(2024, 1, 1, 0, 0, 0);
        LocalDateTime end = LocalDateTime.of(2024, 9, 1, 0, 0, 0);
        given(statsRepository.findUniqueStatsDtoByUris(any(), any(), anyList())).willReturn(List.of(statsUnique1));

        List<StatsDto> statsDtoList = statsService.getStats(start, end, List.of("/test1"), true);

        assertThat(statsDtoList.size(), is(1));
        assertThat(statsDtoList.get(0).getUri(), is("/test1"));
        assertThat(statsDtoList.get(0).getHits(), is(1L));
    }

    @Test
    public void getStatsWithUris() {
        LocalDateTime start = LocalDateTime.of(2024, 1, 1, 0, 0, 0);
        LocalDateTime end = LocalDateTime.of(2024, 9, 1, 0, 0, 0);
        given(statsRepository.findStatsDtoByUris(any(), any(), anyList())).willReturn(List.of(statsUnique2));

        List<StatsDto> statsDtoList = statsService.getStats(start, end, List.of("/test2"), false);

        assertThat(statsDtoList.size(), is(1));
        assertThat(statsDtoList.get(0).getUri(), is("/test2"));
        assertThat(statsDtoList.get(0).getHits(), is(1L));
    }
}
