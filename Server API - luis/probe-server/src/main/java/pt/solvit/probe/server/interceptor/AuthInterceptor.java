package pt.solvit.probe.server.interceptor;


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
                throw new UnauthorizedException("Invalid token.", "Token is null.", "/probs/invalid-token", "about:blank");

            String[] authHeaderArr = authHeader.split(" ");
            if (authHeaderArr.length != 2 && !authHeaderArr[0].equals("Basic")) {
                throw new UnauthorizedException("Invalid token.", "Token is null or not Basic.", "/probs/invalid-token", "about:blank");
            }

            User user = verify(authHeaderArr[1]);

            request.setAttribute("user", user);

        }

        return true;
    }

    private User verify(String token){
        String[] decodedToken = new String(Base64.getDecoder().decode(token)).split(":");

        if(decodedToken.length != 2)
            throw new UnauthorizedException("Invalid token.", "Token is not Basic.", "/probs/invalid-token", "about:blank");

        User user = authenticationService.getAuthenticatedUser(decodedToken[0], decodedToken[1]);


        if (user.getSuspended())
            throw new ForbiddenException("User suspended.", "This user is suspended.", "/probs/user-suspended", "about:blank");

        return user;
    }
}