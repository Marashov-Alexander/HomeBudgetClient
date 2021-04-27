package database;

import models.Balance;
import models.Operation;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class OperationsRepository extends Repository<Operation>{

    public OperationsRepository() {

    }

    @Override
    public String getTableName() {
        return "OPERATIONS";
    }

    @Override
    String getSelectTableName() {
        return "FULL_OPERATIONS";
    }

    @Override
    public Operation buildFromRow(final ResultSet resultSet) throws SQLException {
        return new Operation(
                resultSet.getInt(1),
                resultSet.getInt(2),
                resultSet.getString(3),
                resultSet.getInt(4),
                resultSet.getInt(5),
                resultSet.getString(6),
                resultSet.getInt(7),
                resultSet.getString(8),
                resultSet.getString(9),
                resultSet.getString(10)
        );
    }
}
