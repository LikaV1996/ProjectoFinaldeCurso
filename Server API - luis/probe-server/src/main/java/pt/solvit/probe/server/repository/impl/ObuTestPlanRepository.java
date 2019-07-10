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
import pt.solvit.probe.server.repository.model.ObuTestPlanDao;
import pt.solvit.probe.server.repository.api.IObuTestPlanRepository;
import pt.solvit.probe.server.repository.exception.impl.EntityWithIdNotFoundException;

/**
 *
 * @author AnaRita
 */
@Repository
public class ObuTestPlanRepository implements IObuTestPlanRepository {

    @Autowired
    private AppConfiguration appConfiguration;

    private JdbcTemplate jdbcTemplate;

    private static final String INSERT_BASE = "INSERT INTO Obu_has_TestPlan (obu_id, test_plan_id, properties)";
    private static final String INSERT_POSTGRES = INSERT_BASE + " VALUES (?, ?, cast(? as jsonb));";
    private static final String INSERT_MYSQL = INSERT_BASE + " VALUES (?, ?, ?);";

    private static final String SELECT_ALL = "SELECT obu_id AS obuId, test_plan_id AS testPlanId, properties FROM Obu_has_TestPlan";
    private static final String SELECT_BY_IDS = SELECT_ALL + " WHERE obu_id = ? AND test_plan_id = ?;";
    private static final String SELECT_BY_OBU_ID = SELECT_ALL + " WHERE obu_id = ?;";
    private static final String SELECT_BY_TESTPLAN_ID = SELECT_ALL + " WHERE test_plan_id = ?;";

    private static final String SELECT_ALL_INNERJOIN_PROBEUSER_OBU = "SELECT OT.obu_id AS obuId, test_plan_id AS testPlanId, properties FROM Obu_has_TestPlan AS OT INNER JOIN probeuser_obu AS PO ON OT.obu_id = PO.obu_id";
    private static final String SELECT_ALL_REGISTERED_TO_USER = SELECT_ALL_INNERJOIN_PROBEUSER_OBU + " WHERE PO.probeuser_id = ?";
    private static final String SELECT_BY_OBU_ID_REGISTERED_TO_USER = SELECT_ALL_REGISTERED_TO_USER + " AND OT.obu_id = ?";
    private static final String SELECT_BY_TESTPLAN_ID_REGISTERED_TO_USER = SELECT_ALL_REGISTERED_TO_USER + " AND OT.test_plan_id = ?";
    private static final String SELECT_BY_IDS_REGISTERED_TO_USER = SELECT_ALL_INNERJOIN_PROBEUSER_OBU + " AND OT.obu_id = ? AND OT.test_plan_id = ?;";

    private static final String UPDATE_POSTGRES = "UPDATE Obu_has_TestPlan SET properties = cast(? as jsonb) WHERE obu_id = ? AND test_plan_id = ?;";
    private static final String UPDATE_MYSQL  = "UPDATE Obu_has_TestPlan SET properties = ? WHERE obu_id = ? AND test_plan_id = ?;";

    private static final String DELETE_BY_ID = "DELETE FROM Obu_has_TestPlan WHERE obu_id = ? AND test_plan_id = ?;";
    private static final String DELETE_BY_OBU_ID = "DELETE FROM Obu_has_TestPlan WHERE obu_id = ?;";

    public ObuTestPlanRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public ObuTestPlanDao findById(long obuId, long testPlanId, Long userID) {
        try {
            if (userID != null)
                return jdbcTemplate.queryForObject(SELECT_BY_IDS_REGISTERED_TO_USER, new BeanPropertyRowMapper<>(ObuTestPlanDao.class), userID, obuId, testPlanId);
            else
                return jdbcTemplate.queryForObject(SELECT_BY_IDS, new BeanPropertyRowMapper<>(ObuTestPlanDao.class), obuId, testPlanId);

        } catch (IncorrectResultSizeDataAccessException e) {
            throw new EntityWithIdNotFoundException(EntityType.OBU, EntityType.TESTPLAN);
        }
    }

    @Override
    public List<ObuTestPlanDao> findAll(Long userID) {
        if (userID != null)
            return jdbcTemplate.query(SELECT_ALL_REGISTERED_TO_USER, new BeanPropertyRowMapper<>(ObuTestPlanDao.class), userID);
        else
            return jdbcTemplate.query(SELECT_ALL, new BeanPropertyRowMapper<>(ObuTestPlanDao.class));
    }

    @Override
    public List<ObuTestPlanDao> findByObuId(long obuId, Long userID) {
        if (userID != null)
            return jdbcTemplate.query(SELECT_BY_OBU_ID_REGISTERED_TO_USER, new BeanPropertyRowMapper<>(ObuTestPlanDao.class), userID, obuId);
        else
            return jdbcTemplate.query(SELECT_BY_OBU_ID, new BeanPropertyRowMapper<>(ObuTestPlanDao.class), obuId);
    }

    @Override
    public List<ObuTestPlanDao> findByTestPlanId(long testPlanId, Long userID) {
        if (userID != null)
            return jdbcTemplate.query(SELECT_BY_TESTPLAN_ID_REGISTERED_TO_USER, new BeanPropertyRowMapper<>(ObuTestPlanDao.class), userID, testPlanId);
        else
            return jdbcTemplate.query(SELECT_BY_TESTPLAN_ID, new BeanPropertyRowMapper<>(ObuTestPlanDao.class), testPlanId);
    }

    @Override
    public int add(ObuTestPlanDao obuTestPlanDao) {
        if (appConfiguration.datasourceDriverClassName.contains("postgresql")) {
            return jdbcTemplate.update(INSERT_POSTGRES, obuTestPlanDao.getObuId(), obuTestPlanDao.getTestPlanId(), obuTestPlanDao.getProperties());
        }
        //mysql
        return jdbcTemplate.update(INSERT_MYSQL, obuTestPlanDao.getObuId(), obuTestPlanDao.getTestPlanId(), obuTestPlanDao.getProperties());
    }

    @Override
    public int deleteById(long obuId, long testPlanId) {
        return jdbcTemplate.update(DELETE_BY_ID, obuId, testPlanId);
    }

    @Override
    public int deleteAllByObuId(long obuId) {
        return jdbcTemplate.update(DELETE_BY_OBU_ID, obuId);
    }

    @Override
    public void update(ObuTestPlanDao obuTestPlanDao) {
        if (appConfiguration.datasourceDriverClassName.contains("postgresql")) {
            jdbcTemplate.update(UPDATE_POSTGRES, obuTestPlanDao.getProperties(), obuTestPlanDao.getObuId(), obuTestPlanDao.getTestPlanId());
        } else{
            jdbcTemplate.update(UPDATE_MYSQL, obuTestPlanDao.getProperties(), obuTestPlanDao.getObuId(), obuTestPlanDao.getTestPlanId());
        }
    }
}
