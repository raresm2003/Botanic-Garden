package Repositories;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Repository {
    protected Connection connection;

    private static final String url = "jdbc:mysql://localhost:3306/botanicgardendb";
    private static final String username = "root";
    private static final String password = "1234";

    public Repository() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void openConnection() {
        try {
            if (this.connection != null && !this.connection.isClosed())
                return;
            this.connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        try {
            if (this.connection != null && !this.connection.isClosed())
                this.connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean executeSQL(String sql) {
        boolean result = true;
        try {
            openConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            if (statement.executeUpdate() == 0)
                result = false;
        } catch (SQLException e) {
            e.printStackTrace();
            result = false;
        } finally {
            closeConnection();
        }
        return result;
    }

    public ResultSet getResultSet(String sql) {
        ResultSet result = null;
        try {
            openConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            result = statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
