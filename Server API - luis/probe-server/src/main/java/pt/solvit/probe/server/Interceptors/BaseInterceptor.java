package pt.solvit.probe.server.Interceptors;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import pt.solvit.probe.server.controller.exception.ForbiddenException;
import pt.solvit.probe.server.controller.exception.UnauthorizedException;
import pt.solvit.probe.server.model.User;
import pt.solvit.probe.server.service.api.IAuthenticationService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Base64;

@Component
public class BaseInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        request.setAttribute("request_date", LocalDateTime.now());

        return true;
    }

}