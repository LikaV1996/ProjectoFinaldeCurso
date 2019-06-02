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
import pt.solvit.probe.server.repository.model.ConfigDao;
import pt.solvit.probe.server.repository.api.IConfigRepository;
import pt.solvit.probe.server.repository.exception.impl.EntityWithIdNotFoundException;

/**
 *
 * @author AnaRita
 */
@Repository
public class ConfigRepository implements IConfigRepository {

    @Autowired
    private AppConfiguration appConfiguration;

    private JdbcTemplate jdbcTemplate;

    private static final String INSERT_BASE = "INSERT INTO Config (config_name, activation_date, properties, creator, creation_date)";
    private static final String INSERT_POSTGRES = INSERT_BASE + " VALUES (?, ?, cast(? as jsonb), ?, ?) RETURNING id;";
    private static final String INSERT_MYSQL = INSERT_BASE + " VALUES (?, ?, ?, ?, ?);";
    private static final String SELECT_ALL = "SELECT id, config_name, activation_date AS activationDate, properties, creator, creation_date AS creationDate, modifier, modified_date AS modifiedDate FROM Config";
    private static final String SELECT_BY_ID = SELECT_ALL + " WHERE id = ?;";
    private static final String UPDATE_POSTGRES = "UPDATE Config SET config_name = ?, activation_date = ?, properties = cast(? as jsonb) WHERE id = ?;";
    private static final String UPDATE_MYSQL = "UPDATE Config SET config_name = ?, activation_date = ?, properties = ? WHERE id = ?;";
    private static final String DELETE_ALL = "DELETE FROM Config";
    private static final String DELETE_BY_ID = DELETE_ALL + " WHERE id = ?;";

    public ConfigRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public ConfigDao findById(long id) {
        try {
            ConfigDao configDao = jdbcTemplate.queryForObject(SELECT_BY_ID, new BeanPropertyRowMapper<>(ConfigDao.class), id);
            return configDao;
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new EntityWithIdNotFoundException(EntityType.CONFIG);
        }
    }

    @Override
    public List<ConfigDao> findAll() {
        return jdbcTemplate.query(SELECT_ALL, new BeanPropertyRowMapper<>(ConfigDao.class));
    }

    @Transactional()
    @Override
    public long add(ConfigDao configDao) {
        if (appConfiguration.datasourceDriverClassName.contains("postgresql")) {
            return jdbcTemplate.queryForObject(INSERT_POSTGRES, Long.class, configDao.getConfigName(), configDao.getActivationDate(), configDao.getProperties(), configDao.getCreator(), configDao.getCreationDate());
        }

        //mysql
        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement statement = con.prepareStatement(INSERT_MYSQL, new String[]{"id"});
                statement.setString(1, configDao.getConfigName());
                statement.setTimestamp(2, configDao.getActivationDate());
                statement.setString(3, configDao.getProperties());
                statement.setString(4, configDao.getCreator());
                statement.setTimestamp(5, configDao.getCreationDate());
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
    public int deleteAll(List<ConfigDao> configDaoList) {
        int rows = 0;
        for (ConfigDao curConfigDao : configDaoList) {
            rows += deleteById(curConfigDao.getId());
        }
        return rows;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteAll() {
        return jdbcTemplate.update(DELETE_ALL);
    }
}
