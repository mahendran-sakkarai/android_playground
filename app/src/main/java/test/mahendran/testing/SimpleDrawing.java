package test.mahendran.testing;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by sakkam2 on 7/3/2017.
 */

public class SimpleDrawing extends View {
    private Paint drawPaint;
    private int paintColor = Color.BLACK;
    private ArrayList<Point> points = new ArrayList<>();
    Path path = new Path();

    public SimpleDrawing(Context context) {
        this(context, null);
    }

    public SimpleDrawing(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleDrawing(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setUpPaint();
    }

    private void setUpPaint() {
        drawPaint = new Paint();
        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(Utils.convertDpToPixel(1, getContext()));
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRoundRect(
                new RectF(0, 0, getWidth(), getHeight()),
                Utils.convertDpToPixel(30, getContext()),
                Utils.convertDpToPixel(30, getContext()),
                drawPaint);

        drawPaint.setStyle(Paint.Style.FILL);
        drawPaint.setColor(Color.WHITE);
        canvas.drawRect(new RectF(Utils.convertDpToPixel(1, getContext()), Utils.convertDpToPixel(1, getContext()), getWidth() - Utils.convertDpToPixel(1, getContext()), getHeight() - Utils.convertDpToPixel(1, getContext())), drawPaint);
        //canvas.drawRoundRect(new RectF(10, 100, 100, 200), 6, 6, drawPaint);
    }
}
