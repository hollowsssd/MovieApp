package com.example.movieapp.Adapters;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;


public class TopCrop extends androidx.appcompat.widget.AppCompatImageView{
    public TopCrop(Context context) {
        super(context);
        setScaleType(ScaleType.MATRIX);
    }

    public TopCrop(Context context, AttributeSet attrs) {
        super(context, attrs);
        setScaleType(ScaleType.MATRIX);
    }

    @Override
    protected boolean setFrame(int l, int t, int r, int b) {
        Drawable drawable = getDrawable();
        if (drawable != null) {
            float scale;

            int viewWidth = getWidth();
            int viewHeight = getHeight();
            int drawableWidth = drawable.getIntrinsicWidth();
            int drawableHeight = drawable.getIntrinsicHeight();

            scale = Math.max((float) viewWidth / (float) drawableWidth,
                    (float) viewHeight / (float) drawableHeight);

            Matrix matrix = new Matrix();
            matrix.setScale(scale, scale);
            matrix.postTranslate(0, 0); // giữ ảnh ở top

            setImageMatrix(matrix);
        }

        return super.setFrame(l, t, r, b);
    }



}
