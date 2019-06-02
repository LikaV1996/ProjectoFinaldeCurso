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
import pt.solvit.probe.server.repository.model.UserDao;
import pt.solvit.probe.server.model.enums.UserProfile;
import pt.solvit.probe.server.repository.exception.impl.EntityWithIdNotFoundException;
import pt.solvit.probe.server.repository.api.IUserRepository;

/**
 *
 * @author AnaRita
 */
@Repository
public class UserRepository implements IUserRepository {

    @Autowired
    private AppConfiguration appConfiguration;

    private JdbcTemplate jdbcTemplate;

    private static final String INSERT_BASE = "INSERT INTO ProbeUser (user_name, user_password, user_profile, properties, creator, creation_date)";
    private static final String INSERT_POSTGRES =   INSERT_BASE + " VALUES (?, ?, ?, cast(? as jsonb), ?, ?) RETURNING id;";
    private static final String INSERT_MYSQL =      INSERT_BASE + " VALUES (?, ?, ?, ?, ?, ?);";
    private static final String SELECT_ALL = "SELECT id, user_name AS userName, user_password AS userPassword, user_profile AS userProfile, properties, creator, creation_date AS creationDate, modifier, modified_date AS modifiedDate, suspended FROM ProbeUser";
    private static final String SELECT_BY_ID =          SELECT_ALL + " WHERE id = ?;";
    private static final String SELECT_BY_USERNAME =    SELECT_ALL + " WHERE user_name = ?;";
    private static final String UPDATE_POSTGRES = "UPDATE ProbeUser SET user_name = ?, user_password = ?, user_profile = ?, properties = cast(? as jsonb), modifier = ?, modified_date = CURRENT_TIMESTAMP , suspended = ? WHERE id = ? RETURNING id;";
    private static final String UPDATE_MYSQL = "UPDATE ProbeUser SET user_name = ?, user_password = ?, user_profile = ?, properties = ?, modifier = ?, modified_date = CURRENT_TIMESTAMP, suspended = ? WHERE id = ?;";
    private static final String DELETE_BY_ID = "DELETE FROM ProbeUser WHERE id = ?;";

    public UserRepository(JdbcTemplate jdbcTemplate) {
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
    public UserDao findByName(String userName) {
        return jdbcTemplate.queryForObject(SELECT_BY_USERNAME, new BeanPropertyRowMapper<>(UserDao.class), userName);
    }

    @Override
    public List<UserDao> findAll() {
        return jdbcTemplate.query(SELECT_ALL, new BeanPropertyRowMapper<>(UserDao.class));
    }

    @Transactional()
    @Override
    public long add(UserDao userDao) {
        if (appConfiguration.datasourceDriverClassName.contains("postgresql")) {
            return jdbcTemplate.queryForObject(INSERT_POSTGRES, Long.class, userDao.getUserName(), userDao.getUserPassword(),
                    userDao.getUserProfile(), userDao.getProperties(), userDao.getCreator(), userDao.getCreationDate());
        }

        //mysql
        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement statement = con.prepareStatement(INSERT_MYSQL, new String[]{"id"});
                statement.setString(1, userDao.getUserName());
                statement.setString(2, userDao.getUserPassword());
                statement.setString(3, userDao.getUserProfile());
                statement.setString(4, userDao.getProperties());
                statement.setString(5, userDao.getCreator());
                statement.setTimestamp(6, userDao.getCreationDate());

                if(userDao.getModifier() != null)
                    statement.setString(7, userDao.getModifier());
                else
                    statement.setObject(7, null);

                if(userDao.getModifiedDate() != null)
                    statement.setTimestamp(8, userDao.getModifiedDate());
                else
                    statement.setObject(8, null);
                statement.setBoolean(9, userDao.getSuspended());
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
    public int deleteAll() {
        int rows = 0;
        List<UserDao> userDaoList = findAll();
        for (UserDao curUserDao : userDaoList) {
            if (UserProfile.valueOf(curUserDao.getUserProfile()) != UserProfile.ADMIN) { //Delete all users except admin
                rows += deleteById(curUserDao.getId());
            }
        }
        return rows;
    }

    @Override
    public int updateByID(UserDao userDao) {
        if (appConfiguration.datasourceDriverClassName.contains("postgresql")) {
            return jdbcTemplate.queryForObject(UPDATE_POSTGRES, Integer.class, userDao.getUserName(), userDao.getUserPassword(),
                    userDao.getUserProfile(), userDao.getProperties(), userDao.getModifier(), userDao.getSuspended(), userDao.getId());
        }

        String rip = "not implemented exception";
        return -1;
    }
}
