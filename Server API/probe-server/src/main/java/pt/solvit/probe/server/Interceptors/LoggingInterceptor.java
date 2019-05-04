package pt.solvit.probe.server.Interceptors;


import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import pt.solvit.probe.server.config.AppConfiguration;
import pt.solvit.probe.server.controller.exception.UnauthorizedException;
import pt.solvit.probe.server.controller.impl.util.ControllerUtil;
import pt.solvit.probe.server.model.ServerLog;
import pt.solvit.probe.server.model.User;
import pt.solvit.probe.server.model.enums.AccessType;
import pt.solvit.probe.server.service.api.IAuthenticationService;
import pt.solvit.probe.server.service.api.IServerLogService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Base64;

@Component
public class LoggingInterceptor implements HandlerInterceptor {

    private IServerLogService serverLogService;

    public LoggingInterceptor(IServerLogService serverLogService){
        this.serverLogService = serverLogService;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        String hi = "";
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if(handler instanceof HandlerMethod){
            User user = (User) request.getAttribute("user");

            String URI = request.getRequestURI();
            String status = response.getStatus() +"";

            ServerLog serverLog = new ServerLog(null, LocalDateTime.now(), AccessType.USER, URI, user.getUserName(), LocalDateTime.now(),
                    status, null);

            serverLogService.createServerLog(serverLog);

        }
    }
}