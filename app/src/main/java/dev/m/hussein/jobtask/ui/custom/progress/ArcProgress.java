package dev.m.hussein.jobtask.ui.custom.progress;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import dev.m.hussein.jobtask.R;
import dev.m.hussein.jobtask.ui.custom.Utils;


/**
 * Created by bruce on 11/6/14.
 */
public class ArcProgress extends View {
    @ColorInt protected int defaultColor = Color.parseColor("#1a6085");
    protected final int default_finished_color = Color.WHITE;
    protected final int default_unfinished_color = Color.rgb(72, 106, 176);
    protected final float default_arc_angle = 360 * 0.8f;
    protected final int default_max = 100;
    protected float defaultTextSize = 2.0F;
    protected float defaultStrokeWidth = 2.0F;

    private Paint paint;
    protected Paint bottomTextPaint;
    protected Paint titleTextPaint;
    protected Paint subTitleTextPaint;
    protected Typeface typeface;

    private String bottomText = "";
    private String title = "";
    private String subTitle = "";

    private float strokeWidth = defaultStrokeWidth;

    private float titleTextSize = defaultTextSize;
    private float subTitleTextSize = defaultTextSize;
    private float bottomTextSize = defaultTextSize;

    private int titleTextColor = defaultColor;
    private int subTitleTextColor = defaultColor;
    private int bottomTextColor = defaultColor;

    private int progress = 0;
    private int max = default_max;
    private int finishedStrokeColor = default_finished_color;
    private int unfinishedStrokeColor = default_unfinished_color;

    private float arcAngle = default_arc_angle;

    private float arcBottomHeight;
    private boolean isTitleInCenter = false;






    private  int min_size;

    private static final String INSTANCE_STATE = "saved_instance";
    private static final String INSTANCE_STROKE_WIDTH = "stroke_width";

    private static final String INSTANCE_BOTTOM_TEXT = "bottom_text";
    private static final String INSTANCE_BOTTOM_TEXT_SIZE = "bottom_text_size";
    private static final String INSTANCE_BOTTOM_TEXT_COLOR = "bottom_text_color";

    private static final String INSTANCE_TITLE_TEXT = "title_text";
    private static final String INSTANCE_TITLE_TEXT_SIZE = "title_text_size";
    private static final String INSTANCE_TITLE_TEXT_COLOR = "title_text_color";

    private static final String INSTANCE_SUB_TITLE_TEXT = "sub_title_text";
    private static final String INSTANCE_SUB_TITLE_TEXT_SIZE = "sub_title_text_size";
    private static final String INSTANCE_SUB_TITLE_TEXT_COLOR = "sub_title_text_color";


    private static final String INSTANCE_ARC_ANGLE = "arc_angle";

    private static final String INSTANCE_PROGRESS = "progress";

    private static final String INSTANCE_MAX = "max";

    private static final String INSTANCE_FINISHED_STROKE_COLOR = "finished_stroke_color";
    private static final String INSTANCE_UNFINISHED_STROKE_COLOR = "unfinished_stroke_color";







    private RectF rectF = new RectF();
    private int mWidth;
    private int mHeight;


    public ArcProgress(Context context) {
        this(context, null);
    }

    public ArcProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArcProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        defaultTextSize = Utils.sp2px(getResources(), 18);
        min_size = (int) Utils.dp2px(getResources(), 100);
        defaultStrokeWidth = Utils.dp2px(getResources(), 10);

        TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ArcProgress, defStyleAttr, 0);
        initByAttributes(attributes);
        attributes.recycle();

        initPainters();
    }

    protected void initByAttributes(TypedArray attributes) {
        finishedStrokeColor = attributes.getColor(R.styleable.ArcProgress_arc_finished_color, default_finished_color);
        unfinishedStrokeColor = attributes.getColor(R.styleable.ArcProgress_arc_unfinished_color, default_unfinished_color);

        titleTextColor = attributes.getColor(R.styleable.ArcProgress_arc_title_text_color, defaultColor);
        subTitleTextColor = attributes.getColor(R.styleable.ArcProgress_arc_sub_title_text_color, defaultColor);
        bottomTextColor = attributes.getColor(R.styleable.ArcProgress_arc_bottom_text_color, defaultColor);

        titleTextSize = attributes.getDimension(R.styleable.ArcProgress_arc_title_text_size, defaultTextSize);
        subTitleTextSize= attributes.getDimension(R.styleable.ArcProgress_arc_sub_title_text_size, defaultTextSize);
        bottomTextSize = attributes.getDimension(R.styleable.ArcProgress_arc_bottom_text_size, defaultTextSize);

        arcAngle = attributes.getFloat(R.styleable.ArcProgress_arc_angle, default_arc_angle);
        setMax(attributes.getInt(R.styleable.ArcProgress_arc_max, default_max));
        setProgress(attributes.getInt(R.styleable.ArcProgress_arc_progress, 0));
        strokeWidth = attributes.getDimension(R.styleable.ArcProgress_arc_stroke_width, defaultStrokeWidth);

        title = TextUtils.isEmpty(attributes.getString(R.styleable.ArcProgress_arc_title_text)) ? title : attributes.getString(R.styleable.ArcProgress_arc_title_text);
        subTitle = TextUtils.isEmpty(attributes.getString(R.styleable.ArcProgress_arc_sub_title_text)) ? subTitle: attributes.getString(R.styleable.ArcProgress_arc_sub_title_text);
        bottomText = attributes.getString(R.styleable.ArcProgress_arc_bottom_text);

        isTitleInCenter = attributes.getBoolean(R.styleable.ArcProgress_arc_title_is_in_center , false);
    }

    protected void initPainters() {
        bottomTextPaint = new TextPaint();
        bottomTextPaint.setColor(bottomTextColor);
        bottomTextPaint.setTextSize(bottomTextSize);
        if (typeface != null)
            bottomTextPaint.setTypeface(Typeface.create(typeface, Typeface.BOLD));
        else
            bottomTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
        bottomTextPaint.setAntiAlias(true);


        titleTextPaint= new TextPaint();
        titleTextPaint.setColor(titleTextColor);
        titleTextPaint.setTextSize(titleTextSize);
        if (typeface != null)
            titleTextPaint.setTypeface(Typeface.create(typeface, Typeface.BOLD));
        else
            titleTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
        titleTextPaint.setAntiAlias(true);

        subTitleTextPaint = new TextPaint();
        subTitleTextPaint.setColor(subTitleTextColor);
        subTitleTextPaint.setTextSize(subTitleTextSize);
        if (typeface != null)
            subTitleTextPaint.setTypeface(typeface);
        subTitleTextPaint.setAntiAlias(true);

        paint = new Paint();
        paint.setColor(default_unfinished_color);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(strokeWidth);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    public void invalidate() {
        initPainters();
        super.invalidate();
    }



    public void setTypeface(Typeface typeface) {
        this.typeface = typeface;
        invalidate();
    }
    public Typeface getTypeface() {
        return typeface;
    }

    public String getBottomText() {
        return bottomText;
    }

    public void setBottomText(String bottomText) {
        this.bottomText = bottomText;
        invalidate();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        invalidate();
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
        invalidate();
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
        if (this.progress > getMax()) {
            this.progress %= getMax();
        }
        invalidate();
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        if (max > 0) {
            this.max = max;
            invalidate();
        }
    }

    public float getBottomTextSize() {
        return bottomTextSize;
    }

    public void setBottomTextSize(float bottomTextSize) {
        this.bottomTextSize = bottomTextSize;
        this.invalidate();
    }






















    @Override
    protected int getSuggestedMinimumHeight() {
        return min_size;
    }

    @Override
    protected int getSuggestedMinimumWidth() {
        return min_size;
    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        int mWidth = MeasureSpec.getSize(widthMeasureSpec);
        int mHeight = MeasureSpec.getSize(heightMeasureSpec);
//        mHeight = getDefaultSize(getSuggestedMinimumHeight(),
//                heightMeasureSpec);
//        mWidth = getDefaultSize(getSuggestedMinimumWidth(),
//                widthMeasureSpec);

        if (mWidth == 0) this.mWidth = min_size;
        if (mHeight == 0)

        min_size = Math.min(mHeight , mWidth);
        setMeasuredDimension(min_size ,min_size);

        rectF.set(strokeWidth / 2f, strokeWidth / 2f, min_size - strokeWidth / 2f, min_size - strokeWidth / 2f);
        float radius = min_size / 2f;
        float angle = (360 - arcAngle) / 2f;
        arcBottomHeight = radius * (float) (1 - Math.cos(angle / 180 * Math.PI));

    }




    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float startAngle = 270 - arcAngle / 2f;
        float finishedSweepAngle = progress / (float) getMax() * arcAngle;
        float finishedStartAngle = startAngle;
        paint.setColor(unfinishedStrokeColor);
        canvas.drawArc(rectF, startAngle, arcAngle, false, paint);
        paint.setColor(finishedStrokeColor);
        canvas.drawArc(rectF, finishedStartAngle, finishedSweepAngle, false, paint);



        if (!TextUtils.isEmpty(getBottomText())) {
            float bottomTextBaseline = getHeight() - arcBottomHeight - (bottomTextPaint.descent() + bottomTextPaint.ascent()) / 2;
            canvas.drawText(getBottomText(), (getWidth() - bottomTextPaint.measureText(getBottomText())) / 2.0f, bottomTextBaseline, bottomTextPaint);
        }

        float dx = 2 * strokeWidth ;
        float dy = 0;
        StaticLayout titleStaticLayout = null;
        if (!TextUtils.isEmpty(title)){

            TextPaint textPaint = new TextPaint(titleTextPaint);

            titleStaticLayout = new StaticLayout(title, textPaint, (int) (getWidth() - 4*strokeWidth), Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
            dy = getHeight() - titleStaticLayout.getHeight();
            dy = isTitleInCenter ? (dy * 0.5F):(dy * 0.4F);

            canvas.translate(dx,dy);
            titleStaticLayout.draw(canvas);

        }


        if (!TextUtils.isEmpty(subTitle)){

            TextPaint textPaint = new TextPaint(subTitleTextPaint);

            StaticLayout staticLayout = new StaticLayout(subTitle, textPaint, (int) (getWidth() - 4*strokeWidth), Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
            if (titleStaticLayout != null)
                dy = titleStaticLayout.getHeight() + strokeWidth;


            canvas.translate(0, dy);
            staticLayout.draw(canvas);


        }


        canvas.restore();


    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Bundle bundle = new Bundle();

        bundle.putParcelable(INSTANCE_STATE, super.onSaveInstanceState());
        bundle.putFloat(INSTANCE_STROKE_WIDTH, strokeWidth);

        bundle.putString(INSTANCE_BOTTOM_TEXT , bottomText);
        bundle.putFloat(INSTANCE_BOTTOM_TEXT_SIZE , bottomTextSize);
        bundle.putInt(INSTANCE_BOTTOM_TEXT_COLOR , bottomTextColor);

        bundle.putString(INSTANCE_TITLE_TEXT , title);
        bundle.putFloat(INSTANCE_TITLE_TEXT_SIZE , titleTextSize);
        bundle.putInt(INSTANCE_TITLE_TEXT_COLOR , titleTextColor);

        bundle.putString(INSTANCE_SUB_TITLE_TEXT , subTitle);
        bundle.putFloat(INSTANCE_SUB_TITLE_TEXT_SIZE , subTitleTextSize);
        bundle.putInt(INSTANCE_SUB_TITLE_TEXT_COLOR , subTitleTextColor);

        bundle.putFloat(INSTANCE_ARC_ANGLE , arcAngle);

        bundle.putInt(INSTANCE_PROGRESS , progress);

        bundle.putInt(INSTANCE_MAX , max);

        bundle.putInt(INSTANCE_FINISHED_STROKE_COLOR ,finishedStrokeColor);
        bundle.putInt(INSTANCE_UNFINISHED_STROKE_COLOR ,unfinishedStrokeColor);

        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            final Bundle bundle = (Bundle) state;

            strokeWidth = bundle.getFloat(INSTANCE_STROKE_WIDTH);

            bottomText = bundle.getString(INSTANCE_BOTTOM_TEXT);
            bottomTextSize = bundle.getFloat(INSTANCE_BOTTOM_TEXT_SIZE);
            bottomTextColor = bundle.getInt(INSTANCE_BOTTOM_TEXT_COLOR);


            title = bundle.getString(INSTANCE_TITLE_TEXT);
            titleTextSize = bundle.getFloat(INSTANCE_TITLE_TEXT_SIZE);
            titleTextColor = bundle.getInt(INSTANCE_TITLE_TEXT_COLOR);

            subTitle = bundle.getString(INSTANCE_SUB_TITLE_TEXT);
            subTitleTextSize = bundle.getFloat(INSTANCE_SUB_TITLE_TEXT_SIZE);
            subTitleTextColor = bundle.getInt(INSTANCE_SUB_TITLE_TEXT_COLOR);

            arcAngle = bundle.getFloat(INSTANCE_ARC_ANGLE);

            progress = bundle.getInt(INSTANCE_PROGRESS);
            setProgress(progress);

            max = bundle.getInt(INSTANCE_MAX);
            setMax(max);

            finishedStrokeColor = bundle.getInt(INSTANCE_FINISHED_STROKE_COLOR);
            unfinishedStrokeColor = bundle.getInt(INSTANCE_UNFINISHED_STROKE_COLOR);


            initPainters();
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATE));
            return;
        }
        super.onRestoreInstanceState(state);
    }
}
