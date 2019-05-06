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
import pt.solvit.probe.server.repository.model.SetupDao;
import pt.solvit.probe.server.repository.api.ISetupRepository;
import pt.solvit.probe.server.repository.exception.impl.EntityWithIdNotFoundException;

/**
 *
 * @author AnaRita
 */
@Repository
public class SetupRepository implements ISetupRepository {

    @Autowired
    private AppConfiguration appConfiguration;

    private JdbcTemplate jdbcTemplate;

    private static final String INSERT_POSTGRES = "INSERT INTO Setup (properties, creator, creation_date) VALUES (cast(? as jsonb), ?, ?) RETURNING id;";
    private static final String INSERT_MYSQL = "INSERT INTO Setup (properties, creator, creation_date) VALUES (?, ?, ?);";
    private static final String SELECT_BY_ID = "SELECT id, properties, creator, creation_date AS creationDate FROM  Setup WHERE id = ?;";
    private static final String SELECT_ALL = "SELECT id, properties, creator, creation_date AS creationDate FROM Setup;";
    private static final String UPDATE_POSTGRES = "UPDATE Setup SET properties = ? WHERE id = ?;";
    private static final String UPDATE_MYSQL = "UPDATE Setup SET properties = cast(? as jsonb) WHERE id = ?;";
    private static final String DELETE_BY_ID = "DELETE FROM Setup WHERE id = ?;";
    private static final String DELETE_ALL = "DELETE FROM Setup;";

    public SetupRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public SetupDao findById(long id) {
        try {
            return jdbcTemplate.queryForObject(SELECT_BY_ID, new BeanPropertyRowMapper<>(SetupDao.class), id);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new EntityWithIdNotFoundException(EntityType.SETUP);
        }
    }

    @Override
    public List<SetupDao> findAll() {
        return jdbcTemplate.query(SELECT_ALL, new BeanPropertyRowMapper<>(SetupDao.class));
    }

    @Transactional()
    @Override
    public long add(SetupDao setupDao) {
        if (appConfiguration.datasourceDriverClassName.contains("postgresql")) {
            return jdbcTemplate.queryForObject(INSERT_POSTGRES, Long.class, setupDao.getProperties(), setupDao.getCreator(), setupDao.getCreationDate());
        }

        //mysql
        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement statement = con.prepareStatement(INSERT_MYSQL, new String[]{"id"});
                statement.setString(1, setupDao.getProperties());
                statement.setString(2, setupDao.getCreator());
                statement.setTimestamp(3, setupDao.getCreationDate());
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
    public int deleteAll(List<SetupDao> setupList) {
        int rows = 0;
        for (SetupDao curSetup : setupList) {
            rows += deleteById(curSetup.getId());
        }
        return rows;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteAll() {
        return jdbcTemplate.update(DELETE_ALL);
    }
}