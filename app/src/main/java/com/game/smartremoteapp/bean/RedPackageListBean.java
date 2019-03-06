package com.game.smartremoteapp.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by mi on 2018/12/28.
 */

public class RedPackageListBean implements Serializable{
    private List<RedPackageBean> list;

    public List<RedPackageBean> getList() {
        return list;
    }

    public void setList(List<RedPackageBean> list) {
        this.list = list;
    }
}
