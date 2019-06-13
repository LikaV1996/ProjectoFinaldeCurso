/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pt.solvit.probe.server.config.AppConfiguration;
import pt.solvit.probe.server.model.enums.EntityType;
import pt.solvit.probe.server.repository.api.IServerLogRepository;
import pt.solvit.probe.server.repository.exception.impl.EntityWithIdNotFoundException;
import pt.solvit.probe.server.repository.model.ServerLogDao;

/**
 *
 * @author AnaRita
 */
@Repository
public class ServerLogRepository implements IServerLogRepository {

    @Autowired
    private AppConfiguration appConfiguration;

    private JdbcTemplate jdbcTemplate;

    private static final String INSERT_BASE = "INSERT INTO ServerLog (log_date, access_type, access_path, access_user, response_date, status, detail)";
    private static final String INSERT_POSTGRES = INSERT_BASE + " VALUES (?, ?::AccessType, ?, ?, ?, ?, ?) RETURNING id;";
    private static final String INSERT_MYSQL = INSERT_BASE + " VALUES (?, ?, ?, ?, ?, ?, ?);";
    private static final String SELECT_ALL = "SELECT id, log_date AS logDate, access_type AS accessType, access_path AS accessPath, access_user AS accessUser, response_date AS responseDate, status, detail FROM ServerLog";
    private static final String ORDER_BY_LOG_DATE = " ORDER BY log_date ";
    //private static final String SELECT_ALL_W_LIMIT_OFFSET = SELECT_ALL + " LIMIT ? OFFSET ?";
    private static final String SELECT_BY_ID = SELECT_ALL + " WHERE id = ? ";
    private static final String UPDATE = "UPDATE ServerLog SET access_user = ?, response_date = ?, status = ?, detail = ? WHERE id = ?;";
    private static final String DELETE_BY_ID = "DELETE FROM ServerLog WHERE id = ?;";
    private static final String DELETE_ALL = "DELETE FROM ServerLog;";

    public ServerLogRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public ServerLogDao findById(long id) {
        try {
            return jdbcTemplate.queryForObject(SELECT_BY_ID, new BeanPropertyRowMapper<>(ServerLogDao.class), id);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new EntityWithIdNotFoundException(EntityType.SERVERLOG);
        }
    }

    @Override
    public List<ServerLogDao> findAll(boolean ascending) {
        /*
        StringBuilder orderByLogDate = new StringBuilder(ORDER_BY_LOG_DATE);
        int replaceIdx = orderByLogDate.indexOf("?");
        orderByLogDate.replace(replaceIdx, replaceIdx, (ascending ? "ASC" : "DESC"));

        String queryStr = SELECT_ALL + orderByLogDate.toString();
        */

        String queryStr = SELECT_ALL + ORDER_BY_LOG_DATE + (ascending ? "ASC" : "DESC");
        return jdbcTemplate.query(queryStr , new BeanPropertyRowMapper<>(ServerLogDao.class));
    }

    @Transactional()
    @Override
    public long add(ServerLogDao serverLogDao) {
        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        if (appConfiguration.datasourceDriverClassName.contains("postgresql")) {
            return jdbcTemplate.queryForObject(INSERT_POSTGRES, Long.class, serverLogDao.getLogDate(), serverLogDao.getAccessType(),  serverLogDao.getAccessPath(),
                    serverLogDao.getAccessUser(), serverLogDao.getResponseDate(), serverLogDao.getStatus(), serverLogDao.getDetail());
        }

        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement statement = con.prepareStatement(INSERT_MYSQL, new String[]{"id"});
                statement.setTimestamp(1, serverLogDao.getLogDate());
                statement.setString(2, serverLogDao.getAccessType());
                statement.setString(3, serverLogDao.getAccessPath());
                statement.setString(4, serverLogDao.getAccessUser());
                if (serverLogDao.getResponseDate() == null) {
                    statement.setNull(5, java.sql.Types.TIMESTAMP);
                    statement.setNull(6, java.sql.Types.VARCHAR);
                    statement.setNull(7, java.sql.Types.VARCHAR);
                } else {
                    statement.setTimestamp(5, serverLogDao.getResponseDate());
                    statement.setString(6, serverLogDao.getStatus());
                    statement.setString(7, serverLogDao.getDetail());
                }
                return statement;
            }
        }, holder);

        return holder.getKey().longValue();
    }

    @Override
    public int deleteById(long id) {
        return jdbcTemplate.update(DELETE_BY_ID, id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteAll(List<ServerLogDao> serverLogDaoList) {
        int rows = 0;
        for (ServerLogDao curServerLogDao : serverLogDaoList) {
            rows += deleteById(curServerLogDao.getId());
        }
        return rows;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteAll() {
        return jdbcTemplate.update(DELETE_ALL);
    }

    @Override
    public void save(ServerLogDao serverLog) {
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement statement = con.prepareStatement(UPDATE);
                statement.setString(1, serverLog.getAccessUser());
                statement.setTimestamp(2, serverLog.getResponseDate());
                statement.setString(3, serverLog.getStatus());
                if (serverLog.getDetail() == null) {
                    statement.setNull(4, java.sql.Types.VARCHAR);
                } else {
                    statement.setString(4, serverLog.getDetail());
                }
                statement.setLong(5, serverLog.getId());
                return statement;
            }
        });
    }
}
