package ru.practicum.ewm.stats_server;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.practicum.ewm.stats_dto.EventCreateDto;
import ru.practicum.ewm.stats_dto.StatsDto;
import ru.practicum.ewm.stats_server.controller.StatsController;
import ru.practicum.ewm.stats_server.exception.IncorrectDataException;
import ru.practicum.ewm.stats_server.service.StatsService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = StatsController.class)
public class StatsControllerTest {
    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private StatsService statsService;

    @Autowired
    private MockMvc mvc;

    @Test
    public void addEventSuccess() throws Exception {
        EventCreateDto event = EventCreateDto.builder()
                .app("test")
                .uri("/test")
                .ip("127.0.0.1")
                .timestamp(LocalDateTime.now())
                .build();


        mvc.perform(post("/hit")
                        .content(mapper.writeValueAsString(event))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(201));
    }

    @Test
    public void addEventWithBlankApp() throws Exception {
        EventCreateDto event = EventCreateDto.builder()
                .app("")
                .uri("/test")
                .ip("127.0.0.1")
                .timestamp(LocalDateTime.now())
                .build();


        mvc.perform(post("/hit")
                        .content(mapper.writeValueAsString(event))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400))
                .andExpect(
                        result -> assertThat(
                                result.getResolvedException() instanceof MethodArgumentNotValidException, is(true)
                        )
                );
    }

    @Test
    public void addEventWithMaxSizeApp() throws Exception {
        EventCreateDto event = EventCreateDto.builder()
                .app(new String(new char[65]).replace('\0', '1'))
                .uri("/test")
                .ip("127.0.0.1")
                .timestamp(LocalDateTime.now())
                .build();


        mvc.perform(post("/hit")
                        .content(mapper.writeValueAsString(event))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400))
                .andExpect(
                        result -> assertThat(
                                result.getResolvedException() instanceof MethodArgumentNotValidException, is(true)
                        )
                );
    }

    @Test
    public void addEventWithBlankUri() throws Exception {
        EventCreateDto event = EventCreateDto.builder()
                .app("test")
                .uri("")
                .ip("127.0.0.1")
                .timestamp(LocalDateTime.now())
                .build();


        mvc.perform(post("/hit")
                        .content(mapper.writeValueAsString(event))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400))
                .andExpect(
                        result -> assertThat(
                                result.getResolvedException() instanceof MethodArgumentNotValidException, is(true)
                        )
                );
    }

    @Test
    public void addEventWithMaxSizeUri() throws Exception {
        EventCreateDto event = EventCreateDto.builder()
                .app("test")
                .uri(new String(new char[65]).replace('\0', '1'))
                .ip("127.0.0.1")
                .timestamp(LocalDateTime.now())
                .build();


        mvc.perform(post("/hit")
                        .content(mapper.writeValueAsString(event))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400))
                .andExpect(
                        result -> assertThat(
                                result.getResolvedException() instanceof MethodArgumentNotValidException, is(true)
                        )
                );
    }

    @Test
    public void addEventWithBlankIp() throws Exception {
        EventCreateDto event = EventCreateDto.builder()
                .app("test")
                .uri("/test")
                .ip("")
                .timestamp(LocalDateTime.now())
                .build();


        mvc.perform(post("/hit")
                        .content(mapper.writeValueAsString(event))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400))
                .andExpect(
                        result -> assertThat(
                                result.getResolvedException() instanceof MethodArgumentNotValidException, is(true)
                        )
                );
    }

    @Test
    public void addEventWithMaxSizeIp() throws Exception {
        EventCreateDto event = EventCreateDto.builder()
                .app("test")
                .uri("/test")
                .ip(new String(new char[16]).replace('\0', '1'))
                .timestamp(LocalDateTime.now())
                .build();


        mvc.perform(post("/hit")
                        .content(mapper.writeValueAsString(event))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400))
                .andExpect(
                        result -> assertThat(
                                result.getResolvedException() instanceof MethodArgumentNotValidException, is(true)
                        )
                );
    }

    @Test
    public void addEventWithDtNull() throws Exception {
        EventCreateDto event = EventCreateDto.builder()
                .app("test")
                .uri("/test")
                .ip("127.0.0.1")
                .timestamp(null)
                .build();


        mvc.perform(post("/hit")
                        .content(mapper.writeValueAsString(event))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400))
                .andExpect(
                        result -> assertThat(
                                result.getResolvedException() instanceof MethodArgumentNotValidException, is(true)
                        )
                );
    }

    @Test
    public void getStatsSuccess() throws Exception {
        StatsDto stats1 = StatsDto.builder()
                .app("test")
                .uri("/test")
                .hits(5L)
                .build();

        StatsDto stats2 = StatsDto.builder()
                .app("test")
                .uri("/test1")
                .hits(2L)
                .build();

        when(statsService.getStats(any(), any(), anyList(), anyBoolean())).thenReturn(List.of(stats1, stats2));


        mvc.perform(get("/stats?start={start}&end={end}&uris={uris}&uris={uris}", LocalDateTime.now(), LocalDateTime.now(), "/test", "/test1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$[0].app", is(stats1.getApp()), String.class))
                .andExpect(jsonPath("$[0].uri", is(stats1.getUri()), String.class))
                .andExpect(jsonPath("$[0].hits", is(stats1.getHits()), Long.class))
                .andExpect(jsonPath("$[1].app", is(stats2.getApp()), String.class))
                .andExpect(jsonPath("$[1].uri", is(stats2.getUri()), String.class))
                .andExpect(jsonPath("$[1].hits", is(stats2.getHits()), Long.class));
    }

    @Test
    public void getStatsIncorrectDate() throws Exception {
        when(statsService.getStats(any(), any(), anyList(), anyBoolean())).thenThrow(new IncorrectDataException("test exception"));


        mvc.perform(get("/stats?start={start}&end={end}&uris={uris}&uris={uris}", LocalDateTime.now(), LocalDateTime.now(), "/test", "/test1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400))
                .andExpect(
                        result -> assertThat(
                                result.getResolvedException() instanceof IncorrectDataException, is(true)
                        )
                );
    }
}