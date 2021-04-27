package models;

import static utils.Utils.sqlTimestampString;

public class Operation implements Model {
    public Integer id;
    public Integer articleId;
    public String articleName;
    public int debit;
    public int credit;
    public String createDate;
    public Integer balanceId;
    public String balanceCreated;
    public String balancePeriod;

    public Operation(Integer id, Integer articleId, String articleName, int debit, int credit, String createDate, Integer balanceId, String balanceCreated) {
        this.id = id;
        this.articleId = articleId;
        this.articleName = articleName;
        this.debit = debit;
        this.credit = credit;
        this.createDate = createDate;
        this.balanceId = balanceId;
        this.balanceCreated = balanceCreated;
        balancePeriod = null;
    }

    public Operation(Integer id, Integer articleId, String articleName, int debit, int credit, String createDate, Integer balanceId, String balanceCreated, String month, String year) {
        this.id = id;
        this.articleId = articleId;
        this.articleName = articleName;
        this.debit = debit;
        this.credit = credit;
        this.createDate = createDate;
        this.balanceId = balanceId;
        this.balanceCreated = balanceCreated;
        if (month != null && year != null)
            this.balancePeriod = month + "." + year;
        else
            this.balancePeriod = null;
    }

    public String getBalancePeriod() {
        return balancePeriod;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public String getRowsString() {
        return "article_id, debit, credit, create_date";
    }

    @Override
    public String getValuesString() {
        return "%d, %d, %d, %s".formatted(articleId, debit, credit, sqlTimestampString(createDate));
    }

    @Override
    public String getSetString() {
        return "article_id = %d, debit = %d, credit = %d"
                .formatted(articleId, debit, credit);
    }

    public Integer getArticleId() {
        return articleId;
    }

    public String getArticleName() {
        return articleName;
    }

    public int getDebit() {
        return debit;
    }

    public int getCredit() {
        return credit;
    }

    public String getCreateDate() {
        return createDate;
    }

    public Integer getBalanceId() {
        return balanceId;
    }

    public String getBalanceCreated() {
        return balanceCreated;
    }
}
