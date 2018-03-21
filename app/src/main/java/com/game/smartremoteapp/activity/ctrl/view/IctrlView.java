package com.game.smartremoteapp.activity.ctrl.view;

import java.util.List;

/**
 * Created by zhouh on 2017/9/7.
 */
public interface IctrlView {

    void getTime(int time);
    void getTimeFinish();

    void getUserInfos(List<String> list, boolean is);

    void getRecordErrCode(int code);
    void getRecordSuecss();
    void getRecordAttributetoNet(String time, String name);

    void getPlayerErcErrCode(int code);
    void getPlayerSucess();
    void getVideoPlayConnect();
    void getVideoPlayStart();
    void getVideoStop();
}
