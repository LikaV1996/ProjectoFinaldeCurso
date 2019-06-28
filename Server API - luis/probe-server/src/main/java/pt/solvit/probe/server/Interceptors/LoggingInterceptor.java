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
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if(request.getAttribute("Request_Date") == null)
        request.setAttribute("Request_Date", LocalDateTime.now());

        return true;
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if(handler instanceof HandlerMethod) {


            //username
            User user = (User) request.getAttribute("user");

            //request_date
            LocalDateTime request_date = (LocalDateTime) request.getAttribute("Request_Date");

            //access_path
            String access_path = request.getMethod().toUpperCase() + " : " + request.getRequestURI();
            if (request.getQueryString() != null)
                access_path += "?" + request.getQueryString();

            //status
            HttpStatus httpStatus = HttpStatus.valueOf(response.getStatus());
            String status = httpStatus.toString() + " " + httpStatus.getReasonPhrase();

            //detail
            String detail = (String) request.getAttribute("response_detail");   //TODO if detail is really needed (add detail to request as an attribute)


            ServerLog serverLog = new ServerLog(null, request_date, AccessType.USER, access_path, user.getUserName(), LocalDateTime.now(),
                    status, detail);
            serverLogService.createServerLog(serverLog);

        }
    }
}