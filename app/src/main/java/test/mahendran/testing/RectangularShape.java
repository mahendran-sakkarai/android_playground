package test.mahendran.testing;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class RectangularShape extends View {
    private static final String TAG = "RectangularShape";
    private final Paint mTableHorizontalLinePaint;
    private final Paint mTableVerticalLinePaint;
    private Paint mLinePaint;
    private int noOfTableDivisions;
    private int mHorizontalTableDistance;
    private int mVerticalTableDistance;

    public RectangularShape(Context context) {
        this(context, null);
    }

    public RectangularShape(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public RectangularShape(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setBackgroundColor(Color.LTGRAY);

        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setColor(Color.MAGENTA);
        mLinePaint.setStrokeWidth(2);

        mTableHorizontalLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setColor(Color.RED);
        mLinePaint.setStrokeWidth(2);

        mTableVerticalLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setColor(Color.BLUE);
        mLinePaint.setStrokeWidth(2);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        noOfTableDivisions = 10;
        mHorizontalTableDistance = h / noOfTableDivisions;
        mVerticalTableDistance = w / noOfTableDivisions;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = 500;
        int height = 500;

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Drawing Vertical Line
        canvas.drawLine(0, 0, 0, 500, mLinePaint);

        // Drawing Horizontal Line
        canvas.drawLine(0, 0, 250, 0, mLinePaint);

        // Drawing a table
        // Drawing Horizontal lines
        Log.e(TAG, "Width: " + getWidth() + " | Measured Width:" + getMeasuredWidth());
        Log.e(TAG, "Height: " + getHeight() + " | Measured Height:" + getMeasuredHeight());
        int nextYPosition = mHorizontalTableDistance;
        for (int i = 0; i < noOfTableDivisions; i++) {
            canvas.drawLine(0, nextYPosition, getWidth(), nextYPosition, mTableHorizontalLinePaint);
            nextYPosition += mHorizontalTableDistance;
        }
        // Drawing Vertical lines
        int nextXPosition = mHorizontalTableDistance;
        for (int i = 0; i < noOfTableDivisions; i++) {
            canvas.drawLine(nextXPosition, 0, nextXPosition, getHeight(), mTableVerticalLinePaint);
            nextXPosition += mVerticalTableDistance;
        }

        // Drawing cross lines from top left to bottom right
        int nextTLBRCrossStartXPosition = 0;
        int nextTLBRCrossStartYPosition = getHeight() - mVerticalTableDistance;
        int nextTLBRCrossEndXPosition = mHorizontalTableDistance;
        int nextTLBRCrossEndYPosition = getHeight();
        for (int i = 0; i < noOfTableDivisions * 2; i++) {
            canvas.drawLine(nextTLBRCrossStartXPosition, nextTLBRCrossStartYPosition, nextTLBRCrossEndXPosition, nextTLBRCrossEndYPosition, mTableVerticalLinePaint);
            nextTLBRCrossStartXPosition = i > noOfTableDivisions ? nextTLBRCrossEndXPosition + mHorizontalTableDistance : nextTLBRCrossEndXPosition;
            nextTLBRCrossStartYPosition = i > noOfTableDivisions ? nextTLBRCrossStartYPosition + mVerticalTableDistance : nextTLBRCrossStartYPosition;
        }
    }
}
