package com.robosoft.liveworkplay.helper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Rahul Kumar Pandey on 01-03-2017.
 */

public class CustomImageView extends ImageView {

    private static float sRadius = 18.0f;

    public CustomImageView(Context context) {
        super(context);
    }

    public CustomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //float sRadius = 36.0f;
        Path clipPath = new Path();
        RectF rect = new RectF(15, 15, this.getWidth(), this.getHeight());
        clipPath.addRoundRect(rect, sRadius, sRadius, Path.Direction.CW);
        canvas.clipPath(clipPath);
        super.onDraw(canvas);
    }
}
