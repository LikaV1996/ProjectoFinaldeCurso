/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.controller.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pt.solvit.probe.server.config.AppConfiguration;
import pt.solvit.probe.server.controller.api.IServerLogController;
import pt.solvit.probe.server.controller.impl.util.ControllerUtil;
import pt.solvit.probe.server.model.User;
import pt.solvit.probe.server.model.ServerLog;
import pt.solvit.probe.server.model.enums.AccessType;
import pt.solvit.probe.server.model.enums.UserProfile;
import pt.solvit.probe.server.service.api.IServerLogService;
import pt.solvit.probe.server.service.api.IUserService;

import javax.servlet.http.HttpServletRequest;

@RestController
public class ServerLogController implements IServerLogController {

    private static final String REQUEST_TIME = "Request Time";
    private static final String USER = "User";
    private static final String REQUEST_URI = "Request URI";
    private static final String RESPONSE_TIME = "Response Time";
    private static final String STATUS = "Status";
    private static final String DETAILS = "Details";

    @Autowired
    private IUserService userService;
    @Autowired
    private IServerLogService serverLogService;

    @Override
    public ResponseEntity<String> getServerLog(HttpServletRequest request, @RequestHeader(value = "Authorization", required = true) String authorization) {

        //User user = userService.checkUserCredentials(authorization);

        List<ServerLog> serverLogList = serverLogService.getAllServerLogs();
        String serverLogStr = serverLogListToString(serverLogList);

        //ServerLog serverLog = ControllerUtil.transformToServerLog(user, RequestMethod.GET, HttpStatus.OK, AppConfiguration.URL_SERVER_LOG);
        //serverLogService.createServerLog(serverLog);

        return ResponseEntity.ok(serverLogStr);
    }

    @Override
    public ResponseEntity<String> getObuServerLog(HttpServletRequest request, @RequestHeader(value = "Authorization", required = true) String authorization) {

        //User user = userService.checkUserCredentials(authorization);

        List<ServerLog> obuServerLogList = serverLogService.getServerLogsByType(AccessType.OBU);
        String serverLogStr = serverLogListToString(obuServerLogList);

        //ServerLog serverLog = ControllerUtil.transformToServerLog(user, RequestMethod.GET, HttpStatus.OK, AppConfiguration.URL_SERVER_LOG_OBU);
        //serverLogService.createServerLog(serverLog);

        return ResponseEntity.ok(serverLogStr);
    }

    @Override
    public ResponseEntity<String> getUserServerLog(HttpServletRequest request, @RequestHeader(value = "Authorization", required = true) String authorization) {

        //User user = userService.checkUserCredentials(authorization);

        List<ServerLog> userServerLogList = serverLogService.getServerLogsByType(AccessType.USER);
        String serverLogStr = serverLogListToString(userServerLogList);

        //ServerLog serverLog = ControllerUtil.transformToServerLog(user, RequestMethod.GET, HttpStatus.OK, AppConfiguration.URL_SERVER_LOG_USER);
        //serverLogService.createServerLog(serverLog);

        return ResponseEntity.ok(serverLogStr);
    }

    @Override
    public ResponseEntity<Void> clearServerLog(HttpServletRequest request, @RequestHeader(value = "Authorization", required = true) String authorization) {

        User user = (User) request.getAttribute("user");

        userService.checkUserPermissions(user, UserProfile.SUPER_USER);

        serverLogService.deleteAllServerLogs();

        //ServerLog serverLog = ControllerUtil.transformToServerLog(user, RequestMethod.DELETE, HttpStatus.OK, AppConfiguration.URL_SERVER_LOG);
        //serverLogService.createServerLog(serverLog);

        return ResponseEntity.ok().build();
    }

    private String serverLogListToString(List<ServerLog> serverLogList) {
        if (serverLogList.isEmpty()) {
            return "No logs found";
        }

        //Calculate header size - Time
        int maxTimeSize = 19;

        //Calculate header size - User
        int maxUserSize = USER.length();
        for (ServerLog curServerLog : serverLogList) {
            int userSize = curServerLog.getAccessUser().length();
            if (userSize > maxUserSize) {
                maxUserSize = userSize;
            }
        }

        //Calculate header size - URI
        int maxUriSize = REQUEST_URI.length();
        for (ServerLog curServerLog : serverLogList) {
            int uriSize = curServerLog.getAccessPath().length();
            if (uriSize > maxUriSize) {
                maxUriSize = uriSize;
            }
        }

        //Calculate header size - Status
        int maxStatusSize = STATUS.length();
        for (ServerLog curServerLog : serverLogList) {
            if(curServerLog.getStatus()!=null){
                int statusSize = curServerLog.getStatus().length();
                if (statusSize > maxStatusSize) {
                    maxStatusSize = statusSize;
                }
            }
        }

        //Create header
        StringBuilder sb = new StringBuilder();
        sb.append(" ").append(REQUEST_TIME);
        for (int timeSize = REQUEST_TIME.length(); timeSize < maxTimeSize; timeSize++) {
            sb.append(" ");
        }
        sb.append("  |  ").append(USER);
        for (int userSize = USER.length(); userSize < maxUserSize; userSize++) {
            sb.append(" ");
        }
        sb.append("  |  ").append(REQUEST_URI);
        for (int uriSize = REQUEST_URI.length(); uriSize < maxUriSize; uriSize++) {
            sb.append(" ");
        }
        sb.append("  |  ").append(RESPONSE_TIME);
        for (int timeSize = RESPONSE_TIME.length(); timeSize < maxTimeSize; timeSize++) {
            sb.append(" ");
        }
        sb.append("  |  ").append(STATUS).append("\n");

        int headerSize = sb.length();
        for (int idx = 0; idx < headerSize; idx++) {
            sb.append("-");
        }
        sb.append("\n");

        //Append logs
        for (ServerLog curServerLog : serverLogList) {
            sb.append(curServerLog.toLogString(maxTimeSize, maxUserSize, maxUriSize, maxStatusSize)).append("\n");
        }

        return sb.toString();
    }

}
