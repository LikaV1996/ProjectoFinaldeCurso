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
import pt.solvit.probe.server.model.User;
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

    private static final String INSERT_BASE = "INSERT INTO ServerLog (log_date, access_type, access_path, accessor_name, response_date, status, detail)";
    private static final String INSERT_POSTGRES = INSERT_BASE + " VALUES (?, ?::AccessType, ?, ?, ?, ?, ?) RETURNING id;";
    private static final String INSERT_MYSQL = INSERT_BASE + " VALUES (?, ?, ?, ?, ?, ?, ?);";

    private static final String SELECT_ENTRIES = "SELECT count(*) FROM ServerLog";
    private static final String SELECT_ALL = "SELECT id, log_date AS logDate, access_type AS accessType, access_path AS accessPath, accessor_name AS accessorName, response_date AS responseDate, status, detail FROM ServerLog";
    private static final String SELECT_ALL_W_LIMIT_OFFSET = SELECT_ALL + " LIMIT ? OFFSET ?";
    private static final String SELECT_BY_ID = SELECT_ALL + " WHERE id = ? ";

    private static final String UPDATE = "UPDATE ServerLog SET accessor_name = ?, response_date = ?, status = ?, detail = ? WHERE id = ?;";

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
    public List<ServerLogDao> findAll(Boolean ascending, String accessor_name, String access_type, Integer pageNumber, Integer pageLimit) {
        return jdbcTemplate.query(makeFilteredServerLogQuery(ascending, accessor_name, access_type, pageNumber, pageLimit) , new BeanPropertyRowMapper<>(ServerLogDao.class));
    }

    @Override
    public long findNumberOfEntries(String accessor_name, String access_type) {
        return jdbcTemplate.queryForObject(makeFilteredServerLogEntriesQuery(accessor_name, access_type), Long.class);
    }

    @Transactional()
    @Override
    public long add(ServerLogDao serverLogDao) {
        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        if (appConfiguration.datasourceDriverClassName.contains("postgresql")) {
            return jdbcTemplate.queryForObject(INSERT_POSTGRES, Long.class, serverLogDao.getLogDate(), serverLogDao.getAccessType(),  serverLogDao.getAccessPath(),
                    serverLogDao.getAccessorName(), serverLogDao.getResponseDate(), serverLogDao.getStatus(), serverLogDao.getDetail());
        }

        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement statement = con.prepareStatement(INSERT_MYSQL, new String[]{"id"});
                statement.setTimestamp(1, serverLogDao.getLogDate());
                statement.setString(2, serverLogDao.getAccessType());
                statement.setString(3, serverLogDao.getAccessPath());
                statement.setString(4, serverLogDao.getAccessorName());
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
                statement.setString(1, serverLog.getAccessorName());
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



    private String makeFilteredServerLogQuery(Boolean ascending, String accessor_name, String access_type, Integer pageNumber, Integer pageLimit){

        //ASCENDING or DESCENDING
        String orderBy = " ORDER BY " + "log_date" + " ",
                order = "DESC";
        if (ascending != null){
            order = ascending ? "ASC" : "DESC";
        }
        orderBy += order;


        String whereStmt = makeWhereStatement(accessor_name, access_type);

        //pagination
        String limit = "";
        if (pageNumber != null && pageLimit != null){
            int offset = ((pageNumber -1) * pageLimit);
            limit = " LIMIT " + pageLimit + (offset >= 0 ? " OFFSET " + ((pageNumber -1) * pageLimit) : "" );
        }


        return SELECT_ALL + whereStmt + orderBy + limit;
    }

    private String makeFilteredServerLogEntriesQuery(String accessor_name, String access_type){

        String whereStmt = makeWhereStatement(accessor_name, access_type);

        return SELECT_ENTRIES + whereStmt;
    }


    //filter/where statements
    private String makeWhereStatement(String accessor_name, String access_type) {
        boolean whereStmtBool = accessor_name != null || access_type != null,
                doubleWhereStmtBool = accessor_name != null && access_type != null;

        String accessor_nameWhereStmt = accessor_name != null ? ("accessor_name LIKE '%' || '"+ accessor_name +"' || '%'") : "";
        String access_typeWhereStmt = access_type != null ? ("access_type = '"+ access_type +"'") : "";

        return whereStmtBool ? (" WHERE " + accessor_nameWhereStmt + (doubleWhereStmtBool ? " AND " : "") + access_typeWhereStmt) : "";
    }

}
