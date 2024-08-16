package ru.practicum.ewm.stats_server;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import ru.practicum.ewm.stats_dto.EventCreateDto;

import java.time.LocalDateTime;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class EventCreateDtoValidationTest {
    @Mock
    static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    public void validateAppNotBlank() {
        EventCreateDto event = EventCreateDto.builder()
                .app("")
                .uri("/test")
                .ip("127.0.0.1")
                .timestamp(LocalDateTime.now())
                .build();

        Set<ConstraintViolation<EventCreateDto>> constraintViolations = validator.validate(event);
        for (ConstraintViolation<EventCreateDto> c : constraintViolations) {
            assertThat(c.getMessage(), is("must not be blank"));
        }
    }

    @Test
    public void validateAppMaxSize() {
        EventCreateDto event = EventCreateDto.builder()
                .app(new String(new char[65]).replace('\0', '1'))
                .uri("/test")
                .ip("127.0.0.1")
                .timestamp(LocalDateTime.now())
                .build();

        Set<ConstraintViolation<EventCreateDto>> constraintViolations = validator.validate(event);
        for (ConstraintViolation<EventCreateDto> c : constraintViolations) {
            assertThat(c.getMessage(), is("size must be between 0 and 64"));
        }
    }

    @Test
    public void validateUriNotBlack() {
        EventCreateDto event = EventCreateDto.builder()
                .app("test")
                .uri("")
                .ip("127.0.0.1")
                .timestamp(LocalDateTime.now())
                .build();

        Set<ConstraintViolation<EventCreateDto>> constraintViolations = validator.validate(event);
        for (ConstraintViolation<EventCreateDto> c : constraintViolations) {
            assertThat(c.getMessage(), is("must not be blank"));
        }
    }

    @Test
    public void validateUriMaxSize() {
        EventCreateDto event = EventCreateDto.builder()
                .app("test")
                .uri(new String(new char[65]).replace('\0', '1'))
                .ip("127.0.0.1")
                .timestamp(LocalDateTime.now())
                .build();

        Set<ConstraintViolation<EventCreateDto>> constraintViolations = validator.validate(event);
        for (ConstraintViolation<EventCreateDto> c : constraintViolations) {
            assertThat(c.getMessage(), is("size must be between 0 and 64"));
        }
    }

    @Test
    public void validateIpNotBlack() {
        EventCreateDto event = EventCreateDto.builder()
                .app("test")
                .uri("/test")
                .ip("")
                .timestamp(LocalDateTime.now())
                .build();

        Set<ConstraintViolation<EventCreateDto>> constraintViolations = validator.validate(event);
        for (ConstraintViolation<EventCreateDto> c : constraintViolations) {
            assertThat(c.getMessage(), is("must not be blank"));
        }
    }

    @Test
    public void validateIpMaxSize() {
        EventCreateDto event = EventCreateDto.builder()
                .app("test")
                .uri("/test")
                .ip(new String(new char[16]).replace('\0', '1'))
                .timestamp(LocalDateTime.now())
                .build();

        Set<ConstraintViolation<EventCreateDto>> constraintViolations = validator.validate(event);
        for (ConstraintViolation<EventCreateDto> c : constraintViolations) {
            assertThat(c.getMessage(), is("size must be between 0 and 15"));
        }
    }

    @Test
    public void validateDtNotNull() {
        EventCreateDto event = EventCreateDto.builder()
                .app("test")
                .uri("/test")
                .ip("127.0.0.1")
                .timestamp(null)
                .build();

        Set<ConstraintViolation<EventCreateDto>> constraintViolations = validator.validate(event);
        for (ConstraintViolation<EventCreateDto> c : constraintViolations) {
            assertThat(c.getMessage(), is("must not be null"));
        }
    }
}
