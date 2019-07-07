package ru.rgs.csvparser.model;

public class UserReq {
    String clientName;
    String contractDate;

    public UserReq() {
    }

    public UserReq(String clientName, String contractDate) {
        this.clientName = clientName;
        this.contractDate = contractDate;
    }
}
