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
import pt.solvit.probe.server.repository.model.TestDao;
import pt.solvit.probe.server.repository.api.ITestRepository;
import pt.solvit.probe.server.repository.exception.impl.EntityWithIdNotFoundException;

/**
 *
 * @author AnaRita
 */
@Repository
public class TestRepository implements ITestRepository {

    @Autowired
    private AppConfiguration appConfiguration;

    private JdbcTemplate jdbcTemplate;

    private static final String INSERT_POSTGRES = "INSERT INTO Test (test_index, test_type, delay, setup_id, properties) VALUES (?, ?, ?, ?, cast(? as jsonb)) RETURNING id;";
    private static final String INSERT_MYSQL = "INSERT INTO Test (test_index, test_type, delay, setup_id, properties) VALUES (?, ?, ?, ?, ?);";
    private static final String SELECT_BY_ID = "SELECT id, test_index AS testIndex, test_type AS testType, delay, setup_id AS setupId, properties FROM Test WHERE id = ?;";
    private static final String SELECT_BY_SETUP_ID = "SELECT id, test_index AS testIndex, test_type AS testType, delay, setup_id AS setupId, properties FROM Test WHERE setup_id = ?;";
    private static final String SELECT_ALL = "SELECT id, test_index AS testIndex, test_type AS testType, delay, setup_id AS setupId, properties FROM Test;";
    private static final String DELETE_BY_ID = "DELETE FROM Test WHERE id = ?;";

    public TestRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public TestDao findById(long id) {
        try {
            return jdbcTemplate.queryForObject(SELECT_BY_ID, new BeanPropertyRowMapper<>(TestDao.class), id);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new EntityWithIdNotFoundException(EntityType.TEST);
        }
    }

    @Override
    public List<TestDao> findAll() {
        return jdbcTemplate.query(SELECT_ALL, new BeanPropertyRowMapper<>(TestDao.class));
    }

    @Override
    public List<TestDao> findBySetupId(long setupId) {
        return jdbcTemplate.query(SELECT_BY_SETUP_ID, new BeanPropertyRowMapper<>(TestDao.class), setupId);
    }

    @Transactional()
    @Override
    public long add(TestDao testDao) {
        if (appConfiguration.datasourceDriverClassName.contains("postgresql")) {
            return jdbcTemplate.queryForObject(INSERT_POSTGRES, Long.class, testDao.getTestIndex(), testDao.getTestType(), testDao.getDelay(),
                    testDao.getSetupId(), testDao.getProperties());
        }

        //mysql
        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement statement = con.prepareStatement(INSERT_MYSQL, new String[]{"id"});
                statement.setLong(1, testDao.getTestIndex());
                statement.setString(2, testDao.getTestType());
                statement.setLong(3, testDao.getDelay());
                statement.setLong(4, testDao.getSetupId());
                statement.setString(5, testDao.getProperties());
                return statement;
            }
        }, holder);

        return holder.getKey().longValue();
    }

    @Override
    public int deleteById(long id) {
        return jdbcTemplate.update(DELETE_BY_ID, id);
    }
}
