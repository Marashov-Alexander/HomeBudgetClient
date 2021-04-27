package models;

public class StatObject {
    int articleId;
    String articleName;
    int amount;
    double percent;

    public int getArticleId() {
        return articleId;
    }

    public String getArticleName() {
        return articleName;
    }

    public int getAmount() {
        return amount;
    }

    public double getPercent() {
        return percent;
    }

    public StatObject(int articleId, String articleName, int amount, double percent) {
        this.articleId = articleId;
        this.articleName = articleName;
        this.amount = amount;
        this.percent = percent;
    }
}
