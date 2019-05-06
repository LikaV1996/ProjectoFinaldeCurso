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

    private static final String INSERT_POSTGRES = "INSERT INTO ObuStatus (obu_id, status_date, latitude, longitude, speed, location_properties, usable_storage, free_storage, critical_alarms, major_alarms, warning_alarms, temperature, network_interfaces) VALUES (?, ?, ?, ?, ?, cast(? as jsonb), ?, ?, ?, ?, ?, ?, cast(? as jsonb)) RETURNING id;";
    private static final String INSERT_MYSQL = "INSERT INTO ObuStatus (obu_id, status_date, latitude, longitude, speed, location_properties, usable_storage, free_storage, critical_alarms, major_alarms, warning_alarms, temperature, network_interfaces) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
    private static final String SELECT_BY_OBU_ID = "SELECT id, obu_id AS obuId, status_date AS statusDate, latitude AS lat, longitude AS lon, speed, location_properties AS locationProperties, usable_storage AS usableStorage, free_storage AS freeStorage, critical_alarms AS criticalAlarms, major_alarms AS majorAlarms, warning_alarms AS warningAlarms, temperature, network_interfaces AS networkInterfaces FROM ObuStatus WHERE obu_id = ? ORDER BY status_date DESC;";
    private static final String SELECT_LAST_BY_OBU_ID = "SELECT id, obu_id AS obuId, status_date AS statusDate, latitude AS lat, longitude AS lon, speed, location_properties AS locationProperties, usable_storage AS usableStorage, free_storage AS freeStorage, critical_alarms AS criticalAlarms, major_alarms AS majorAlarms, warning_alarms AS warningAlarms, temperature, network_interfaces AS networkInterfaces  FROM ObuStatus WHERE obu_id = ? ORDER BY status_date DESC LIMIT 1;";

    public ObuStatusRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<ObuStatusDao> findByObuId(long obuId) {
        try{
            return jdbcTemplate.query(SELECT_BY_OBU_ID, new BeanPropertyRowMapper<>(ObuStatusDao.class), obuId);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new EntityWithIdNotFoundException(EntityType.OBU);
        }
    }

    @Override
    public ObuStatusDao findLastByObuId(long obuId) {
        return jdbcTemplate.queryForObject(SELECT_LAST_BY_OBU_ID, new BeanPropertyRowMapper<>(ObuStatusDao.class), obuId);
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
