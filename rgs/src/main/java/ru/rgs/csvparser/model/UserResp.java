package ru.rgs.csvparser.model;

import lombok.Data;

@Data
public class UserResp {
    private String status;
    private float scoringValue;

    @Override
    public String toString() {
        if (status.equals("NOT_FOUND"))
            return "не найден";
        return Float.toString(scoringValue);
    }
}
