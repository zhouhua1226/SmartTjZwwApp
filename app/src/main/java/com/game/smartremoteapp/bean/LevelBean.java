package com.game.smartremoteapp.bean;

/**
 * Created by mi on 2018/11/6.
 */

public class LevelBean {
    private int level;
    private  float percentage;
    private float residualValue ;

    public float getPercentage() {
        return percentage;
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public float getResidualValue() {
        return residualValue;
    }

    public void setResidualValue(float residualValue) {
        this.residualValue = residualValue;
    }
}
