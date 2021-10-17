package by.training.finaltask.dao.daoimpl;

import by.training.finaltask.bean.entities.Role;
import by.training.finaltask.bean.entities.User;
import by.training.finaltask.dao.UserDao;
import by.training.finaltask.dao.exception.DaoException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl extends BaseDao implements UserDao {
    private final Logger debugLog = LogManager.getLogger("DebugLog");

    private static final String SELECT = "SELECT id, login, role FROM program_user WHERE role NOT LIKE 0 ORDER BY id";
    private static final String SELECT_BY_ID = "SELECT id, login, role FROM program_user WHERE id = ?";
    private static final String SELECT_LOGIN = "SELECT id, login FROM program_user WHERE login = ?";
    private static final String SELECT_LOGIN_PASS = "SELECT id, login, role FROM program_user WHERE login = ? AND password = ?";
    private static final String CREATE = "INSERT INTO program_user(login, password, role) VALUES (? , ?, ?)";
    private static final String DELETE = "DELETE FROM program_user WHERE id LIKE ?";
    private static final String UPDATE = "UPDATE program_user SET login = ?, role = ? WHERE id = ?";

    @Override
    public List<User> findAll() throws DaoException {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            List<User> users = new ArrayList<>();
            ResultSet resultSet = statement.executeQuery(SELECT);
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setLogin(resultSet.getString("login"));
                user.setRole(Role.getByCode(resultSet.getInt("role")));
                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public User findById(Integer id) throws DaoException {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(SELECT_BY_ID);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setLogin(resultSet.getString("login"));
                user.setRole(Role.getByCode(resultSet.getInt("role")));
                return user;
            }
            return null;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean delete(Integer id) throws DaoException {
        debugLog.debug("in dao delete");
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(DELETE);
            statement.setInt(1, id);
            int rows = statement.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean create(User user) throws DaoException {
        PreparedStatement statement = null;
        debugLog.debug("in dao");
        try {
            statement = connection.prepareStatement(CREATE);
            statement.setString(1, user.getLogin());
            statement.setInt(2, user.getRole().getValue());
            statement.setInt(3, user.getRole().getValue());
            debugLog.debug(statement.toString());
            int rows = statement.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean update(User user) throws DaoException {
        PreparedStatement statement;
        try {
            User user1 = findByLogin(user.getLogin());
            if (user1 == null || !user1.getLogin().equals(user.getLogin()) || user1.getId().equals(user.getId()) ) {
                statement = connection.prepareStatement(UPDATE);
                statement.setString(1, user.getLogin());
                statement.setInt(2, user.getRole().getValue());
                statement.setInt(3, user.getId());
                return statement.executeUpdate() > 0;
            } else {
                return false;
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public User findByLoginPass(String login, String pass) throws DaoException {
        PreparedStatement statement = null;
        try {
            debugLog.debug("we are in dao");
            DatabaseMetaData metaData = connection.getMetaData();
            debugLog.debug(metaData.getUserName() + metaData.getURL() + metaData.getDatabaseProductName());
            statement = connection.prepareStatement(SELECT_LOGIN_PASS);
            statement.setString(1, login);
            statement.setString(2, pass);
            debugLog.debug("we prepared statement");
            User user = null;
            ResultSet resultSet = statement.executeQuery();
            debugLog.debug("we prepared statement and result set");
            if (resultSet.next()) {
                user = new User();
                user.setId(resultSet.getInt("id"));
                user.setRole(Role.getByCode(resultSet.getInt("role")));
                user.setLogin(login);
            }
            debugLog.debug("we return user");
            return user;
        } catch (SQLException e) {
            debugLog.debug("error" + e);
            throw new DaoException(e);
        }
    }

    private User findByLogin(String login) throws DaoException {
        PreparedStatement statement;
        try {
            statement = connection.prepareStatement(SELECT_LOGIN);
            statement.setString(1, login);
            ResultSet resultSet = statement.executeQuery();
            User user = null;
            if (resultSet.next()) {
                user = new User();
                user.setLogin(resultSet.getString("login"));
                user.setId(resultSet.getInt("id"));
            }
            return user;
        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
    }

}