package com.jazztech.exceptionhandler;

import com.jazztech.exception.AnalysisApiConnectionException;
import com.jazztech.exception.AnalysisNotFoundException;
import com.jazztech.exception.CardHolderAlreadyExistsException;
import com.jazztech.exception.CardHolderNotFoundException;
import com.jazztech.exception.CardNotFoundException;
import com.jazztech.exception.CustomIllegalArgumentException;
import com.jazztech.exception.InactiveCardHolderException;
import com.jazztech.exception.InsufficientLimitException;
import com.jazztech.exception.InvalidLimitException;
import com.jazztech.exception.InvalidStatusException;
import com.jazztech.utils.CardHolderStatus;
import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class ExceptionResolver {

    private static final String TIMESTAMP = "timestamp";
    private static final String UNPROCESSABLE_ENTITY = "https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/422";

    @ExceptionHandler(InvalidStatusException.class)
    public ProblemDetail invalidStatusExceptionHandler(InvalidStatusException exception, HttpServletRequest request) {
        final ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);
        problemDetail.setType(URI.create(UNPROCESSABLE_ENTITY));
        problemDetail.setTitle("Invalid status");
        problemDetail.setProperty(TIMESTAMP, LocalDateTime.now());
        problemDetail.setDetail(exception.getMessage());
        problemDetail.setInstance(URI.create(request.getRequestURL().toString()));
        return problemDetail;
    }

    @ExceptionHandler(AnalysisNotFoundException.class)
    public ProblemDetail analysisNotFoundExceptionHandler(AnalysisNotFoundException exception, HttpServletRequest request) {
        final ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problemDetail.setType(URI.create(UNPROCESSABLE_ENTITY));
        problemDetail.setTitle("Analysis not found");
        problemDetail.setProperty(TIMESTAMP, LocalDateTime.now());
        problemDetail.setDetail(exception.getMessage());
        problemDetail.setInstance(URI.create(request.getRequestURL().toString()));
        return problemDetail;
    }

    @ExceptionHandler(AnalysisApiConnectionException.class)
    public ProblemDetail analysisApiConnectionExceptionHandler(AnalysisApiConnectionException exception, HttpServletRequest request) {
        final ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.SERVICE_UNAVAILABLE);
        problemDetail.setType(URI.create(UNPROCESSABLE_ENTITY));
        problemDetail.setTitle("Analysis API connection error");
        problemDetail.setProperty(TIMESTAMP, LocalDateTime.now());
        problemDetail.setDetail(exception.getMessage());
        problemDetail.setInstance(URI.create(request.getRequestURL().toString()));
        return problemDetail;
    }

    @ExceptionHandler(CardHolderNotFoundException.class)
    public ProblemDetail cardHolderNotFoundExceptionHandler(CardHolderNotFoundException exception, HttpServletRequest request) {
        final ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problemDetail.setType(URI.create("https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/404"));
        problemDetail.setTitle("Card holder not found");
        problemDetail.setProperty(TIMESTAMP, LocalDateTime.now());
        problemDetail.setDetail(exception.getMessage());
        problemDetail.setInstance(URI.create(request.getRequestURL().toString()));
        return problemDetail;
    }

    @ExceptionHandler(CustomIllegalArgumentException.class)
    public ProblemDetail customIllegalArgumentExceptionHandler(CustomIllegalArgumentException exception, HttpServletRequest request) {
        final ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setType(URI.create(UNPROCESSABLE_ENTITY));
        problemDetail.setTitle("Some parameters are invalid");
        problemDetail.setProperty(TIMESTAMP, LocalDateTime.now());
        problemDetail.setDetail(exception.getMessage());
        problemDetail.setInstance(URI.create(request.getRequestURL().toString()));
        return problemDetail;
    }

    @ExceptionHandler(CardHolderAlreadyExistsException.class)
    public ProblemDetail cardHolderAlreadyExistsExceptionHandler(CardHolderAlreadyExistsException exception, HttpServletRequest request) {
        final ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);
        problemDetail.setType(URI.create(UNPROCESSABLE_ENTITY));
        problemDetail.setTitle("Card holder already exists");
        problemDetail.setProperty(TIMESTAMP, LocalDateTime.now());
        problemDetail.setDetail(exception.getMessage());
        problemDetail.setInstance(URI.create(request.getRequestURL().toString()));
        return problemDetail;
    }

    @ExceptionHandler(InsufficientLimitException.class)
    public ProblemDetail insufficientLimitExceptionHandler(InsufficientLimitException exception, HttpServletRequest request) {
        final ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);
        problemDetail.setType(URI.create(UNPROCESSABLE_ENTITY));
        problemDetail.setTitle("Insufficient account limit");
        problemDetail.setProperty(TIMESTAMP, LocalDateTime.now());
        problemDetail.setDetail(exception.getMessage());
        problemDetail.setInstance(URI.create(request.getRequestURL().toString()));
        return problemDetail;
    }

    @ExceptionHandler(InactiveCardHolderException.class)
    public ProblemDetail inactiveCardHolderExceptionHandler(InactiveCardHolderException exception, HttpServletRequest request) {
        final ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);
        problemDetail.setType(URI.create(UNPROCESSABLE_ENTITY));
        problemDetail.setTitle("Inactive card holder");
        problemDetail.setProperty(TIMESTAMP, LocalDateTime.now());
        problemDetail.setDetail(exception.getMessage());
        problemDetail.setInstance(URI.create(request.getRequestURL().toString()));
        return problemDetail;
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ProblemDetail methodArgumentTypeMismatchExceptionHandler(MethodArgumentTypeMismatchException exception, HttpServletRequest request) {
        final ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);
        problemDetail.setType(URI.create("https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/422"));
        problemDetail.setTitle("Invalid parameter type");
        problemDetail.setProperty(TIMESTAMP, LocalDateTime.now());

        final List<CardHolderStatus> acceptedValues = Arrays.stream(CardHolderStatus.values()).toList();
        final String paramName = exception.getParameter().getParameterName();
        final String invalidValue = exception.getValue().toString();
        final String errorMessage = "Invalid parameter value for " + paramName
                + ". Expected type: " + acceptedValues
                + ". Invalid value: " + invalidValue;

        problemDetail.setDetail(errorMessage);
        problemDetail.setInstance(URI.create(request.getRequestURL().toString()));
        return problemDetail;
    }

    @ExceptionHandler(CardNotFoundException.class)
    public ProblemDetail cardNotFoundExceptionHandler(CardNotFoundException exception, HttpServletRequest request) {
        final ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problemDetail.setType(URI.create("https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/404"));
        problemDetail.setTitle("Card not found");
        problemDetail.setProperty(TIMESTAMP, LocalDateTime.now());
        problemDetail.setDetail(exception.getMessage());
        problemDetail.setInstance(URI.create(request.getRequestURL().toString()));
        return problemDetail;
    }

    @ExceptionHandler(InvalidLimitException.class)
    public ProblemDetail invalidLimitExceptionHandler(InvalidLimitException exception, HttpServletRequest request) {
        final ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);
        problemDetail.setType(URI.create(UNPROCESSABLE_ENTITY));
        problemDetail.setTitle("Invalid limit");
        problemDetail.setProperty(TIMESTAMP, LocalDateTime.now());
        problemDetail.setDetail(exception.getMessage());
        problemDetail.setInstance(URI.create(request.getRequestURL().toString()));

        return problemDetail;
    }
}
