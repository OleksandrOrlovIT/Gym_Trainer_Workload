package ua.orlov.gymtrainerworkload.configuration.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoggingFilterTest {

    @InjectMocks
    private LoggingFilter loggingFilter;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Test
    public void testLoggingFilter() throws ServletException, IOException {
        String transactionId = "12345";
        String requestMethod = "POST";
        String requestUri = "/api/v1/trainer/create";
        int responseStatus = 200;

        when(request.getHeader("X-Transaction-Id")).thenReturn(transactionId);
        when(request.getMethod()).thenReturn(requestMethod);
        when(request.getRequestURI()).thenReturn(requestUri);
        when(response.getStatus()).thenReturn(responseStatus);

        loggingFilter.doFilter(request, response, filterChain);

        doNothing().when(filterChain).doFilter(any(ServletRequest.class), any(ServletResponse.class));

        loggingFilter.doFilter(request, response, filterChain);

        verify(request, times(2)).getHeader(any());
        verify(request, times(2)).getMethod();
        verify(request, times(2)).getRequestURI();
        verify(response, times(2)).getStatus();
    }
}
