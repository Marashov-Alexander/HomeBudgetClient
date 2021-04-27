package database;

import models.Article;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ArticleRepository extends Repository<Article>{

    public ArticleRepository() {

    }

    @Override
    public String getTableName() {
        return "ARTICLES";
    }

    @Override
    public Article buildFromRow(final ResultSet resultSet) throws SQLException {
        return new Article(resultSet.getInt(1), resultSet.getString(2));
    }
}
