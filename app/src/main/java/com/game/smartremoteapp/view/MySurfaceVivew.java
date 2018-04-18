package com.game.smartremoteapp.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by mi on 2018/3/30.
 */
public class MySurfaceVivew extends SurfaceView implements SurfaceHolder.Callback{
    private SurfaceHolder holder;
    private Bitmap bitmap;

    public MySurfaceVivew(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        holder = this.getHolder();
    }

    private void drawCanvas(Bitmap bitmap){
        Canvas canvas = holder.lockCanvas();
        if(canvas != null){
            canvas.drawBitmap(bitmap, 0, 0, null);
            holder.unlockCanvasAndPost(canvas);
        }
    }

    public void setBitmap(Bitmap bitmap){
        this.bitmap = bitmap;
        this.drawCanvas(bitmap);
    }
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        // TODO Auto-generated method stub
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // TODO Auto-generated method stub
         this.drawCanvas(bitmap);
    }
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub
    }


}
