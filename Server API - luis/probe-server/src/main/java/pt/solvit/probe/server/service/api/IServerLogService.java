/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.service.api;

import java.util.List;
import pt.solvit.probe.server.model.ServerLog;
import pt.solvit.probe.server.model.User;
import pt.solvit.probe.server.model.enums.AccessType;

/**
 *
 * @author AnaRita
 */
public interface IServerLogService {

    public ServerLog createServerLog(ServerLog serverLog);

    public ServerLog getServerLog(long logId);

    public List<ServerLog> getAllServerLogs();

    public List<ServerLog> getAllServerLogs(boolean ascending, AccessType accessType, String user, int pageNumber, int pageLimit);

    public List<ServerLog> getServerLogsByType(AccessType accessType);

    public void deleteAllServerLogs(User user);

    public void updateServerLog(ServerLog serverLog);
}
