package ru.practicum.ewm_main_service.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestControllerAdvice(basePackages = "ru.practicum.ewm_main_service")
@Slf4j
public class ErrorHandler {
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @ExceptionHandler
    protected ResponseEntity<ApiError> handleBadRequestException(BadRequestException e) {
        log.error("Запрос составлен некорректно.");
        return new ResponseEntity<>(ApiError.builder()
                .status("BAD_REQUEST")
                .reason("Incorrectly made request.")
                .message(e.getMessage())
                .timestamp(dtf.format(LocalDateTime.now()))
                .build(),
                HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler
    protected ResponseEntity<ApiError> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        if (Objects.requireNonNull(e.getRootCause()).toString().contains("categories")) {
            log.error("Существуют события, связанные с категорией");
            return new ResponseEntity<>(ApiError.builder()
                    .status("CONFLICT")
                    .reason("For the requested operation the conditions are not met.")
                    .message("The category is not empty.")
                    .timestamp(dtf.format(LocalDateTime.now()))
                    .build(),
                    HttpStatus.CONFLICT);
        } else {
            return new ResponseEntity<>(ApiError.builder()
                    .status("CONFLICT")
                    .reason("Integrity constraint has been violated.")
                    .message(e.getLocalizedMessage())
                    .timestamp(dtf.format(LocalDateTime.now()))
                    .build(),
                    HttpStatus.CONFLICT);
        }
    }

    @ExceptionHandler
    protected ResponseEntity<ApiError> handleConstraintViolationException(ConstraintViolationException e) {
        log.error("Запрос составлен некорректно.");
        return new ResponseEntity<>(ApiError.builder()
                .status("BAD_REQUEST")
                .reason("Incorrectly made request.")
                .message(e.getLocalizedMessage())
                .timestamp(dtf.format(LocalDateTime.now()))
                .build(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    protected ResponseEntity<ApiError> handleMissingServletRequestParameterException(
            MissingServletRequestParameterException e) {
        log.error("Запрос составлен некорректно.");
        return new ResponseEntity<>(ApiError.builder()
                .status("BAD_REQUEST")
                .reason("Incorrectly made request.")
                .message(e.getLocalizedMessage())
                .timestamp(dtf.format(LocalDateTime.now()))
                .build(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    protected ResponseEntity<ApiError> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<String> errors = new ArrayList<>();
        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            errors.add("Field: " + error.getField()
                    + ". Error: " + error.getDefaultMessage()
                    + ". Value: " + error.getRejectedValue());
        }
        for (ObjectError error : e.getBindingResult().getGlobalErrors()) {
            errors.add("Field: " + error.getObjectName()
                    + ". Error: " + error.getDefaultMessage()
                    + ". Value: " + error.getObjectName());
        }
        log.error("Ошибка валидации в следующих полях: {}", errors);
        return new ResponseEntity<>(ApiError.builder()
                .status("BAD_REQUEST")
                .reason("Incorrectly made request.")
                .message(errors.toString())
                .timestamp(dtf.format(LocalDateTime.now()))
                .build(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    protected ResponseEntity<ApiError> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return new ResponseEntity<>(ApiError.builder()
                .status("CONFLICT")
                .reason("Malformed JSON Request")
                .message(e.getMessage())
                .timestamp(dtf.format(LocalDateTime.now()))
                .build(),
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> handleNotFoundException(NotFoundException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(ApiError.builder()
                .status("NOT_FOUND")
                .reason("The required object was not found.")
                .message(String.format(e.getEntityName() + " with id= %s  was not found", e.getId()))
                .timestamp(dtf.format(LocalDateTime.now()))
                .build(),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e) {
        return new ResponseEntity<>(
                String.format("Данный медиа тип (= %s) не поддерживается. Тип должен соответствовать application/json.",
                        e.getContentType()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> handleForbiddenException(final ForbiddenException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(ApiError.builder()
                .status("FORBIDDEN")
                .reason("For the requested operation the conditions are not met.")
                .message(e.getMessage())
                .timestamp(dtf.format(LocalDateTime.now()))
                .build(),
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleThrowable(final Throwable e) {
        e.printStackTrace();
        return new ErrorResponse(
                "Произошла непредвиденная ошибка"
        );
    }
}
