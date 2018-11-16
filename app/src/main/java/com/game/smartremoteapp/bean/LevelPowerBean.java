package com.game.smartremoteapp.bean;

/**
 * Created by mi on 2018/11/1.
 */

public class LevelPowerBean {
    public int icon;
    public String levelPowerTitle;
    public int mlevel;
    public LevelPowerBean(int icon, String levelPowerTitle,int mlevel) {
        this.icon = icon;
        this.levelPowerTitle = levelPowerTitle;
        this.mlevel = mlevel;
    }
}
