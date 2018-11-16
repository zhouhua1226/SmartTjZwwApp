package com.game.smartremoteapp.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.game.smartremoteapp.R;

/**
 * Created by mi on 2018/11/1.
 */
public class LevelPowerDialog extends Dialog {
    private ImageView iv_level;
    private ImageButton ibClose;
    public LevelPowerDialog(Context context, int themeResId) {
        super(context, themeResId);
    }
    public LevelPowerDialog(Context context) {
        super(context);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_levler_power);
        iv_level = (ImageView) findViewById(R.id.iv_level_desc);
        ibClose = (ImageButton) findViewById(R.id.ib_level_close);
        ibClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
  public void setLevelPowerBg(int icon){
      iv_level.setBackgroundResource(icon);
  }

}