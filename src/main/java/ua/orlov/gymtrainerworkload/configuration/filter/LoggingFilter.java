package ua.orlov.gymtrainerworkload.configuration.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Log4j2
public class LoggingFilter implements Filter {

    private static final String TRANSACTION_ID_HEADER = "X-Transaction-Id";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String transactionId = httpRequest.getHeader(TRANSACTION_ID_HEADER);
        log.info("Incoming request - TransactionId: {}, Method: {}, URI: {}",
                transactionId, httpRequest.getMethod(), httpRequest.getRequestURI());

        chain.doFilter(request, response);

        log.info("Outgoing response - TransactionId: {}, Status: {}",
                transactionId, httpResponse.getStatus());
    }
}
