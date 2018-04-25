package database;

import models.Model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public abstract class GenericDatabase<E extends Model, T extends Tables.Table> {
    public T TABLE;
    DatabaseController dbController = DatabaseController.getInstance();

    public E get(UUID uuid) {
        String sql = "SELECT * FROM " + TABLE.getName() + " WHERE " + T.UUID + " = (?)";
        System.out.println("UUID"+uuid.toString());
        ResultSet rs = dbController.executeStatementQuery(sql, Arrays.asList(uuid.toString()));
        try {
            if(rs.isBeforeFirst())
                    rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return fromSQL(rs);
    }

    public E get(String field, String paramater) {
        String sql = "SELECT * FROM " + TABLE.getName() + " WHERE " + field + " = (?)";
        ResultSet rs = dbController.executeStatementQuery(sql, Arrays.asList(paramater));
        try {
            rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return fromSQL(rs);
    }

    public List<E> getAll(int count) {
        List<E> items = new ArrayList<E>();
        String sql = "SELECT * FROM " + TABLE.getName()+" LIMIT "+count;
        ResultSet rs = dbController.executeQuery(sql);
        try {
            while (rs.next()) {
                E e = fromSQL(rs);
                if (e != null)
                    items.add(e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    public List<E> getAll() {
        List<E> items = new ArrayList<E>();
        String sql = "SELECT * FROM " + TABLE.getName();
        ResultSet rs = dbController.executeQuery(sql);
        try {
            while (rs.next()) {
                E e = fromSQL(rs);
                if (e != null)
                    items.add(e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }
    public List<E> getAllLike(String column,  String parameter,int count){
        List<E> items = new ArrayList<E>();
        String sql = "SELECT * FROM " + TABLE.getName() + " WHERE " + column + " LIKE (?) LIMIT "+count;
        ResultSet rs = dbController.executeStatementQuery(sql, Arrays.asList(parameter));
        if (rs != null) {
            try {
                while (rs.next()) {
                    items.add(fromSQL(rs));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return items;
    }
    public List<E> getAllLike(String column,  String parameter){
        List<E> items = new ArrayList<E>();
        String sql = "SELECT * FROM " + TABLE.getName() + " WHERE " + column + "LIKE (?)";
        ResultSet rs = dbController.executeStatementQuery(sql, Arrays.asList(parameter));
        if (rs != null) {
            try {
                while (rs.next()) {
                    items.add(fromSQL(rs));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return items;
    }
    public List<E> getAll(String column, String parameter) {
        List<E> items = new ArrayList<E>();
        String sql = "SELECT * FROM " + TABLE.getName() + " WHERE " + column + " = (?)";
        ResultSet rs = dbController.executeStatementQuery(sql, Arrays.asList(parameter));
        if (rs != null) {
            try {
                while (rs.next()) {
                    items.add(fromSQL(rs));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return items;
    }

    public boolean insert(E e) {
        List<String> list = Arrays.asList(ToSQL(e));
        String sql = "INSERT INTO " + TABLE.getName() + " (" + TABLE.getRows() + ") VALUES (" + String.join(", ", Collections.nCopies(list.size(), "?")) + ");";

        int result = dbController.executeStatementUpdate(sql, list);
        return (result > 0);
    }

    public boolean remove(UUID uuid) {
        String sql = "DELETE FROM " + TABLE.getName() + " WHERE " + T.UUID + " = (?);";

        int results = dbController.executeStatementUpdate(sql, Arrays.asList(uuid.toString()));
        return (results > 0);
    }

    public boolean update(E e) {
        String sql = "UPDATE " + TABLE.getName() + " SET " + String.join("=?,", TABLE.getRows().split(",")) + "=? WHERE " + T.UUID + " = (?)";

        List<String> params = new ArrayList<String>(Arrays.asList(ToSQL(e)));
        params.add(e.uuid.toString());
        int results = dbController.executeStatementUpdate(sql, params);
        return (results > 0);
    }

    abstract E fromSQL(ResultSet resultSet);

    abstract String[] ToSQL(E e);
}
