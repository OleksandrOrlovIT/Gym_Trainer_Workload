package ua.orlov.gymtrainerworkload.controller.advice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GlobalExceptionHandlerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(new TestController())
                .setControllerAdvice(new GlobalExceptionHandler())
                .setMessageConverters(new MappingJackson2HttpMessageConverter())
                .build();
    }

    @Test
    void handleNoSuchElementExceptionTest() throws Exception {
        MvcResult result = mockMvc.perform(get("/no-such-element")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertEquals("{\"message\":\"No such element\",\"status\":\"NOT_FOUND\"}", content);
    }

    @Test
    void handleEntityNotFoundExceptionTest() throws Exception {
        MvcResult result = mockMvc.perform(get("/entity-not-found")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertEquals("{\"message\":\"Entity Not Found Exception\",\"status\":\"NOT_FOUND\"}", content);
    }

    @Test
    void handleIllegalArgumentExceptionTest() throws Exception {
        MvcResult result = mockMvc.perform(get("/illegal-argument")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertEquals("{\"message\":\"An unexpected illegal argument was provided\",\"status\":\"BAD_REQUEST\"}", content);
    }

    @Test
    void handleRuntimeExceptionTest() throws Exception {
        MvcResult result = mockMvc.perform(get("/runtime-exception")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertEquals("{\"message\":\"An unexpected runtime error occurred.\",\"status\":\"INTERNAL_ERROR\"}", content);
    }

    @Test
    void handleGeneralExceptionTest() throws Exception {
        MvcResult result = mockMvc.perform(get("/general-exception")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertEquals("{\"message\":\"An unexpected error occurred.\",\"status\":\"GENERAL_ERROR\"}", content);
    }

    @Test
    void handleMethodArgumentTypeMismatchException() throws Exception {
        MvcResult result = mockMvc.perform(get("/method-argument-type-mismatch")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertEquals("{\"message\":\"Method parameter 'id': Failed to convert value of type 'null' to required type 'java.lang.Integer'; Type mismatch\",\"status\":\"BAD_REQUEST\"}", content);
    }

    @Test
    void handleMethodArgumentNotValid() {
        BindingResult bindingResult = mock(BindingResult.class);
        ObjectError error = new ObjectError("username", "username is required.");
        when(bindingResult.getAllErrors()).thenReturn(Collections.singletonList(error));

        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(null, bindingResult);

        GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

        ResponseEntity<?> responseEntity = globalExceptionHandler.handleMethodArgumentNotValid(exception);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("{errors=[username is required.], status=BAD_REQUEST}", responseEntity.getBody().toString());
    }

    @Test
    void handleHttpMessageNotReadableException() throws Exception {
        String invalidJson = "asdaksjdjashfivoqnva";

        MvcResult result = mockMvc.perform(post("/http-message-not-readable")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertEquals("{\"message\":\"JSON parse error: Unrecognized token 'asdaksjdjashfivoqnva': was expecting (JSON String, Number, Array, Object or token 'null', 'true' or 'false')\",\"status\":\"BAD_REQUEST\"}", content);
    }

    @Test
    void handleBusinessLogicException() throws Exception {
        MvcResult result = mockMvc.perform(get("/business-logic-exception")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertEquals("{\"message\":\"Business Logic\",\"status\":\"LOGIC_ERROR\"}", content);
    }
}
