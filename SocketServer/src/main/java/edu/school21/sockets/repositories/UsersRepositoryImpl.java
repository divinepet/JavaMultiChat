package edu.school21.sockets.repositories;
import edu.school21.sockets.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;
import static java.lang.String.format;

@Repository
public class UsersRepositoryImpl extends JdbcTemplate implements UsersRepository {
    private final String      tableName;
    private final RowMapper<User> ROW_MAPPER;

    @Autowired
    @Qualifier("dataSource")
    public void setRepository(DataSource dataSource) {
        super.setDataSource(dataSource);
    }

    public UsersRepositoryImpl() {
        this.tableName = "chat";
        this.ROW_MAPPER = (ResultSet resultSet, int rowNum) -> {
            return new User(resultSet.getLong("id"), resultSet.getString("username"), resultSet.getString("password"));
        };
    }

    @Override
    public Optional<User> findById(Long id)  {
        try {
            User user = super.queryForObject(format("SELECT * FROM %s WHERE id = %d", tableName, id), ROW_MAPPER);
            return Optional.ofNullable(user);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        try {
            User user = super.queryForObject(format("SELECT * FROM %s WHERE username = '%s';", tableName, username), ROW_MAPPER);
            return (Optional.ofNullable(user));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<User> findAll()  {
        return super.query(format("SELECT * FROM %s;", tableName), ROW_MAPPER);
    }

    @Override
    public void save(User entity)  {
        super.update(format("INSERT INTO %s (username, password) VALUES ('%s', '%s');", tableName, entity.getUsername(), entity.getPassword()));
    }

    @Override
    public void update(User entity) {
        super.update(format("UPDATE %s SET username = '%s', password = '%s' WHERE id = %d;",
                tableName, entity.getUsername(), entity.getPassword(), entity.getId()));
    }

    @Override
    public void delete(Long id) {
        super.update(format("DELETE FROM %s WHERE id = %d;", tableName, id));
    }
}