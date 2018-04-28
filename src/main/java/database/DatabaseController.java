package database;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DatabaseController {
    private static final String DB_USERNAME = "cutedogs";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/cutedogs";
    private static DatabaseController ourInstance = new DatabaseController();
    private Connection connection;
    private MysqlDataSource mysqlDataSource;

    private DatabaseController() {
        mysqlDataSource = new MysqlDataSource();
        mysqlDataSource.setUser(DB_USERNAME);
        byte[] encoded;
        String DB_PASS = "";
        try {
            encoded = Files.readAllBytes(Paths.get("p.pwd"));
             DB_PASS = new String(encoded,"UTF-8");
            System.out.println(DB_PASS);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mysqlDataSource.setPassword(DB_PASS);
        mysqlDataSource.setUrl(DB_URL);
        try {
            System.out.println("Connecting to database");
            connection = (Connection) mysqlDataSource.getConnection();
            System.out.println("Connected to cutedogs");
            initTables();
            //deleteTables();
            //resultSet.close();

            //connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static DatabaseController getInstance() {
        return ourInstance;
    }

    public MysqlDataSource getMysqlDataSource() {
        return mysqlDataSource;
    }

    public ResultSet executeQuery(String sql) {
        Statement statement = null;
        try {
            statement = (Statement) connection.createStatement();
            return statement.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ResultSet executeStatementQuery(String sql, List<String> params) {
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            for (int i = 1; i <= params.size(); i++) {
                statement.setString(i, params.get(i - 1));

            }

            return statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int executeStatementUpdate(String sql, List<String> params) {

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            for (int j = 1; j <= params.size(); j++) {
                statement.setString(j, params.get(j - 1));

            }
            return statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void deleteTables() {
        try {
            Statement statement = (Statement) connection.createStatement();
            String sql = "DROP TABLE IF EXISTS "+ Tables.LikesTable.TABLE_NAME + ","+ Tables.ImageTable.TABLE_NAME + "," + Tables.DogTable.TABLE_NAME + "," + Tables.UserTable.TABLE_NAME + "," + Tables.ProfilesTable.TABLE_NAME +";";

            int result = statement.executeUpdate(sql);
            System.out.println("All Tables Dropped " + result);
        } catch (SQLException e) {
            System.out.println("ERROR");
            e.printStackTrace();
        }
    }

    public void initTables() {

        this.initTable(new Tables.UserTable());
        this.initTable(new Tables.DogTable());
        this.initTable(new Tables.ImageTable());
        this.initTable(new Tables.ProfilesTable());
        this.initTable(new Tables.LikesTable());
    }

    public void initTable(Tables.Table t) {
        Statement statement = null;
        try {
            statement = (Statement) connection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS " + t.getName() + t.getSQL() + ";";
            //System.out.println(sql);
            int result = statement.executeUpdate(sql);
            System.out.println("Table " + t.getName() + " initialized " + result);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
