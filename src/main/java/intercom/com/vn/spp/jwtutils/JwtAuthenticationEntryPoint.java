package intercom.com.vn.spp.jwtutils;

import java.io.IOException;
import java.io.Serializable;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint,
        Serializable { 

    @Override
    public void commence(jakarta.servlet.http.HttpServletRequest request,
            jakarta.servlet.http.HttpServletResponse response, AuthenticationException authException)
            throws IOException, jakarta.servlet.ServletException {
         
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                "Unauthorized");
    }
}