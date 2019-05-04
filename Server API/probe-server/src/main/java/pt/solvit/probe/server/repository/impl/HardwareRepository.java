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
import pt.solvit.probe.server.repository.exception.impl.EntityWithIdNotFoundException;
import pt.solvit.probe.server.repository.model.HardwareDao;
import pt.solvit.probe.server.repository.api.IHardwareRepository;

/**
 *
 * @author AnaRita
 */
@Repository
public class HardwareRepository implements IHardwareRepository {

    @Autowired
    private AppConfiguration appConfiguration;

    private JdbcTemplate jdbcTemplate;

    private static final String INSERT_POSTGRES = "INSERT INTO Hardware (serial_number, properties, creator, creation_date) VALUES (?, cast(? as jsonb), ?, ?) RETURNING id";
    private static final String INSERT_MYSQL = "INSERT INTO Hardware (serial_number, properties, creator, creation_date) VALUES (?, ?, ?, ?);";
    private static final String SELECT_BY_ID = "SELECT id, serial_number as serialNumber, properties, creator, creation_date as creationDate FROM Hardware WHERE id = ?;";
    private static final String SELECT_BY_SERIAL_NUMBER = "SELECT id, serial_number as serialNumber, properties, creator, creation_date as creationDate FROM Hardware WHERE serial_number = ?;";
    private static final String SELECT_ALL = "SELECT id, serial_number as serialNumber, properties, creator, creation_date as creationDate FROM Hardware;";
    private static final String UPDATE_POSTGRES = "UPDATE Hardware SET serial_number = ?, properties = ? WHERE id = ?;";
    private static final String UPDATE_MYSQL = "UPDATE Hardware SET serial_number = ?, properties = cast(? as jsonb) WHERE id = ?;";
    private static final String DELETE_BY_ID = "DELETE FROM Hardware WHERE id = ?;";
    private static final String DELETE_ALL = "DELETE FROM Hardware;";

    public HardwareRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public HardwareDao findById(long id) {
        try {
            return jdbcTemplate.queryForObject(SELECT_BY_ID, new BeanPropertyRowMapper<>(HardwareDao.class), id);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new EntityWithIdNotFoundException(EntityType.HARDWARE);
        }
    }

    @Override
    public HardwareDao findBySerialNumber(String serialNumber) {
        return jdbcTemplate.queryForObject(SELECT_BY_SERIAL_NUMBER, new BeanPropertyRowMapper<>(HardwareDao.class), serialNumber);
    }

    @Override
    public List<HardwareDao> findAll() {
        return jdbcTemplate.query(SELECT_ALL, new BeanPropertyRowMapper<>(HardwareDao.class));
    }

    @Transactional()
    @Override
    public long add(HardwareDao hardwareDao) {
        if (appConfiguration.datasourceDriverClassName.contains("postgresql")) {
            return jdbcTemplate.queryForObject(INSERT_POSTGRES, Long.class, hardwareDao.getSerialNumber(), hardwareDao.getProperties(), hardwareDao.getCreator(), hardwareDao.getCreationDate());
        }

        //mysql
        GeneratedKeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement statement = con.prepareStatement(INSERT_MYSQL, new String[]{"id"});
                statement.setString(1, hardwareDao.getSerialNumber());
                statement.setString(2, hardwareDao.getProperties());
                statement.setString(3, hardwareDao.getCreator());
                statement.setTimestamp(4, hardwareDao.getCreationDate());
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
    public int deleteAll(List<HardwareDao> hardwareDaoList) {
        int rows = 0;
        for (HardwareDao curHardwareDao : hardwareDaoList) {
            rows += deleteById(curHardwareDao.getId());
        }
        return rows;
    }

    @Override
    public int deleteAll() {
        return jdbcTemplate.update(DELETE_ALL);
    }
}
