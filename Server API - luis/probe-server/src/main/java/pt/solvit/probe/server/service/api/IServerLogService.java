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

    public long getAllServerLogsEntries(String accessType, String accessor_name, User loggedInUser);

    public List<ServerLog> getAllServerLogs(User loggedInUser);
    public List<ServerLog> getAllServerLogs(boolean ascending, User loggedInUser);
    public List<ServerLog> getAllServerLogs(boolean ascending, String accessType, String accessor_name, Integer pageNumber, Integer pageLimit, User loggedInUser);

    public List<ServerLog> getServerLogsByType(String accessType);

    public void deleteAllServerLogs(User loggedInUser);

    public void updateServerLog(ServerLog serverLog);
}
