package ru.rgs.csvparser.model;

import lombok.Data;

@Data
public class UserResp {
    String status;
    int scoringValue;

    @Override
    public String toString() {
        return "UserResp{" +
                "status='" + status + '\'' +
                ", scoringValue=" + scoringValue +
                '}';
    }
}
