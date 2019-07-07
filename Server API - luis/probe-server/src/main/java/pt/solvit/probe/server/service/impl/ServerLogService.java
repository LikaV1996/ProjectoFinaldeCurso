/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.service.impl;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.solvit.probe.server.model.ServerLog;
import pt.solvit.probe.server.model.User;
import pt.solvit.probe.server.model.enums.AccessType;
import pt.solvit.probe.server.model.enums.UserProfile;
import pt.solvit.probe.server.repository.api.IServerLogRepository;
import pt.solvit.probe.server.repository.model.ServerLogDao;
import pt.solvit.probe.server.service.api.IServerLogService;
import pt.solvit.probe.server.service.api.IUserService;
import pt.solvit.probe.server.service.exception.impl.PermissionException;
import pt.solvit.probe.server.service.impl.util.ServiceUtil;

/**
 *
 * @author AnaRita
 */
@Service
public class ServerLogService implements IServerLogService {

    private static final Logger LOGGER = Logger.getLogger(ServerLogService.class.getName());

    @Autowired
    private IUserService userService;

    @Autowired
    private IServerLogRepository serverLogRepository;

    @Override
    public ServerLog createServerLog(ServerLog serverLog) {
        long id = serverLogRepository.add(transformToServerLogDao(serverLog));
        serverLog.setId(id);
        return serverLog;
    }

    @Override
    public ServerLog getServerLog(long logId) {
        ServerLogDao serverLogDao = serverLogRepository.findById(logId);
        return transformToServerLog(serverLogDao);
    }

    @Override
    public long getAllServerLogsEntries(String accessType, String accessor_name, User loggedInUser) {
        checkUserPermissions(loggedInUser);

        LOGGER.log(Level.INFO, "Finding server logs entries");
        return serverLogRepository.findNumberOfEntries(accessor_name, accessType);
    }

    @Override
    public List<ServerLog> getAllServerLogs(User loggedInUser) {
        return getAllServerLogs(true, loggedInUser);
    }

    @Override
    public List<ServerLog> getAllServerLogs(boolean ascending, User loggedInUser) {
        checkUserPermissions(loggedInUser);

        LOGGER.log(Level.INFO, "Finding all server logs");
        List<ServerLogDao> serverLogDaoList = serverLogRepository.findAll(ascending, null, null, null, null);
        return ServiceUtil.map(serverLogDaoList, this::transformToServerLog);
    }

    @Override
    public List<ServerLog> getAllServerLogs(boolean ascending, String accessType, String accessor_name, Integer pageNumber, Integer pageLimit, User loggedInUser) {
        checkUserPermissions(loggedInUser);

        LOGGER.log(Level.INFO, "Finding all server logs with a page limit");
        List<ServerLogDao> serverLogDaoList = serverLogRepository.findAll(ascending, accessor_name, accessType, pageNumber, pageLimit);
        return ServiceUtil.map(serverLogDaoList, this::transformToServerLog);
    }

    @Override
    public List<ServerLog> getServerLogsByType(String accessType) {
        LOGGER.log(Level.INFO, "Finding all server logs by access type");
        List<ServerLogDao> obuServerLogDaoList = serverLogRepository.findAll(true, accessType, null, null, null);
        return ServiceUtil.map(obuServerLogDaoList, this::transformToServerLog);
    }

    @Override
    public void deleteAllServerLogs(User loggedInUser) {
        checkUserPermissions(loggedInUser);

        LOGGER.log(Level.INFO, "Deleting all server logs");
        serverLogRepository.deleteAll();
    }

    @Override
    public void updateServerLog(ServerLog serverLog) {
        LOGGER.log(Level.INFO, "Updating server log");
        serverLogRepository.save(transformToServerLogDao(serverLog));
    }



    /*
    private List<ServerLogDao> filterByContainsUsername(List<ServerLogDao> serverLogList, String username){
        List<ServerLogDao> userServerLogList = new ArrayList();

        for (ServerLogDao curServerLog : serverLogList) {
            if (curServerLog.getAccessorName().contains(username)) {
                userServerLogList.add(curServerLog);
            }
        }

        return userServerLogList;
    }


    private List<ServerLogDao> filterByLimitAndOffset(List<ServerLogDao> serverLogList, int pageNumber, int pageLimit){
        int offset = (pageNumber-1) * pageLimit;
        List<ServerLogDao> offsetServerLogList = new ArrayList();

        try {
            for (int i = 0; i < pageLimit && (offset+i) < serverLogList.size() ; i++) {
                ServerLogDao cur = serverLogList.get(offset + i);
                offsetServerLogList.add( cur );
            }
        } catch (IndexOutOfBoundsException ex){
            throw ex;
        }

        return offsetServerLogList;
    }


    private List<ServerLogDao> filterByAccessType(List<ServerLogDao> serverLogList, AccessType accessType) {
        List<ServerLogDao> accessServerLogList = new ArrayList();
        for (ServerLogDao curServerLog : serverLogList) {
            if (AccessType.valueOf(curServerLog.getAccessType()) == accessType) {
                accessServerLogList.add(curServerLog);
            }
        }
        return accessServerLogList;
    }
    */

    private ServerLogDao transformToServerLogDao(ServerLog serverLog) {
        Timestamp responseDate = serverLog.getResponseDateLocalDateTime() == null ? null : Timestamp.valueOf(serverLog.getResponseDateLocalDateTime());

        return new ServerLogDao(serverLog.getId(), Timestamp.valueOf(serverLog.getDateLocalDateTime()), serverLog.getAccessType().name(), serverLog.getAccessPath(),
                serverLog.getAccessorName(), responseDate, serverLog.getStatus(), serverLog.getDetail());
    }

    private ServerLog transformToServerLog(ServerLogDao serverLogDao) {
        AccessType accessType = AccessType.valueOf(serverLogDao.getAccessType());
        LocalDateTime responseDate = serverLogDao.getResponseDate() == null ? null : serverLogDao.getResponseDate().toLocalDateTime();

        return new ServerLog(serverLogDao.getId(), serverLogDao.getLogDate().toLocalDateTime(), accessType, serverLogDao.getAccessPath(),
                serverLogDao.getAccessorName(), responseDate, serverLogDao.getStatus(), serverLogDao.getDetail());
    }



    private void checkUserPermissions(User loggedInUser) {
        if ( ! userService.checkUserPermissions(loggedInUser, UserProfile.ADMIN))
            throw new PermissionException();
    }
}
