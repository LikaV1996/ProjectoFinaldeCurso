/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.repository.impl;

import java.util.List;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import pt.solvit.probe.server.model.enums.EntityType;
import pt.solvit.probe.server.repository.model.TestPlanSetupDao;
import pt.solvit.probe.server.repository.api.ITestPlanSetupRepository;
import pt.solvit.probe.server.repository.exception.impl.EntityWithIdNotFoundException;

/**
 *
 * @author AnaRita
 */
@Repository
public class TestPlanSetupRepository implements ITestPlanSetupRepository {

    private JdbcTemplate jdbcTemplate;

    private static final String INSERT = "INSERT INTO TestPlan_has_Setup (test_plan_id, setup_id) VALUES (?, ?);";
    private static final String SELECT_BY_ID = "SELECT test_plan_id AS testPlanId, setup_id AS setupId FROM TestPlan_has_Setup WHERE test_plan_id = ? AND setup_id = ?;";
    private static final String SELECT_BY_TESTPLAN_ID = "SELECT test_plan_id AS testPlanId, setup_id AS setupId FROM TestPlan_has_Setup WHERE test_plan_id = ?;";
    private static final String SELECT_BY_SETUP_ID = "SELECT test_plan_id AS testPlanId, setup_id AS setupId FROM TestPlan_has_Setup WHERE setup_id = ?;";
    private static final String DELETE_BY_ID = "DELETE FROM TestPlan_has_Setup WHERE test_plan_id = ? AND setup_id = ?;";
    private static final String DELETE_BY_TESTPLAN_ID = "DELETE FROM TestPlan_has_Setup WHERE test_plan_id = ?;";

    public TestPlanSetupRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public TestPlanSetupDao findById(long testPlanId, long setupId) {
        try {
            return jdbcTemplate.queryForObject(SELECT_BY_ID, new BeanPropertyRowMapper<>(TestPlanSetupDao.class), testPlanId, setupId);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new EntityWithIdNotFoundException(EntityType.TESTPLAN,EntityType.SETUP);
        }
    }

    @Override
    public List<TestPlanSetupDao> findFromTestPlan(long testPlanId) {
        return jdbcTemplate.query(SELECT_BY_TESTPLAN_ID, new BeanPropertyRowMapper<>(TestPlanSetupDao.class), testPlanId);
    }

    @Override
    public List<TestPlanSetupDao> findFromSetup(long setupId) {
        return jdbcTemplate.query(SELECT_BY_SETUP_ID, new BeanPropertyRowMapper<>(TestPlanSetupDao.class), setupId);
    }

    @Override
    public int add(long testPlanId, long setupId) {
        return jdbcTemplate.update(INSERT, testPlanId, setupId);
    }

    @Override
    public int deleteById(long testPlanId, long setupId) {
        return jdbcTemplate.update(DELETE_BY_ID, testPlanId, setupId);
    }

    @Override
    public int deleteAllByTestPlanId(long testPlanId) {
        return jdbcTemplate.update(DELETE_BY_TESTPLAN_ID, testPlanId);
    }
}
