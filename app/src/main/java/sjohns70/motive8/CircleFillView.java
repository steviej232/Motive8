package sjohns70.motive8;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Steven on 9/14/2016.
 */
public class CircleFillView extends View
{
    public static final int MIN_VALUE = 0;
    public static final int MAX_VALUE = 100;

    private PointF center = new PointF();
    private RectF circleRect = new RectF();

    private Path segment = new Path();
    private Paint strokePaint = new Paint();
    private Paint fillPaint = new Paint();

    private int radius;
    private int fillColor;
    private int strokeColor;
    private int value;

    private float strokeWidth;

    public CircleFillView(Context context)
    {
        this(context, null);
    }

    public CircleFillView(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircleFillView, 0, 0);

        try
        {
            fillColor = a.getColor(R.styleable.CircleFillView_fillColor, Color.WHITE);
            strokeColor = a.getColor(R.styleable.CircleFillView_strokeColor, Color.BLACK);
            strokeWidth = a.getFloat(R.styleable.CircleFillView_strokeWidth, 1f);
            value = a.getInteger(R.styleable.CircleFillView_value, 0);
            adjustValue(value);
        }
        finally
        {
            a.recycle();
        }

        fillPaint.setColor(fillColor);
        strokePaint.setColor(strokeColor);
        strokePaint.setStrokeWidth(strokeWidth);
        strokePaint.setStyle(Paint.Style.STROKE);
    }

    /*public void setFillColor(int fillColor) {
        this.fillColor = fillColor;
        fillPaint.setColor(fillColor);
        invalidate();
    }

    public int getFillColor() {
        return fillColor;
    }

    public void setStrokeColor(int strokeColor) {
        this.strokeColor = strokeColor;
        strokePaint.setColor(strokeColor);
        invalidate();
    }

    public int getStrokeColor() {
        return strokeColor;
    }

    public void setStrokeWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
        strokePaint.setStrokeWidth(strokeWidth);
        invalidate();
    }

    public float getStrokeWidth() {
        return strokeWidth;
    }*/

    public void setValue(int value) {
        adjustValue(value);
        setPaths();

        invalidate();
    }

    public int getValue() {
        return value;
    }

    private void adjustValue(int value)  {
        this.value = Math.min(MAX_VALUE, Math.max(MIN_VALUE, value));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        center.x = getWidth() / 2;
        center.y = getHeight() / 2;
        radius = Math.min(getWidth(), getHeight()) / 2 - (int) strokeWidth;
        circleRect.set(center.x - radius, center.y - radius, center.x + radius, center.y + radius);

        setPaths();
    }

    private void setPaths() {
        float y = center.y + radius - (2 * radius * value / 100 - 1);
        float x = center.x - (float) Math.sqrt(Math.pow(radius, 2) - Math.pow(y - center.y, 2));

        float angle = (float) Math.toDegrees(Math.atan((center.y - y) / (x - center.x)));
        float startAngle = 180 - angle;
        float sweepAngle = 2 * angle - 180;

        segment.rewind();
        segment.addArc(circleRect, startAngle, sweepAngle);
        segment.close();
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        Paint heart_outline_paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        heart_outline_paint.setStrokeJoin(Paint.Join.MITER);
        heart_outline_paint.setColor(Color.RED);
        heart_outline_paint.setStrokeWidth(15);
        heart_outline_paint.setStyle(Paint.Style.STROKE);

        float length = 100;
        float x = canvas.getWidth()/2;
        float y = canvas.getHeight()/2;

        canvas.rotate(45, x, y);

        segment.moveTo(x, y);
        segment.lineTo(x-length, y);
        segment.arcTo(new RectF(x-length-(length/2), y-length, x-(length/2), y), 90, 180);
        segment.arcTo(new RectF(x-length, y-length-(length/2), x, y-(length/2)), 180, 180);
        segment.lineTo(x, y);
        segment.close();

        //canvas.drawPath(segment, heart_outline_paint);

        canvas.drawPath(segment, fillPaint);
        //canvas.drawCircle(center.x, center.y, radius, strokePaint);
    }
}
