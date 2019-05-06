/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.service.impl;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.solvit.probe.server.model.ServerLog;
import pt.solvit.probe.server.model.enums.AccessType;
import pt.solvit.probe.server.repository.api.IServerLogRepository;
import pt.solvit.probe.server.repository.model.ServerLogDao;
import pt.solvit.probe.server.service.api.IServerLogService;
import pt.solvit.probe.server.service.impl.util.ServiceUtil;

/**
 *
 * @author AnaRita
 */
@Service
public class ServerLogService implements IServerLogService {

    private static final Logger LOGGER = Logger.getLogger(ServerLogService.class.getName());

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
    public List<ServerLog> getAllServerLogs() {
        LOGGER.log(Level.INFO, "Finding all server logs");
        List<ServerLogDao> serverLogDaoList = serverLogRepository.findAll();
        return ServiceUtil.map(serverLogDaoList, this::transformToServerLog);
    }

    @Override
    public List<ServerLog> getServerLogsByType(AccessType accessType) {
        LOGGER.log(Level.INFO, "Finding all server logs");
        List<ServerLogDao> serverLogDaoList = serverLogRepository.findAll();
        List<ServerLogDao> obuServerLogDaoList = filterByAccessType(serverLogDaoList, accessType);
        return ServiceUtil.map(obuServerLogDaoList, this::transformToServerLog);
    }

    @Override
    public void deleteAllServerLogs() {
        LOGGER.log(Level.INFO, "Deleting all server logs");
        serverLogRepository.deleteAll();
    }

    @Override
    public void updateServerLog(ServerLog serverLog) {
        LOGGER.log(Level.INFO, "Updating server log");
        serverLogRepository.save(transformToServerLogDao(serverLog));
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

    private ServerLogDao transformToServerLogDao(ServerLog serverLog) {
        Timestamp responseDate = serverLog.getResponseDate() == null ? null : Timestamp.valueOf(serverLog.getResponseDate());

        return new ServerLogDao(serverLog.getId(), Timestamp.valueOf(serverLog.getDate()), serverLog.getAccessType().name(), serverLog.getAccessPath(),
                serverLog.getAccessUser(), responseDate, serverLog.getStatus(), serverLog.getDetail());
    }

    private ServerLog transformToServerLog(ServerLogDao serverLogDao) {
        AccessType accessType = AccessType.valueOf(serverLogDao.getAccessType());
        LocalDateTime responseDate = serverLogDao.getResponseDate() == null ? null : serverLogDao.getResponseDate().toLocalDateTime();

        return new ServerLog(serverLogDao.getId(), serverLogDao.getLogDate().toLocalDateTime(), accessType, serverLogDao.getAccessPath(),
                serverLogDao.getAccessUser(), responseDate, serverLogDao.getStatus(), serverLogDao.getDetail());
    }
}
