/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.repository.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import pt.solvit.probe.server.config.AppConfiguration;
import pt.solvit.probe.server.model.enums.EntityType;
import pt.solvit.probe.server.repository.model.ObuConfigDao;
import pt.solvit.probe.server.repository.api.IObuConfigRepository;
import pt.solvit.probe.server.repository.exception.impl.EntityWithIdNotFoundException;

/**
 *
 * @author AnaRita
 */
@Repository
public class ObuConfigRepository implements IObuConfigRepository {

    @Autowired
    private AppConfiguration appConfiguration;

    private JdbcTemplate jdbcTemplate;

    private static final String INSERT_POSTGRES = "INSERT INTO Obu_has_Config (obu_id, config_id, properties) VALUES (?, ?, cast(? as jsonb))";
    private static final String INSERT_MYSQL = "INSERT INTO Obu_has_Config (obu_id, config_id, properties) VALUES (?, ?, ?);";
    private static final String SELECT_BY_ID = "SELECT obu_id AS obuId, config_id AS configId, properties FROM Obu_has_Config WHERE obu_id = ? AND config_id = ?;";
    private static final String SELECT_BY_OBU_ID = "SELECT obu_id AS obuId, config_id AS configId, properties FROM Obu_has_Config WHERE obu_id = ?;";
    private static final String SELECT_BY_CONFIG_ID = "SELECT obu_id AS obuId, config_id AS configId, properties FROM Obu_has_Config WHERE config_id = ?;";
    private static final String SELECT_ALL = "SELECT obu_id AS obuId, config_id AS configId, properties FROM Obu_has_Config;";
    private static final String UPDATE_POSTGRES = "UPDATE Obu_has_Config SET properties = cast(? as jsonb) WHERE obu_id = ? AND config_id = ?;";
    private static final String UPDATE_MYSQL = "UPDATE Obu_has_Config SET properties = ? WHERE obu_id = ? AND config_id = ?;";
    private static final String DELETE_BY_ID = "DELETE FROM Obu_has_Config WHERE obu_id = ? AND config_id = ?;";
    private static final String DELETE_BY_OBU_ID = "DELETE FROM Obu_has_Config WHERE obu_id = ?;";

    public ObuConfigRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public ObuConfigDao findById(long obuId, long configId) {
        try {
            return jdbcTemplate.queryForObject(SELECT_BY_ID, new BeanPropertyRowMapper<>(ObuConfigDao.class), obuId, configId);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new EntityWithIdNotFoundException(EntityType.CONFIG);
        }
    }

    @Override
    public List<ObuConfigDao> findAll() {
        return jdbcTemplate.query(SELECT_ALL, new BeanPropertyRowMapper<>(ObuConfigDao.class));
    }

    @Override
    public List<ObuConfigDao> findByObuId(long obuId) {
        return jdbcTemplate.query(SELECT_BY_OBU_ID, new BeanPropertyRowMapper<>(ObuConfigDao.class), obuId);
    }

    @Override
    public List<ObuConfigDao> findByConfigId(long configId) {
        return jdbcTemplate.query(SELECT_BY_CONFIG_ID, new BeanPropertyRowMapper<>(ObuConfigDao.class), configId);
    }

    @Override
    public int addConfigToObu(ObuConfigDao obuConfigDao) {
        if (appConfiguration.datasourceDriverClassName.contains("postgresql")) {
            return jdbcTemplate.update(INSERT_POSTGRES, obuConfigDao.getObuId(), obuConfigDao.getConfigId(), obuConfigDao.getProperties());
        }
        //mysql
        return jdbcTemplate.update(INSERT_MYSQL, obuConfigDao.getObuId(), obuConfigDao.getConfigId(), obuConfigDao.getProperties());
    }

    @Override
    public int removeConfigFromObu(long obuId, long configId) {
        return jdbcTemplate.update(DELETE_BY_ID, obuId, configId);
    }

    @Override
    public int removeAllConfigsFromObu(long obuId) {
        return jdbcTemplate.update(DELETE_BY_OBU_ID, obuId);
    }

    @Override
    public void save(ObuConfigDao obuConfigDao) {
        if (appConfiguration.datasourceDriverClassName.contains("postgresql")) {
            jdbcTemplate.update(UPDATE_POSTGRES, obuConfigDao.getProperties(), obuConfigDao.getObuId(), obuConfigDao.getConfigId());
        } else{
            jdbcTemplate.update(UPDATE_MYSQL, obuConfigDao.getProperties(), obuConfigDao.getObuId(), obuConfigDao.getConfigId());
        }
    }
}
