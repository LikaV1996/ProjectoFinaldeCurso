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
import pt.solvit.probe.server.repository.api.ISysLogRepository;
import pt.solvit.probe.server.repository.exception.impl.EntityWithIdNotFoundException;
import pt.solvit.probe.server.repository.model.SysLogDao;

/**
 *
 * @author AnaRita
 */
@Repository
public class SysLogRepository implements ISysLogRepository {

    @Autowired
    private AppConfiguration appConfiguration;

    private JdbcTemplate jdbcTemplate;

    private static final String INSERT_BASE = "INSERT INTO SysLog (obu_id, file_name, close_date, upload_date, file_data, properties)";
    private static final String INSERT_POSTGRES = INSERT_BASE + " VALUES (?, ?, ?, ?, ?, cast(? as jsonb)) RETURNING id;";
    private static final String INSERT_MYSQL = INSERT_BASE + " VALUES (?, ?, ?, ?, ?, ?);";

    private static final String SELECT_ALL = "SELECT id, obu_id AS obuId, file_name AS fileName, close_date AS closeDate, upload_date AS uploadDate, file_data AS fileData, properties FROM SysLog";
    private static final String SELECT_BY_OBU_ID = SELECT_ALL + " WHERE obu_id = ?;";
    private static final String SELECT_BY_ID_AND_OBU_ID = SELECT_ALL + " WHERE id = ? AND obu_id = ?;";

    public SysLogRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public SysLogDao findByObuIDAndID(long obuId, long id) {
        try {
            return jdbcTemplate.queryForObject(SELECT_BY_ID_AND_OBU_ID, new BeanPropertyRowMapper<>(SysLogDao.class), id, obuId);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new EntityWithIdNotFoundException(EntityType.TESTLOG);
        }
    }

    @Override
    public List<SysLogDao> findAllByObuId(long obuId, boolean ascending, String filename, Integer pageNumber, Integer pageLimit) {
        return jdbcTemplate.query(makeFilteredSysLogQuery(obuId, ascending, filename, pageNumber, pageLimit), new BeanPropertyRowMapper<>(SysLogDao.class));
    }

    @Transactional()
    @Override
    public long add(SysLogDao sysLogDao) {
        if (appConfiguration.datasourceDriverClassName.contains("postgresql")) {
            return jdbcTemplate.queryForObject(INSERT_POSTGRES, Long.class, sysLogDao.getObuId(), sysLogDao.getFileName(), sysLogDao.getCloseDate(),
                    sysLogDao.getUploadDate(), sysLogDao.getFileData(), sysLogDao.getProperties());
        }

        //mysql
        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement statement = con.prepareStatement(INSERT_MYSQL, new String[]{"id"});
                statement.setLong(1, sysLogDao.getObuId());
                statement.setString(2, sysLogDao.getFileName());
                statement.setTimestamp(3, sysLogDao.getCloseDate());
                statement.setTimestamp(4, sysLogDao.getUploadDate());
                statement.setBytes(5, sysLogDao.getFileData());
                statement.setString(6, sysLogDao.getProperties());
                return statement;
            }
        }, holder);

        return holder.getKey().longValue();
    }

    private String makeFilteredSysLogQuery(long obuID, Boolean ascending, String filename, Integer pageNumber, Integer pageLimit){

        //ASCENDING or DESCENDING
        String orderBy = " ORDER BY " + "close_date" + " ",
                order = "DESC";
        if (ascending != null){
            order = ascending ? "ASC" : "DESC";
        }
        orderBy += order;


        String filenameWhereStmt = filename != null ? ("file_name LIKE '%' || '"+ filename +"' || '%'") : "";

        String whereStmt = " WHERE obu_id = " + obuID + (filename != null ? " AND " + filenameWhereStmt : "");

        //pagination
        String limit = "";
        if (pageNumber != null && pageLimit != null){
            int offset = ((pageNumber -1) * pageLimit);
            limit = " LIMIT " + pageLimit + (offset <= 0 ? " OFFSET " + ((pageNumber -1) * pageLimit) : "" );
        }


        return SELECT_ALL + whereStmt + orderBy + limit;
    }
}
