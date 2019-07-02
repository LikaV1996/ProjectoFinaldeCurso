package pt.solvit.probe.server.Interceptors;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import pt.solvit.probe.server.controller.exception.ForbiddenException;
import pt.solvit.probe.server.controller.exception.UnauthorizedException;
import pt.solvit.probe.server.model.User;
import pt.solvit.probe.server.service.api.IAuthenticationService;
import pt.solvit.probe.server.service.impl.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Base64;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    IAuthenticationService authenticationService;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(handler instanceof HandlerMethod /*&& ((HandlerMethod) handler).getMethod().getDeclaringClass().isAnnotationPresent(NoAuthorizationRequired.class)*/ ) {

            String authHeader = request.getHeader("Authorization");

            if (authHeader == null)
                throw new UnauthorizedException("Invalid token.", "Token is null.", "string", "about:blank");

            String[] authHeaderArr = authHeader.split(" ");
            if (authHeaderArr.length != 2 && !authHeaderArr[0].equals("Basic")) {
                throw new UnauthorizedException("Invalid token.", "Token is null or not Basic.", "string", "about:blank");
            }

            User user = verify(authHeaderArr[1]);

            request.setAttribute("user", user);
            /*
            request.setAttribute("userID",user.getId());
            request.setAttribute("userName",user.getUserName());
            request.setAttribute("userProfile",user.getUserProfile());//.toUpperCase());
            */

        }

        return true;
    }

    private User verify(String token){
        String[] decodedToken = new String(Base64.getDecoder().decode(token)).split(":");

        if(decodedToken.length != 2)
            throw new UnauthorizedException("Invalid token.", "Token is not Basic.", "string", "about:blank");

        User user = authenticationService.getAuthenticatedUser(decodedToken[0], decodedToken[1]);


        if (user.getSuspended())
            throw new ForbiddenException("User suspended.", "This user is suspended.", "string", "about:blank");

        return user;
    }
}