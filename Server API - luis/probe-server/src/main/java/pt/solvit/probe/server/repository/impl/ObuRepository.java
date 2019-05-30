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
import pt.solvit.probe.server.repository.model.ObuDao;
import pt.solvit.probe.server.repository.api.IObuRepository;
import pt.solvit.probe.server.repository.exception.impl.EntityWithIdNotFoundException;

/**
 *
 * @author AnaRita
 */
@Repository
public class ObuRepository implements IObuRepository {

    @Autowired
    private AppConfiguration appConfiguration;

    private JdbcTemplate jdbcTemplate;

    private static final String INSERT_POSTGRES = "INSERT INTO Obu (hardware_id, obu_state, current_config_id, current_test_plan_id, obu_name, obu_password, properties, creator, creation_date) VALUES (?, ?::ObuState, ?, ?, ?, ?, cast(? as jsonb), ?, ?) RETURNING id";
    private static final String INSERT_MYSQL = "INSERT INTO Obu (hardware_id, obu_state, current_config_id, current_test_plan_id, obu_name, obu_password, properties, creator, creation_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
    private static final String SELECT_BY_ID = "SELECT id, hardware_id AS hardwareId, obu_state AS obuState, current_config_id AS currentConfigId, current_test_plan_id AS currentTestPlanId, obu_name AS obuName, obu_password AS obuPassword, properties, creator, creation_date AS creationDate, modifier, modified_date AS modifiedDate FROM Obu WHERE id = ?;";
    private static final String SELECT_READY_POSTGRES = "SELECT id, hardware_id AS hardwareId, obu_state AS obuState, current_config_id AS currentConfigId, current_test_plan_id AS currentTestPlanId, obu_name AS obuName, obu_password AS obuPassword, properties, creator, creation_date AS creationDate FROM Obu WHERE hardware_id = ? AND obu_state <> 'DEACTIVATED'::ObuState;";
    private static final String SELECT_READY_MYSQL = "SELECT id, hardware_id AS hardwareId, obu_state AS obuState, current_config_id AS currentConfigId, current_test_plan_id AS currentTestPlanId, obu_name AS obuName, obu_password AS obuPassword, properties, creator, creation_date AS creationDate FROM Obu WHERE hardware_id = ? AND obu_state <> 'DEACTIVATED';";
    private static final String SELECT_BY_HARDWARE_ID = "SELECT id, hardware_id AS hardwareId, obu_state AS obuState, current_config_id AS currentConfigId, current_test_plan_id AS currentTestPlanId, obu_name AS obuName, obu_password AS obuPassword, properties, creator, creation_date AS creationDate FROM Obu WHERE hardware_id = ?;";
    private static final String SELECT_ALL = "SELECT * FROM Obu;";
    private static final String UPDATE_POSTGRES = "UPDATE Obu SET obu_name = ?, obu_state = ?::ObuState, current_config_id = ?, current_test_plan_id = ?, properties = cast(? as jsonb), modifier = ?, modified_date = CURRENT_TIMESTAMP WHERE id = ?;";
    private static final String UPDATE_MYSQL = "UPDATE Obu SET obu_name = ?, obu_state = ?, current_config_id = ?, current_test_plan_id = ?, properties = ?, modifier = ?, modified_date = CURRENT_TIMESTAMP WHERE id = ?;";
    private static final String DELETE_BY_ID = "DELETE FROM Obu WHERE id = ?;";
    private static final String DELETE_ALL = "DELETE FROM Obu;";

    public ObuRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public ObuDao findById(long id) {
        try {
            return jdbcTemplate.queryForObject(SELECT_BY_ID, new BeanPropertyRowMapper<>(ObuDao.class), id);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new EntityWithIdNotFoundException(EntityType.OBU);
        }
    }

    @Override
    public ObuDao findReadyObu(long hardwareId) {
        if (appConfiguration.datasourceDriverClassName.contains("postgresql")) {
            return jdbcTemplate.queryForObject(SELECT_READY_POSTGRES, new BeanPropertyRowMapper<>(ObuDao.class), hardwareId);
        } else { //mysql
            return jdbcTemplate.queryForObject(SELECT_READY_MYSQL, new BeanPropertyRowMapper<>(ObuDao.class), hardwareId);
        }
    }

    @Override
    public List<ObuDao> findByHardwareId(long hardwareId) {
        return jdbcTemplate.query(SELECT_BY_HARDWARE_ID, new BeanPropertyRowMapper<>(ObuDao.class), hardwareId);
    }

    @Override
    public List<ObuDao> findAll() {
        return jdbcTemplate.query(SELECT_ALL, new BeanPropertyRowMapper<>(ObuDao.class));
    }

    @Transactional()
    @Override
    public long add(ObuDao obuDao) {
        if (appConfiguration.datasourceDriverClassName.contains("postgresql")) {
            return jdbcTemplate.queryForObject(INSERT_POSTGRES, Long.class, obuDao.getHardwareId(), obuDao.getObuState(),
                    obuDao.getCurrentConfigId(), obuDao.getCurrentTestPlanId(), obuDao.getObuName(), obuDao.getObuPassword(),
                    obuDao.getProperties(), obuDao.getCreator(), obuDao.getCreationDate());
        }

        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement statement = con.prepareStatement(INSERT_MYSQL, new String[]{"id"});

                statement.setLong(1, obuDao.getHardwareId());
                statement.setString(2, obuDao.getObuState());
                if (obuDao.getCurrentConfigId() == null) {
                    statement.setNull(3, java.sql.Types.BIGINT);
                } else {
                    statement.setLong(3, obuDao.getCurrentConfigId());
                }
                if (obuDao.getCurrentTestPlanId() == null) {
                    statement.setNull(4, java.sql.Types.BIGINT);
                } else {
                    statement.setLong(4, obuDao.getCurrentTestPlanId());
                }
                statement.setString(5, obuDao.getObuName());
                statement.setString(6, obuDao.getObuPassword());
                statement.setString(7, obuDao.getProperties());
                statement.setString(8, obuDao.getCreator());
                statement.setTimestamp(9, obuDao.getCreationDate());
                return statement;
            }
        }, holder);

        return holder.getKey().longValue();
    }

    @Override
    public void save(ObuDao obuDao) {
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement statement = null;
                if (appConfiguration.datasourceDriverClassName.contains("postgresql")) {
                    statement = con.prepareStatement(UPDATE_POSTGRES);
                } else { //mysql
                    statement = con.prepareStatement(UPDATE_MYSQL);
                }
                statement.setString(1, obuDao.getObuName());
                statement.setString(2, obuDao.getObuState());
                if (obuDao.getCurrentConfigId() == null) {
                    statement.setNull(3, java.sql.Types.BIGINT);
                } else {
                    statement.setLong(3, obuDao.getCurrentConfigId());
                }
                if (obuDao.getCurrentTestPlanId() == null) {
                    statement.setNull(4, java.sql.Types.BIGINT);
                } else {
                    statement.setLong(4, obuDao.getCurrentTestPlanId());
                }
                statement.setString(5, obuDao.getProperties());
                statement.setString(6, obuDao.getModifier());
                statement.setLong(7, obuDao.getId());
                return statement;
            }
        });
    }

    @Override
    public int deleteById(long id) {
        return jdbcTemplate.update(DELETE_BY_ID, id);
    }

    @Override
    public int deleteAll() {
        return jdbcTemplate.update(DELETE_ALL);
    }
}
