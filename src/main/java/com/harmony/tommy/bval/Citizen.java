package com.harmony.tommy.bval;

import com.harmony.tommy.bval.constraints.IDCard;

public class Citizen {

    @IDCard
    private String idCard;

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

}
