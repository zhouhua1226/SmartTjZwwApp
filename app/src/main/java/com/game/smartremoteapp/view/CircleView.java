package com.game.smartremoteapp.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by mi on 2018/11/28.
 */

public class CircleView extends View {

    //画笔
    private Paint mPaint;

    //圆的半径
    private float mRadius = 50f;

    //圆的圆心的x坐标
    private float pointX = mRadius;

    //圆的圆心的Y坐标
    private float pointY = mRadius;




    public CircleView(Context context) {
        super(context, null);
    }

    //自定义veiw在布局中使用，必须实现的一个构造器
    public CircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //构造一个paint
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //根据圆心的坐标来绘制圆的位置的，而圆心的坐标，我们触摸屏幕的时候被我们修改了
        canvas.drawCircle(pointX, pointY, mRadius, mPaint);
    }
}
