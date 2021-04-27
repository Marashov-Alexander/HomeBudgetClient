package database;

import models.Article;
import models.Balance;

import java.sql.*;

public class BalancesRepository extends Repository<Balance>{

    public BalancesRepository() {

    }

    @Override
    public String getTableName() {
        return "BALANCE";
    }

    @Override
    public void create(Connection connection, Balance obj) throws SQLException {
        CallableStatement stmt = connection.prepareCall("{call CREATE_BALANCE(?, ?)}");
        stmt.setInt(1, obj.month);
        stmt.setInt(2, obj.year);
        stmt.execute();
    }

    @Override
    public Balance buildFromRow(final ResultSet resultSet) throws SQLException {
        return new Balance(
                resultSet.getInt(1),
                resultSet.getString(2),
                resultSet.getInt(3),
                resultSet.getInt(4),
                resultSet.getInt(5),
                resultSet.getInt(6),
                resultSet.getInt(7)
        );
    }
}
