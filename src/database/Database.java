package database;

import models.*;
import oracle.jdbc.driver.OracleDriver;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Database {

    private Connection connection = null;
    private ArticleRepository articleRepository;
    private BalancesRepository balancesRepository;
    private OperationsRepository operationsRepository;


    public Database() {
        articleRepository = new ArticleRepository();
        balancesRepository = new BalancesRepository();
        operationsRepository = new OperationsRepository();
    }

    public void connect(final String login, final String password) throws SQLException {
        DriverManager.registerDriver(new OracleDriver());
        connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", login, password);
    }

    public boolean isConnected() {
        try {
            return connection.isValid(0);
        } catch (final SQLException throwable) {
            throwable.printStackTrace();
            return false;
        }
    }

    public boolean login(final String login, final String password) throws SQLException {
        CallableStatement stmt = connection.prepareCall("{? = call LOGIN(?, ?)}");
        stmt.registerOutParameter(1, Types.INTEGER);
        stmt.setString(2, login);
        stmt.setInt(3, password.hashCode());
        stmt.execute();
        return stmt.getInt(1) == 1;
    }

    public List<Article> fetchArticles() throws SQLException {
        articleRepository.fetchObjects(connection);
        return articleRepository.getList();
    }

    public List<Balance> fetchBalances() throws SQLException {
        balancesRepository.fetchObjects(connection);
        return balancesRepository.getList();
    }

    public List<Operation> fetchOperations() throws SQLException {
        System.out.println("wow");
        operationsRepository.fetchObjects(connection);
        System.out.println("wow2");
        return operationsRepository.getList();
    }


    public void saveArticle(final Article article) throws SQLException {
        if (article.id == null) {
            articleRepository.create(connection, article);
        } else {
            articleRepository.update(connection, article);
        }
    }

    public void removeArticle(final Article article) throws SQLException {
        articleRepository.remove(connection, article);
    }


    public void saveOperation(final Operation operation) throws SQLException {
        if (operation.id == null) {
            operationsRepository.create(connection, operation);
        } else {
            operationsRepository.update(connection, operation);
        }
    }

    public void removeOperation(final Operation operation) throws SQLException {
        operationsRepository.remove(connection, operation);
    }

    public void createBalance(final Balance balance) throws SQLException {
        balancesRepository.create(connection, balance);
    }

    public void removeBalance(final Balance balance) throws SQLException {
        balancesRepository.remove(connection, balance);
    }

    public List<StatObject> fetchPercents(AnalyzeInfo analyzeInfo) throws SQLException {
        CallableStatement stmt = connection.prepareCall("{call ARTICLE_PERCENTS(?, ?, ?, ?)}");
        stmt.setDate(1, analyzeInfo.dateFrom);
        stmt.setDate(2, analyzeInfo.dateTo);
        stmt.setInt(3, analyzeInfo.type);
        stmt.registerOutParameter(4, Types.REF_CURSOR);
        stmt.executeQuery();
        ResultSet cursor = stmt.getObject(4, ResultSet.class);
        List<StatObject> list = new ArrayList<>();
        while (cursor.next()) {
            System.out.println("Name = " + cursor.getString(1));
            list.add(new StatObject(cursor.getInt(1), cursor.getString(2), cursor.getInt(3), cursor.getDouble(4)));
        }
        System.out.println(Arrays.toString(list.toArray()));
        return list;
    }
}
