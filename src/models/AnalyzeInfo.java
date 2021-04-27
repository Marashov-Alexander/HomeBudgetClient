package models;

import java.sql.Date;

public class AnalyzeInfo {
    public Runnable callback;
    public Date dateFrom;
    public Date dateTo;
    public int type;

    public AnalyzeInfo(Runnable callback, Date dateFrom, Date dateTo, int type) {
        this.callback = callback;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.type = type;
    }
}
