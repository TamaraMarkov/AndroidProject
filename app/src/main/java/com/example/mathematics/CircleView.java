package com.example.mathematics;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.ColorInt;

import java.util.ArrayList;

public class CircleView extends View {
    // Slices start at 12 o'clock
    protected static final int START_ANGLE_OFFSET = 0;
    private int selectedColor = Color.GRAY;

    // Helpers
    private float radius;
    private RectF contentBounds;
    private ArrayList<Slice> slices = new ArrayList<>();
    private int numberOfSlices = 0;

    // Drawing helpers
    private Paint strokePaint;


    public CircleView(Context context) {
        super(context);
        init();
    }

    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        contentBounds = new RectF();


        strokePaint = new Paint();
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setStrokeWidth(5);
        slices.add(new Slice(Color.BLUE));
        slices.add(new Slice(Color.RED));
        slices.add(new Slice(Color.YELLOW));

    }

    @Override
    protected void onDraw(Canvas canvas) {
//        contentBounds = getCenteredSquareBounds(chartRadius, contentBounds);
//        canvas.drawCircle(contentBounds.centerX(), contentBounds.centerY(), chartRadius, paint);
        drawSlices(canvas);
    }

    private void drawSlices(Canvas canvas) {
        float sliceStartAngle = START_ANGLE_OFFSET;
        Path slicePath = new Path();
        Paint slicePaint = new Paint();
        slicePaint.setStyle(Paint.Style.FILL);
        float sliceAngle = 360f / slices.size();

        for (Slice slice : slices) {
            slicePath = PathUtils.getSolidArcPath(
                    slicePath, contentBounds,
                    sliceStartAngle, sliceAngle);
            slicePaint.setColor(slice.getColor());
            canvas.drawPath(slicePath, slicePaint);
            canvas.drawPath(slicePath, strokePaint);
            sliceStartAngle += sliceAngle;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // Finding radius of largest circle area inside View.
        radius = Math.min(
                w - getPaddingLeft() - getPaddingRight(),
                h - getPaddingTop() - getPaddingBottom()) / 2f;
        getCenteredSquareBounds(radius * 2, contentBounds);

    }

    protected RectF getCenteredSquareBounds(float innerCircleRadius, RectF bounds) {
        bounds.left = getPaddingLeft()
                + (getWidth() - getPaddingLeft() - getPaddingRight() - innerCircleRadius) / 2;
        bounds.top = getPaddingTop()
                + (getHeight() - getPaddingTop() - getPaddingBottom() - innerCircleRadius) / 2;
        bounds.right = getWidth() - getPaddingRight()
                - (getWidth() - getPaddingLeft() - getPaddingRight() - innerCircleRadius) / 2;
        bounds.bottom = getHeight() - getPaddingBottom()
                - (getHeight() - getPaddingTop() - getPaddingBottom() - innerCircleRadius) / 2;
        return bounds;
    }

    /**
     * Fetches angle relative to screen centre point
     * where 3 O'Clock is 0 and 12 O'Clock is 270 degrees
     *
     * @return angle in degress from 0-360.
     */
    public double getAngle(float x, float y) {
        double dx = x - contentBounds.centerX();
        // Minus to correct for coord re-mapping
        double dy = -(y - contentBounds.centerY());

        double inRads = Math.atan2(dy, dx);

        // We need to map to coord system when 0 degree is at 3 O'clock, 270 at 12 O'clock
        if (inRads < 0)
            inRads = Math.abs(inRads);
        else
            inRads = 2 * Math.PI - inRads;

        return Math.toDegrees(inRads);
    }

    public void setTotalSlices(int number) {
        numberOfSlices = number;
        slices.clear();
        for (int i = 0; i < number; i++) {
            slices.add(new Slice(Color.GRAY));

        }
        invalidate();

    }

    public void setSelectedColor(@ColorInt int color) {
        selectedColor = color;

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        if (event.getAction() == MotionEvent.ACTION_UP) {
            double angle = getAngle(x, y);
            double sliceAngle = 360 / numberOfSlices;
            int position = (int) (angle / sliceAngle);
            slices.get(position).setColor(selectedColor);

        }
        invalidate();
        return true;
    }

}

