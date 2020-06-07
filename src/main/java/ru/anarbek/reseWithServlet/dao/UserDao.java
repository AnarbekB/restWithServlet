package ru.anarbek.reseWithServlet.dao;

import ru.anarbek.reseWithServlet.exception.UserNotFoundException;
import ru.anarbek.reseWithServlet.model.User;
import ru.anarbek.reseWithServlet.util.DbUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao {

    private final Connection connection;

    public UserDao() {
        connection = DbUtil.getConnection();
    }

    public void add(User user) {
        try {
            PreparedStatement preparedStatement =connection
                    .prepareStatement(
                            "insert into users (firstname, lastname, birthday, email) values (?, ?, ?, ?);"
                    );
            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setDate(3, new Date(user.getBirthday().getTime()));
            preparedStatement.setString(4, user.getEmail());
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void update(User user) {
        try {
            PreparedStatement preparedStatement =connection
                    .prepareStatement(
                            "update users set firstname = ?, lastname = ?, birthday = ?, email = ? where id = ?;"
                    );
            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setDate(3, new Date(user.getBirthday().getTime()));
            preparedStatement.setString(4, user.getEmail());
            preparedStatement.setInt(5, user.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    public void delete(int id) {
        try {
            PreparedStatement preparedStatement = connection
                    .prepareStatement(
                            "delete from users where id = ?;"
                    );
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        try {
            Statement statement = this.connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from users;");
            while (resultSet.next()) {
                User user = this.create(
                        resultSet.getInt("id"),
                        resultSet.getString("firstname"),
                        resultSet.getString("lastname"),
                        resultSet.getString("email"),
                        resultSet.getDate("birthday")
                );

                users.add(user);
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }

        return users;
    }

    public User getById(int id) throws UserNotFoundException {
        User user = null;
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement("select * from users where id = ?;");
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                user = this.create(
                        resultSet.getInt("id"),
                        resultSet.getString("firstname"),
                        resultSet.getString("lastname"),
                        resultSet.getString("email"),
                        resultSet.getDate("birthday")
                );
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }

        if (user == null) {
            throw new UserNotFoundException("User not found");
        }

        return user;
    }

    public User getByEmail(String email) throws UserNotFoundException {
        User user = null;
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement("select * from users where email = ?;");
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                user = this.create(
                        resultSet.getInt("id"),
                        resultSet.getString("firstname"),
                        resultSet.getString("lastname"),
                        resultSet.getString("email"),
                        resultSet.getDate("birthday")
                );
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }

        if (user == null) {
            throw new UserNotFoundException("User not found");
        }

        return user;
    }

    private User create(int id, String firstname, String lastname, String email, java.util.Date birthday) {
        User user = new User();
        user.setId(id);
        user.setFirstName(firstname);
        user.setLastName(lastname);
        user.setEmail(email);
        user.setBirthday(birthday);

        return user;
    }
}
