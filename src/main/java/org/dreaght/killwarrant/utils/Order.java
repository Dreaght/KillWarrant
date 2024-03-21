package org.dreaght.killwarrant.utils;

public class Order {
    private String targetName;
    private String clientName;

    private Integer award;

    public Order(String targetName, String clientName, Integer award) {
        this.targetName = targetName;
        this.clientName = clientName;
        this.award = award;
    }

    public String getTargetName() {
        return this.targetName;
    }

    public void setTargetName(String newTargetName) {
        this.targetName = newTargetName;
    }

    public String getClientName() {
        return this.clientName;
    }

    public void setClientName(String newClientName) {
        this.clientName = newClientName;
    }

    public Integer getAward() {
        return this.award;
    }

    public void setAward(Integer award) {
        this.award = award;
    }
}
