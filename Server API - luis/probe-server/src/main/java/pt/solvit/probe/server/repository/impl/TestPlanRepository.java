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
import pt.solvit.probe.server.repository.model.TestPlanDao;
import pt.solvit.probe.server.repository.api.ITestPlanRepository;
import pt.solvit.probe.server.repository.exception.impl.EntityWithIdNotFoundException;

/**
 *
 * @author AnaRita
 */
@Repository
public class TestPlanRepository implements ITestPlanRepository {

    @Autowired
    private AppConfiguration appConfiguration;

    private JdbcTemplate jdbcTemplate;

    private static final String INSERT_POSTGRES = "INSERT INTO TestPlan (start_date, stop_date, properties, creator, creation_date) VALUES (?, ?, cast(? as jsonb), ?, ?) RETURNING id;";
    private static final String INSERT_MYSQL = "INSERT INTO TestPlan (start_date, stop_date, properties, creator, creation_date) VALUES (?, ?, ?, ?, ?);";
    private static final String SELECT_BY_ID = "SELECT id, start_date AS startDate, stop_date AS stopDate, properties, creator, creation_date AS creationDate FROM TestPlan WHERE id = ?;";
    private static final String SELECT_ALL = "SELECT id, start_date AS startDate, stop_date AS stopDate, properties, creator, creation_date AS creationDate FROM TestPlan;";
    private static final String UPDATE_POSTGRES = "UPDATE TestPlan SET start_date = ?, stop_date = ?, properties = ? WHERE id = ?;";
    private static final String UPDATE_MYSQL = "UPDATE TestPlan SET start_date = ?, stop_date = ?, properties = cast(? as jsonb) WHERE id = ?;";
    private static final String DELETE_BY_ID = "DELETE FROM TestPlan WHERE id = ?;";
    private static final String DELETE_ALL = "DELETE FROM TestPlan;";

    public TestPlanRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public TestPlanDao findById(long id) {
        try {
            return jdbcTemplate.queryForObject(SELECT_BY_ID, new BeanPropertyRowMapper<>(TestPlanDao.class), id);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new EntityWithIdNotFoundException(EntityType.TESTPLAN);
        }
    }

    @Override
    public List<TestPlanDao> findAll() {
        return jdbcTemplate.query(SELECT_ALL, new BeanPropertyRowMapper<>(TestPlanDao.class));
    }

    @Transactional()
    @Override
    public long add(TestPlanDao testPlanDao) {
        if (appConfiguration.datasourceDriverClassName.contains("postgresql")) {
            return jdbcTemplate.queryForObject(INSERT_POSTGRES, Long.class, testPlanDao.getStartDate(), testPlanDao.getStopDate(),
                    testPlanDao.getProperties(), testPlanDao.getCreator(), testPlanDao.getCreationDate());
        }

        //mysql
        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement statement = con.prepareStatement(INSERT_MYSQL, new String[]{"id"});
                statement.setTimestamp(1, testPlanDao.getStartDate());
                statement.setTimestamp(2, testPlanDao.getStopDate());
                statement.setString(3, testPlanDao.getProperties());
                statement.setString(4, testPlanDao.getCreator());
                statement.setTimestamp(5, testPlanDao.getCreationDate());
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
    public int deleteAll(List<TestPlanDao> testPlanDaoList) {
        int rows = 0;
        for (TestPlanDao curTestPlanDao : testPlanDaoList) {
            rows += deleteById(curTestPlanDao.getId());
        }
        return rows;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteAll() {
        return jdbcTemplate.update(DELETE_ALL);
    }
}
