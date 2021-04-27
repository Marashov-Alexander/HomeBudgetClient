package models;

public class Article implements Model {
    public Integer id;
    public String name;

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public String getRowsString() {
        return "name";
    }

    @Override
    public String getValuesString() {
        return "'%s'".formatted(name);
    }

    @Override
    public String getSetString() {
        return "name='%s'".formatted(name);
    }

    public Article(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
