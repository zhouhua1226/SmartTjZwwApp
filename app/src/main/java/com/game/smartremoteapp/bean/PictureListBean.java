package com.game.smartremoteapp.bean;

import java.util.List;

/**
 * Created by mi on 2018/10/25.
 */

public class PictureListBean {
    private List<PictureBean> pictureList;

    public List<PictureBean> getPictureList() {
        return pictureList;
    }

    public void setPictureList(List<PictureBean> pictureList) {
        this.pictureList = pictureList;
    }

    public class PictureBean{
        private String MACHINE_TYPE;
        private String doll_name;
        private String nickname;
        private String DOLL_URL;
        private String IMAGE_URL;

        public String getMACHINE_TYPE() {
            return MACHINE_TYPE;
        }

        public void setMACHINE_TYPE(String MACHINE_TYPE) {
            this.MACHINE_TYPE = MACHINE_TYPE;
        }

        public String getDoll_name() {
            return doll_name;
        }

        public void setDoll_name(String doll_name) {
            this.doll_name = doll_name;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getDOLL_URL() {
            return DOLL_URL;
        }

        public void setDOLL_URL(String DOLL_URL) {
            this.DOLL_URL = DOLL_URL;
        }

        public String getIMAGE_URL() {
            return IMAGE_URL;
        }

        public void setIMAGE_URL(String IMAGE_URL) {
            this.IMAGE_URL = IMAGE_URL;
        }
    }
}
