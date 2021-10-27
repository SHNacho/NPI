package com.example.npi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

public class PointsOverlayView extends View {

    PointF[] points;
    private Paint paint;
    Bitmap bm_arrow_left;
    Bitmap bm_arrow_right;

    public PointsOverlayView(Context context) {
        super(context);
        init();
    }

    public PointsOverlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PointsOverlayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public static Bitmap drawableToBitmap (Drawable drawable) {

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    private void init() {
        Drawable arrow_left = getResources().getDrawable(
                R.drawable.baseline_arrow_circle_left_red_400_48dp
        );
        Drawable arrow_right = getResources().getDrawable(
                R.drawable.baseline_arrow_circle_right_red_400_48dp
        );
        paint = new Paint();
        paint.setColor(Color.YELLOW);
        paint.setStyle(Paint.Style.FILL);
        bm_arrow_left = drawableToBitmap(arrow_left);
        bm_arrow_right = drawableToBitmap(arrow_right);
    }

    public void setPoints(PointF[] points) {
        this.points = points;
        invalidate();
    }

    @Override public void draw(Canvas canvas) {
        super.draw(canvas);
        if (points != null && bm_arrow_left != null) {
            PointF inferior_izq = points[0];
            PointF superior_izq = points[1];
            PointF superior_der = points[2];
            PointF inferior_der = new PointF(superior_der.x, inferior_izq.y);

            PointF superior_tr = new PointF((superior_der.x + superior_izq.x) / 2, superior_der.y);
            PointF inferior_tr = new PointF((inferior_der.x + inferior_izq.x) / 2, inferior_der.y);
            PointF izq_tr = new PointF(superior_izq.x, (superior_izq.y + inferior_izq.y) / 2);
            PointF der_tr = new PointF(superior_der.x, (superior_der.y + inferior_der.y) / 2);

            RectF rect = new RectF(izq_tr.x, superior_tr.y, der_tr.x, inferior_tr.y);
            canvas.drawBitmap(bm_arrow_left, null, rect, paint);
        }
    }
}
