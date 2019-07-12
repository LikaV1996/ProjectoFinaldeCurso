/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
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
import pt.solvit.probe.server.repository.model.ObuStatusDao;
import pt.solvit.probe.server.repository.api.IObuStatusRepository;
import pt.solvit.probe.server.repository.exception.impl.EntityWithIdNotFoundException;

/**
 *
 * @author AnaRita
 */
@Repository
public class ObuStatusRepository implements IObuStatusRepository {

    @Autowired
    private AppConfiguration appConfiguration;

    private JdbcTemplate jdbcTemplate;

    private static final String INSERT_BASE = "INSERT INTO ObuStatus (obu_id, status_date, latitude, longitude, speed, location_properties, usable_storage, free_storage, critical_alarms, major_alarms, warning_alarms, temperature, network_interfaces)";
    private static final String INSERT_POSTGRES = INSERT_BASE + " VALUES (?, ?, ?, ?, ?, cast(? as jsonb), ?, ?, ?, ?, ?, ?, cast(? as jsonb)) RETURNING id;";
    private static final String INSERT_MYSQL = INSERT_BASE + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

    private static final String SELECT_ALL = "SELECT id, OS.obu_id AS obuId, status_date AS statusDate, latitude AS lat, longitude AS lon, speed, location_properties AS locationProperties, usable_storage AS usableStorage, free_storage AS freeStorage, critical_alarms AS criticalAlarms, major_alarms AS majorAlarms, warning_alarms AS warningAlarms, temperature, network_interfaces AS networkInterfaces FROM ObuStatus AS OS";
    private static final String SELECT_ALL_INNERJOIN_PROBEUSER_OBU = SELECT_ALL + " INNER JOIN Probeuser_OBU AS PO ON OS.obu_id = PO.obu_id";

    private static final String ORDER_BY_STATUSDATE = " ORDER BY status_date DESC";

    public ObuStatusRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    private String makeFilteredObuStatusQuery(long obuId, Timestamp endDateTS, Timestamp startDateTS, boolean lastPosition, Long userID) {
        String SELECT_INTERVAL_BY_OBU_ID = userID != null ? SELECT_ALL_INNERJOIN_PROBEUSER_OBU : SELECT_ALL;

        SELECT_INTERVAL_BY_OBU_ID += " WHERE " + (userID != null ? "PO.probeuser_id = " + userID + " AND " : "") + "OS.obu_id = " + obuId;

        if (endDateTS != null)  SELECT_INTERVAL_BY_OBU_ID += " AND status_date <= " + "'"+endDateTS.toString()+"'";
        if(startDateTS != null) SELECT_INTERVAL_BY_OBU_ID += " AND status_date >= " + "'"+startDateTS.toString()+"'";

        SELECT_INTERVAL_BY_OBU_ID += ORDER_BY_STATUSDATE;

        if (lastPosition)   SELECT_INTERVAL_BY_OBU_ID += " LIMIT 1";

        return SELECT_INTERVAL_BY_OBU_ID;
    }


    @Override
    public List<ObuStatusDao> findByObuId(long obuId, Long userID) {
        return this.findIntervalByObuId(obuId, null, null, userID);
        /*
        try{
            return jdbcTemplate.query(makeFilteredObuStatusQuery(obuId, null, null), new BeanPropertyRowMapper<>(ObuStatusDao.class));
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new EntityWithIdNotFoundException(EntityType.OBU);
        }
        */
    }

    @Override
    public List<ObuStatusDao> findIntervalByObuId(long obuId, Timestamp endDateLDT, Timestamp startDateLDT, Long userID) {
        try{
            return jdbcTemplate.query(makeFilteredObuStatusQuery(obuId, endDateLDT, startDateLDT, false, userID), new BeanPropertyRowMapper<>(ObuStatusDao.class));
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new EntityWithIdNotFoundException(EntityType.OBU);
        }
    }

    @Override
    public ObuStatusDao findLastByObuId(long obuId, Long userID) {
        return jdbcTemplate.queryForObject(makeFilteredObuStatusQuery(obuId, null, null, true, userID), new BeanPropertyRowMapper<>(ObuStatusDao.class));
    }

    @Transactional()
    @Override
    public long add(ObuStatusDao obuStatusDao) {
        if (appConfiguration.datasourceDriverClassName.contains("postgresql")) {
            return jdbcTemplate.queryForObject(INSERT_POSTGRES, Long.class, obuStatusDao.getObuId(), obuStatusDao.getStatusDate(), obuStatusDao.getLat(),
                    obuStatusDao.getLon(), obuStatusDao.getSpeed(), obuStatusDao.getLocationProperties(), obuStatusDao.getUsableStorage(), obuStatusDao.getFreeStorage(),
                    obuStatusDao.getCriticalAlarms(), obuStatusDao.getMajorAlarms(), obuStatusDao.getWarningAlarms(), obuStatusDao.getTemperature(), obuStatusDao.getNetworkInterfaces());
        }

        //mysql
        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement statement = con.prepareStatement(INSERT_MYSQL, new String[]{"id"});
                statement.setLong(1, obuStatusDao.getObuId());
                statement.setTimestamp(2, obuStatusDao.getStatusDate());
                statement.setDouble(3, obuStatusDao.getLat());
                statement.setDouble(4, obuStatusDao.getLon());
                statement.setDouble(5, obuStatusDao.getSpeed());
                statement.setString(6, obuStatusDao.getLocationProperties());
                statement.setLong(7, obuStatusDao.getUsableStorage());
                statement.setLong(8, obuStatusDao.getFreeStorage());
                statement.setInt(9, obuStatusDao.getCriticalAlarms());
                statement.setInt(10, obuStatusDao.getMajorAlarms());
                statement.setInt(11, obuStatusDao.getWarningAlarms());
                statement.setDouble(12, obuStatusDao.getTemperature());
                statement.setString(13, obuStatusDao.getNetworkInterfaces());
                return statement;
            }
        }, holder);

        return holder.getKey().longValue();
    }
}
