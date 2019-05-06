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
import pt.solvit.probe.server.model.enums.UserProfile;
import pt.solvit.probe.server.repository.api.IAuthenticationRepository;
import pt.solvit.probe.server.repository.api.IUserRepository;
import pt.solvit.probe.server.repository.exception.impl.EntityWithIdNotFoundException;
import pt.solvit.probe.server.repository.model.UserDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author AnaRita
 */
@Repository
public class AuthenticationRepository implements IAuthenticationRepository {

    @Autowired
    private AppConfiguration appConfiguration;

    private JdbcTemplate jdbcTemplate;

    private static final String SELECT_BY_ID = "SELECT id, user_name AS userName, user_password AS userPassword, user_profile AS userProfile, properties, creator, creation_date AS creationDate, modifier, modified_date AS modifiedDate, suspended FROM ProbeUser WHERE id = ?;";
    private static final String SELECT_BY_USERNAME_AND_PASSWORD = "SELECT id, user_name AS userName, user_password AS userPassword, user_profile AS userProfile, properties, creator, creation_date AS creationDate, modifier, modified_date AS modifiedDate, suspended FROM ProbeUser WHERE user_name = ?;";

    public AuthenticationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public UserDao findById(long id) {
        try {
            return jdbcTemplate.queryForObject(SELECT_BY_ID, new BeanPropertyRowMapper<>(UserDao.class), id);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new EntityWithIdNotFoundException(EntityType.USER);
        }
    }

    @Override
    public UserDao findByNameAndPassword(String userName, String password) {
        return jdbcTemplate.queryForObject(SELECT_BY_USERNAME_AND_PASSWORD, new BeanPropertyRowMapper<>(UserDao.class), userName);
    }
}
