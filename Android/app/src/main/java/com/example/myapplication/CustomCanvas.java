package com.example.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.LinkedList;

public class CustomCanvas extends View {
    private Paint paint = new Paint();
    private LinkedList<Point> points = new LinkedList<>();
    public CustomCanvas(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint.setColor(Color.RED);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(100);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (points.size() != 0)
        if (points.size() == 1) {
            canvas.drawPoint(points.getFirst().x, points.getFirst().y, paint);
        } else {
            boolean first = true;
            float x = 0, y = 0;
            for (Point point : points) {
                if (first) {
                    x = point.x;
                    y = point.y;
                    first = false;
                } else {
                    canvas.drawLine(x, y, point.x, point.y, paint);
                    x = point.x;
                    y = point.y;
                }
            }
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                points.addLast(new Point((int)Math.floor(touchX),(int)Math.floor(touchY)));
                Log.i("[MyAPP]","Pressed on the canvas");
                break;
            case MotionEvent.ACTION_MOVE:
                points.getLast().set((int)Math.floor(touchX),(int)Math.floor(touchY));
                Log.i("[MyAPP]","Moved on canvas to("+touchX+","+touchY+")");
                break;

            default:
                invalidate();
                return true;
        }
        invalidate();
        return true;
    }
    public LinkedList<Point> getPoints(){
        return points;
    }
    public void clearCanvas(){
        points = new LinkedList<>();
        invalidate();
    }
}
