package com.v1.sealert.sa.DAO;

import com.v1.sealert.sa.model.User;
import com.v1.sealert.sa.repo.UserJdbcTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserDao implements UserJdbcTemplateRepository {
    @Autowired
    private final JdbcTemplate jdbcTemplate;
    @Autowired
    private DriverManagerDataSource dataSource;


    public UserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<User> getAllUsers() {
        List<User> users = null;
        String sql = "SELECT * FROM public.user";
        users = jdbcTemplate.query(sql, (rs, i) ->  {
            User u = new User(UUID.fromString(rs.getString("guid")),
                                rs.getString("name"),
                                rs.getString("chat_id"),
                                rs.getTimestamp("date_create").toLocalDateTime());
            return u;
        });
        return users;
    }


    public boolean addUser(User u) {
        String sql = "insert into public.user (guid, name, chat_id, date_create) values (?, ?, ?, ?);";
        int i = jdbcTemplate.update(sql, u.getId(), u.getName(), u.getChatId(), u.getCreateAt());
        boolean r = i != 0 ? true : false;
        return r;
    }

    @Override
    public boolean deleteUser(UUID id) {
        String sql = "delete from public.user where guid = ?;";
        int i = jdbcTemplate.update(sql, id);
        boolean r = i != 0 ? true : false;
        return r;
    }

    @Override
    public Optional<User> findByName(String name) {
        String sql = "select * from public.user where name = ?;";
        List<User> userList = null;
        userList = jdbcTemplate.query(sql, this::userMapper, name);
        Optional<User> r = userList.stream().findFirst();
        return r;
    }

    public User userMapper(ResultSet rs, int i) throws SQLException {
        User u = new User(UUID.fromString(rs.getString("guid")),
                rs.getString("name"),
                rs.getString("chat_id"),
                rs.getTimestamp("date_create").toLocalDateTime());
        return u;
    }
}



