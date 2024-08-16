package ru.practicum.ewm.stats_server.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.stats_dto.EventCreateDto;
import ru.practicum.ewm.stats_dto.StatsDto;
import ru.practicum.ewm.stats_server.service.StatsService;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class StatsController {
    private final StatsService statsService;

    @PostMapping("/hit")
    public ResponseEntity addEvent(@Validated @RequestBody EventCreateDto event) {
        log.info("POST /hit with event: {}", event);
        statsService.addEvent(event);
        return ResponseEntity.status(HttpStatusCode.valueOf(201)).build();
    }

    @GetMapping("/stats")
    public ResponseEntity<List<StatsDto>> getStats(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
            @RequestParam(required = false) List<String> uris,
            @RequestParam(defaultValue = "false") boolean unique
            ) {
        log.info("GET /stats?start={}&end={}&uris={}&unique={}", start, end, uris, unique);
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(
                statsService.getStats(start, end, uris, unique)
        );
    }
}
