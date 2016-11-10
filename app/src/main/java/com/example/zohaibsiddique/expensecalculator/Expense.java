package com.example.zohaibsiddique.expensecalculator;

class Expense {
    private String id, title, date, value, type;

    Expense(String id, String title, String value, String date, String type) {
        this.id = id;
        this.title = title;
        this.value = value;
        this.date = date;
        this.date = date;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
