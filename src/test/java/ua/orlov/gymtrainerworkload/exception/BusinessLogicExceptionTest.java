package ua.orlov.gymtrainerworkload.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BusinessLogicExceptionTest {

    @Test
    void testDefaultConstructor() {
        BusinessLogicException exception = new BusinessLogicException();
        assertEquals("A logic error occurred in the application.", exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testConstructorWithMessage() {
        String customMessage = "Custom logic error message.";
        BusinessLogicException exception = new BusinessLogicException(customMessage);
        assertEquals(customMessage, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testConstructorWithMessageAndCause() {
        String customMessage = "Custom logic error message.";
        Throwable cause = new RuntimeException("Cause of the error.");
        BusinessLogicException exception = new BusinessLogicException(customMessage, cause);
        assertEquals(customMessage, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void testConstructorWithCause() {
        Throwable cause = new RuntimeException("Cause of the error.");
        BusinessLogicException exception = new BusinessLogicException(cause);
        assertEquals("A logic error occurred in the application.", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void testConstructorWithNullMessage() {
        BusinessLogicException exception = new BusinessLogicException((String) null);
        assertEquals("A logic error occurred in the application.", exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testConstructorWithNullMessageAndCause() {
        Throwable cause = new RuntimeException("Cause of the error.");
        BusinessLogicException exception = new BusinessLogicException(null, cause);
        assertEquals("A logic error occurred in the application.", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}
