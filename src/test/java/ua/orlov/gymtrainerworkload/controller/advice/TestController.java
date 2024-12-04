package ua.orlov.gymtrainerworkload.controller.advice;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpMethod;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import ua.orlov.gymtrainerworkload.dto.TrainerWorkload;
import ua.orlov.gymtrainerworkload.exception.BusinessLogicException;

import java.util.NoSuchElementException;

@RestController
public class TestController {

    @GetMapping("/no-such-element")
    public void noSuchElement() {
        throw new NoSuchElementException("No such element");
    }

    @GetMapping("/entity-not-found")
    public void entityNotFound() {
        throw new EntityNotFoundException("Entity Not Found Exception");
    }

    @GetMapping("/illegal-argument")
    public void illegalArgument() {
        throw new IllegalArgumentException("Illegal argument Exception");
    }

    @GetMapping("/runtime-exception")
    public void runTimeException() {
        throw new RuntimeException("Runtime Exception");
    }

    @GetMapping("/general-exception")
    public void generalException() throws Exception {
        throw new Exception("Exception");
    }

    @GetMapping("/method-argument-type-mismatch")
    public void methodArgumentTypeMismatchException() {
        throw new MethodArgumentTypeMismatchException(
                null, Integer.class, "id", null, new IllegalArgumentException("Type mismatch"));
    }

    @GetMapping("/no-resource-found")
    public void noResourceFound() throws NoResourceFoundException {
        throw new NoResourceFoundException(HttpMethod.GET, "/path");
    }

    @PostMapping("/http-message-not-readable")
    public void messageNotReadable(@RequestBody @Validated TrainerWorkload trainerWorkload) {

    }

    @GetMapping("/business-logic-exception")
    public void businessLogicException() {
        throw new BusinessLogicException("Business Logic");
    }

}
