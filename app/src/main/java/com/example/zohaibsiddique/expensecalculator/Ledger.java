package com.example.zohaibsiddique.expensecalculator;


class Ledger {
    private String id, title, date, startingBalance,  fromDate, toDate;

    public Ledger(String id, String title, String startingBalance, String date, String fromDate, String toDate) {
        this.id = id;
        this.title = title;
        this.startingBalance = startingBalance;
        this.date = date;
        this.fromDate = fromDate;
        this.toDate = toDate;
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

//    public String getStartingBalance() {
//        return startingBalance;
//    }
//
//    public void setStartingBalance(String startingBalance) {
//        this.startingBalance = startingBalance;
//    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

//    public String getFromDate() {
//        return fromDate;
//    }
//
//    public void setFromDate(String fromDate) {
//        this.fromDate = fromDate;
//    }
//
//    public String getToDate() {
//        return toDate;
//    }
//
//    public void setToDate(String toDate) {
//        this.toDate = toDate;
//    }
}