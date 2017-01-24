package com.gj.gaojiaohui.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class RoundedImageView extends ImageView {

    //中间值用来设置用户头像的颜色
    public int BACK_DEFAULT = Color.parseColor("#95d3f5");

    //当前的用户的名字
    public String username = "";

    //设置当前的字段内容
    public void addTile(String username) {
        this.username = username;
        invalidate();
    }

    //设置字体颜色的
    public void addTile(String username, int namecolor) {
        this.username = username;
        this.BACK_DEFAULT = namecolor;
        invalidate();
    }

    public RoundedImageView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public RoundedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoundedImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint line = new Paint();
        line.setColor(Color.WHITE);
        line.setTextSize(getWidth() / 2);
        line.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetrics fontMetrics = line.getFontMetrics();

        //计算文字高度
        float fontHeight = fontMetrics.bottom - fontMetrics.top;
        //计算文字baseline
        float textBaseY = getHeight() - (getHeight() - fontHeight) / 2 - fontMetrics.bottom;


        canvas.drawText("", getWidth() / 4, getHeight() / 2 + getWidth() / 4, line);

        int w = getWidth(), h = getHeight();
        //画圆圈的画笔
        Paint circle = new Paint();
        circle.setColor(BACK_DEFAULT);
        circle.setStrokeWidth(w / 2); //设置圆环的宽度
        circle.setAntiAlias(true);  //消除锯齿


        Drawable drawable = getDrawable();
        if (drawable == null) {
            return;
        }
        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }


        if (!"".equals(username) && null != username) {

            //画自定义的圆
            canvas.drawCircle(getWidth() / 2, getWidth() / 2, getWidth() / 2, circle);

            if (username.length() > 0) {

                Rect targetRect = new Rect(50, 50, 1000, 200);

                float strwidth = line.measureText(username.substring(0, 1));
                Paint.FontMetrics fm = line.getFontMetrics();
                float hight = (float) Math.ceil(fm.descent - fm.ascent);

                if (username.substring(0, 1).matches("^[a-zA-Z]*")) {
                    if (username.length() >= 2) {
                        strwidth = line.measureText(username.substring(0, 1));
                        if (username.substring(0, 2).matches("^[a-zA-Z]*")) {
                            canvas.drawText(username.substring(0, 1), (getWidth()) / 2, (float) ((getHeight() - hight) / 2 + hight * 0.7), line);
                        } else {
                            canvas.drawText(username.substring(0, 1), getWidth() / 2, (float) ((getHeight() - hight) / 2 + hight * 0.7), line);
                        }
                    } else {
                        canvas.drawText(username.substring(0, 1), (getWidth()) / 2, (float) ((getHeight() - hight) / 2 + hight * 0.7), line);
                    }
                } else {
                    canvas.drawText(username.substring(0, 1), (getWidth()) / 2, (getHeight()) / 2 + hight / 4, line);
                }
            } else {
                canvas.drawText("", getWidth() / 4, (float) ((getHeight() / 4) * 2.8), line);
            }
        } else {
            BitmapDrawable bd = (BitmapDrawable) drawable;
            Bitmap b = bd.getBitmap();
            Bitmap bitmap = b.copy(Config.ARGB_8888, true);

            Bitmap roundBitmap = getCroppedBitmap(bitmap, w);
            canvas.drawBitmap(roundBitmap, 0, 0, null);
            canvas.drawText("", getWidth() / 4, getHeight() / 2 + getWidth() / 4 - 20, line);
        }
    }

    public static Bitmap getCroppedBitmap(Bitmap bmp, int radius) {
        Bitmap sbmp;
        if (bmp.getWidth() != radius || bmp.getHeight() != radius)
            sbmp = Bitmap.createScaledBitmap(bmp, radius, radius, false);
        else
            sbmp = bmp;
        Bitmap output = Bitmap.createBitmap(sbmp.getWidth(),
                sbmp.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xffa19774;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, sbmp.getWidth(), sbmp.getHeight());

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.parseColor("#BAB399"));
        canvas.drawCircle(sbmp.getWidth() / 2 + 0.7f, sbmp.getHeight() / 2 + 0.7f,
                sbmp.getWidth() / 2 + 0.1f, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(sbmp, rect, rect, paint);

        return output;
    }

    //获取文字的高度的方法
    public int getHigh(float fontSize) {
        Paint paint = new Paint();
        paint.setTextSize(fontSize);
        Paint.FontMetrics fm = paint.getFontMetrics();
        return (int) Math.ceil(fm.descent - fm.ascent);
    }

    //获取文字的宽度的方法
    public float getWidth(String displayText) {
        Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(Color.WHITE);
        float textWidth = mTextPaint.measureText(displayText);
        return textWidth;
    }
}
