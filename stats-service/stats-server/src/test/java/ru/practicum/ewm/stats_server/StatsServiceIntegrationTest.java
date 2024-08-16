package ru.practicum.ewm.stats_server;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.ewm.stats_dto.EventCreateDto;
import ru.practicum.ewm.stats_dto.StatsDto;
import ru.practicum.ewm.stats_server.service.StatsService;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@Transactional
@SpringBootTest(
        properties = "jdbc.url=jdbc:h2:mem:shareit",
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class StatsServiceIntegrationTest {
    private final StatsService statsService;

    @Test
    public void addEventAndGetStats() {
        EventCreateDto event1 = EventCreateDto.builder()
                .app("test")
                .uri("/test1")
                .ip("127.0.0.1")
                .timestamp(LocalDateTime.now())
                .build();

        EventCreateDto event2 = EventCreateDto.builder()
                .app("test")
                .uri("/test1")
                .ip("127.0.0.2")
                .timestamp(LocalDateTime.now())
                .build();

        EventCreateDto event3 = EventCreateDto.builder()
                .app("test")
                .uri("/test2")
                .ip("127.0.0.1")
                .timestamp(LocalDateTime.now())
                .build();

        EventCreateDto event4 = EventCreateDto.builder()
                .app("test")
                .uri("/test2")
                .ip("127.0.0.2")
                .timestamp(LocalDateTime.now())
                .build();

        LocalDateTime start = LocalDateTime.of(2024, 1, 1, 0, 0, 0);
        LocalDateTime end = LocalDateTime.of(2024, 2, 1, 0, 0, 0);

        statsService.addEvent(event1);
        statsService.addEvent(event2);
        statsService.addEvent(event3);
        statsService.addEvent(event4);

        List<StatsDto> emptyStats = statsService.getStats(start, end, null, false);

        start = LocalDateTime.of(2024, 1, 1, 0, 0, 0);
        end = LocalDateTime.of(2024, 9, 1, 0, 0, 0);

        List<StatsDto> statsByUris = statsService.getStats(start, end, List.of("/test1"), false);

        List<StatsDto> allStats = statsService.getStats(start, end, null, false);

        List<StatsDto> uniqueStats = statsService.getStats(start, end, null, false);

        assertThat(emptyStats.isEmpty(), is(true));
        assertThat(statsByUris.get(0).getUri(), is("/test1"));
        assertThat(allStats.size(), is(2));
        assertThat(allStats.get(0).getUri(), is("/test1"));
        assertThat(allStats.get(1).getUri(), is("/test2"));
        assertThat(uniqueStats.get(0).getHits(), is(2L));
        assertThat(uniqueStats.get(1).getHits(), is(2L));
    }
}