package ru.rgs.csvparser.model;

import lombok.Data;

@Data
public class UserReq {
    private String clientName;
    private String contractDate;

    public UserReq() {
    }

    public UserReq(String clientName, String contractDate) {
        this.clientName = clientName;
        this.contractDate = contractDate;
    }
}
