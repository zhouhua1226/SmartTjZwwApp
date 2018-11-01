package com.game.smartremoteapp.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by mi on 2018/9/27.
 */

public class LoginRewardGoldBean implements Serializable {
    private UserBean appUser;

    public UserBean getAppUser() {
        return appUser;
    }

    public void setAppUser(UserBean appUser) {
        this.appUser = appUser;
    }

    private RewardGoldManagerBean RewardGoldManager;

    public RewardGoldManagerBean getRewardGoldManager() {
        return RewardGoldManager;
    }

    public void setRewardGoldManager(RewardGoldManagerBean rewardGoldManager) {
        RewardGoldManager = rewardGoldManager;
    }

    private List<LoginRewardGold> LoginRewardGold;

    public List<LoginRewardGold> getLoginRewardGold() {
        return LoginRewardGold;
    }

    public void setLoginRewardGold(List<LoginRewardGold> loginRewardGold) {
        LoginRewardGold = loginRewardGold;
    }

    public class  LoginRewardGold{
        private String id;
        private String createTime;
        private String tag;
        private String gold;
        private String rewardGold;

        public String getRewardGold() {
            return rewardGold;
        }

        public void setRewardGold(String rewardGold) {
            this.rewardGold = rewardGold;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getGold() {
            return gold;
        }

        public void setGold(String gold) {
            this.gold = gold;
        }
    }

}
