package ru.practicum.ewm.stats_client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.ewm.stats_client.client.BaseClient;
import ru.practicum.ewm.stats_dto.EventCreateDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class StatsClient extends BaseClient {
    private static final String HIT_PATH = "/hit";
    private static final String GET_STATS_PATH = "/stats";

    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    public StatsClient(@Value("${stats-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
            builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                .build()
        );
    }

    public ResponseEntity<Object> addEvent(String app, String uri, String ip) {
        EventCreateDto event = EventCreateDto.builder()
                .app(app)
                .uri(uri)
                .ip(ip)
                .timestamp(LocalDateTime.now())
                .build();

        return post(HIT_PATH, event);
    }

    public ResponseEntity<Object> getStats(LocalDateTime start, LocalDateTime end) {
        Map<String, Object> parameters = Map.of(
                "start", start.format(DATE_TIME_FORMAT),
                "end", end.format(DATE_TIME_FORMAT)
        );
        String path = new StringBuilder(GET_STATS_PATH).append("?start={start}&end={end}").toString();
        return get(path, parameters);
    }

    public ResponseEntity<Object> getStats(LocalDateTime start, LocalDateTime end, boolean unique) {
        Map<String, Object> parameters = Map.of(
                "start", start.format(DATE_TIME_FORMAT),
                "end", end.format(DATE_TIME_FORMAT),
                "unique", unique
        );
        String path = new StringBuilder(GET_STATS_PATH).append("?start={start}&end={end}&unique={unique}").toString();
        return get(path, parameters);
    }

    public ResponseEntity<Object> getStats(LocalDateTime start, LocalDateTime end, List<String> uris) {
        Map<String, Object> parameters = Map.of(
                "start", start.format(DATE_TIME_FORMAT),
                "end", end.format(DATE_TIME_FORMAT),
                "uris", uris.toArray()
        );
        String path = new StringBuilder(GET_STATS_PATH).append("?start={start}&end={end}&uris={uris}").toString();
        return get(path, parameters);
    }

    public ResponseEntity<Object> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        Map<String, Object> parameters = Map.of(
                "start", start.format(DATE_TIME_FORMAT),
                "end", end.format(DATE_TIME_FORMAT),
                "unique", unique,
                "uris", uris.toArray()
        );
        String path = new StringBuilder(GET_STATS_PATH).append("?start={start}&end={end}&uris={uris}&unique={unique}").toString();
        return get(path, parameters);
    }
}
