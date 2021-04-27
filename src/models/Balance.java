package models;

public class Balance implements Model {
    public Integer id;
    public String createDate;
    public Integer debit;
    public Integer credit;
    public Integer amount;
    public int year;
    public int month;

    public Balance(Integer id, String createDate, Integer debit, Integer credit, Integer amount, int month, int year) {
        this.id = id;
        this.createDate = createDate;
        this.debit = debit;
        this.credit = credit;
        this.amount = amount;
        this.month = month;
        this.year = year;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public String getRowsString() {
        return null;
    }

    @Override
    public String getValuesString() {
        return null;
    }

    @Override
    public String getSetString() {
        return null;
    }

    public String getCreateDate() {
        return createDate;
    }

    public int getDebit() {
        return debit;
    }

    public int getCredit() {
        return credit;
    }

    public int getAmount() {
        return amount;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }
}
