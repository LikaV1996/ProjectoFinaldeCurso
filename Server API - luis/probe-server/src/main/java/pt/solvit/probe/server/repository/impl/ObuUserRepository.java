/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.solvit.probe.server.repository.impl;

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
import pt.solvit.probe.server.repository.api.IObuRepository;
import pt.solvit.probe.server.repository.api.IObuUserRepository;
import pt.solvit.probe.server.repository.exception.impl.EntityWithIdNotFoundException;
import pt.solvit.probe.server.repository.model.ObuDao;
import pt.solvit.probe.server.repository.model.ObuUserDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;


@Repository
public class ObuUserRepository implements IObuUserRepository {

    @Autowired
    private AppConfiguration appConfiguration;

    private JdbcTemplate jdbcTemplate;

    private static final String INSERT_BASE = "INSERT INTO ProbeUser_Obu (probeuser_id, obu_id, role)";
    private static final String INSERT_POSTGRES = INSERT_BASE + " VALUES (?, ?, ?::PO_Role) RETURNING probeuser_id";
    private static final String INSERT_MYSQL = INSERT_BASE + " VALUES (?, ?, ?);";

    private static final String SELECT_ALL = "SELECT probeuser_id AS userID, user_name AS userName, obu_id AS obuID, obu_name AS obuName, role FROM view_Probeuser_Obu";
    private static final String SELECT_BY_ID = SELECT_ALL + " WHERE probeuser_id = ? AND obu_id = ?;";
    private static final String SELECT_BY_USER_ID = SELECT_ALL + " WHERE probeuser_id = ?;";
    private static final String SELECT_BY_OBU_ID = SELECT_ALL + " WHERE obu_id = ?;";

    private static final String UPDATE_POSTGRES = "UPDATE Probeuser_Obu SET role = ? WHERE probeuser_id = ? AND obu_id = ?;";

    private static final String DELETE_ALL = "DELETE FROM Probeuser_Obu";
    private static final String DELETE_BY_ID = DELETE_ALL + " WHERE probeuser_id = ? AND obu_id = ?;";
    private static final String DELETE_BY_USER_ID = DELETE_ALL + " WHERE probeuser_id = ?;";
    private static final String DELETE_BY_OBU_ID = DELETE_ALL + " WHERE obu_id = ?;";


    public ObuUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public ObuUserDao findById(long obuID, long userID) {
        try {
            return jdbcTemplate.queryForObject(SELECT_BY_ID, new BeanPropertyRowMapper<>(ObuUserDao.class), userID, obuID);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new EntityWithIdNotFoundException(EntityType.OBU, EntityType.USER);
        }
    }

    @Override
    public List<ObuUserDao> findAll() {
        return jdbcTemplate.query(SELECT_ALL, new BeanPropertyRowMapper<>(ObuUserDao.class));
    }

    @Override
    public List<ObuUserDao> findAllByUserId(long userID) {
        try {
            return jdbcTemplate.query(SELECT_BY_USER_ID, new BeanPropertyRowMapper<>(ObuUserDao.class), userID);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new EntityWithIdNotFoundException(EntityType.USER);
        }
    }

    @Override
    public List<ObuUserDao> findAllByObuId(long obuID) {
        try {
            return jdbcTemplate.query(SELECT_BY_OBU_ID, new BeanPropertyRowMapper<>(ObuUserDao.class), obuID);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new EntityWithIdNotFoundException(EntityType.OBU);
        }
    }

    @Override
    public long add(ObuUserDao obuUserDao) {
        return jdbcTemplate.queryForObject(INSERT_POSTGRES, Long.class, obuUserDao.getUserID(), obuUserDao.getObuID(), obuUserDao.getRole());
    }

    /*
    @Override
    public int deleteByUserId(long userID) {
        return jdbcTemplate.update(DELETE_BY_USER_ID, userID);
    }

    @Override
    public int deleteByObuId(long obuID) {
        return jdbcTemplate.update(DELETE_BY_OBU_ID), obuID;
    }
    */

    @Override
    public int deleteById(long obuID, long userID) {
        return jdbcTemplate.update(DELETE_BY_ID, userID, obuID);
    }

    /*
    @Override
    public int deleteAll() {
        return jdbcTemplate.update(DELETE_ALL);
    }
    */


    @Override
    public void updateRole(ObuUserDao obuUserDao) {
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement statement = con.prepareStatement(UPDATE_POSTGRES);

                statement.setString(1, obuUserDao.getRole());
                statement.setLong(2, obuUserDao.getUserID());
                statement.setLong(3, obuUserDao.getObuID());

                return statement;
            }
        });
    }
}
