package database;

import models.Article;
import models.Model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class Repository<T extends Model> {

    private List<T> list;

    T getById(int id) {
        return list.stream().filter(e -> e.getId() == id).findAny().orElse(null);
    }

    abstract String getTableName();
    String getSelectTableName() {
        return getTableName();
    }

    abstract T buildFromRow(final ResultSet resultSet) throws SQLException;

    void fetchObjects(final Connection connection) throws SQLException {
        final Statement statement = connection.createStatement();
        final ResultSet resultSet = statement.executeQuery("select * from %s".formatted(getSelectTableName()));
        list = new ArrayList<>();
        while (resultSet.next()) {
            list.add(buildFromRow(resultSet));
        }
        System.out.println(Arrays.toString(list.toArray()));
    }

    public void create(Connection connection, T obj) throws SQLException {
        final Statement statement = connection.createStatement();
        final String sql = "INSERT INTO %s (%s) VALUES (%s)"
                .formatted(getTableName(), obj.getRowsString(), obj.getValuesString());
        statement.executeUpdate(sql);
    }

    public List<T> getList() {
        return Collections.unmodifiableList(list);
    }

    public void update(Connection connection, T obj) throws SQLException {
        final Statement statement = connection.createStatement();
        final String sql = "UPDATE %s SET %s WHERE id = %d"
                .formatted(getTableName(), obj.getSetString(), obj.getId());
        System.out.println(sql);
        statement.executeUpdate(sql);
    }

    public void remove(Connection connection, T obj) throws SQLException {
        final Statement statement = connection.createStatement();
        statement.executeUpdate(
                "DELETE FROM %s WHERE id = %d"
                        .formatted(getTableName(), obj.getId())
        );
    }
}
