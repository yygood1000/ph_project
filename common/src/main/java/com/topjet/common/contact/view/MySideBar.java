package com.topjet.common.contact.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.topjet.common.R;
import com.topjet.common.config.CMemoryData;
import com.topjet.common.utils.ListUtils;
import com.topjet.common.utils.Logger;

import java.util.HashMap;
import java.util.List;

public class MySideBar extends View {

    // 首字母
    List<String> abcList = null;
    // 根据首字母获取对应的位置
    HashMap<String, Integer> indexPositionMap = new HashMap<String, Integer>();

    OnTouchingLetterChangedListener onTouchingLetterChangedListener;
    // 26个字母
    public String[] b = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
            "S", "T", "U", "V", "W", "X", "Y", "Z", "#"};

    // 这个颜色是按下去颜色
    static final String COLOR = "#FFFFFF";
    private boolean showBkg = false;
    int choose = -1;
    int scrollChoose = -1;
    Paint paint = new Paint();
    Paint rectPaint = new Paint();
    float rectWidth = 0f;
    float rectHeiht = 0f;

    float fontScale = 0f;

    int removeCount = 0;
    int charPosition = -1;

    private TextView mTextDialog;

    public MySideBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        inits(context);
    }

    public MySideBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        inits(context);
    }

    public MySideBar(Context context) {
        super(context);
        inits(context);
    }

    /**
     * 为SideBar设置显示字母的TextView
     *
     * @param mTextDialog
     */
    public void setTextView(TextView mTextDialog) {
        this.mTextDialog = mTextDialog;
    }


    private void inits(Context context) {
        // 设置选中的颜色
        rectPaint.setColor(Color.parseColor("#ffffff"));
        rectWidth = paint.measureText("#");
        Rect rect = new Rect();
        paint.getTextBounds("#", 0, 1, rect);
        rectHeiht = rect.height();
        fontScale = sp2px(context, 12);

    }

    public static int sp2px(Context context, float spValue) {
        float scaleDensity = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * scaleDensity + 0.5f);
    }

    /**
     * 去掉索引不要的字母,只支持去掉一个字母
     *
     * @param charPosition 该字母所在的位置
     *                     共去掉多少个字母
     */
    public void removeUselessChar(int charPosition) {
        this.charPosition = charPosition;
        this.removeCount = 1;
    }

    /**
     * 重写这个方法
     */
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (showBkg) {
            canvas.drawColor(Color.parseColor(COLOR));
        }

        int height = getHeight();
        int width = getWidth();
        int count = b.length;
        int singleHeight = height / (count - removeCount);
        float xRectPos = ((float) width - rectWidth) / 2.0f - rectWidth;
        float xRectPos2 = xRectPos + 3.2f * rectWidth;
        int remove_count = 0;
        for (int i = 0; i < count; i++) {
            if (charPosition == i) {
                remove_count++;
                continue;
            }
            paint.setFakeBoldText(true);
            paint.setAntiAlias(true);
            paint.setTextSize(fontScale);

            // 旁边字母的颜色
            if (CMemoryData.isDriver()) {
                paint.setColor(getResources().getColor(R.color.v3_common_blue));
            } else {
                paint.setColor(getResources().getColor(R.color.v3_common_green));
            }

            float xPos = ((float) width - paint.measureText(b[i])) / 2.0f;
            float yPos = singleHeight * (i - remove_count) + singleHeight;
            if (i == choose) {
                // 字母选中时的颜色
                if (CMemoryData.isDriver()) {
                    paint.setColor(getResources().getColor(R.color.blue_button_end_color_clicked));
                } else {
                    paint.setColor(getResources().getColor(R.color.green_button_start_color_clicked));
                }
                canvas.drawRect(xRectPos, (yPos - rectHeiht - 5.0f - rectHeiht / 2.0f) - 5, xRectPos2,
                        yPos + rectHeiht / 2.0f, rectPaint);
            }
            canvas.drawText(b[i], xPos, yPos, paint);
            paint.reset();
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float y = event.getY();
        final int oldChoose = choose;
        final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
        final int c = (int) (y / getHeight() * b.length);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                showBkg = true;
                if (oldChoose != c && listener != null) {
                    Logger.d("doOnActionDown", "触摸了" + c);
                    doOnActionDown(listener, c);
                }

                break;
            case MotionEvent.ACTION_MOVE:
                if (oldChoose != c && listener != null) {
                    doOnActionDown(listener, c);
                }
                break;
            case MotionEvent.ACTION_UP:
                showBkg = false;
                // choose = -1;
                invalidate();
                if (mTextDialog != null) {
                    mTextDialog.setVisibility(View.INVISIBLE);
                }
                break;
        }
        return true;
    }

    /**
     * listview滚动时候设置
     *
     * @param c
     */
    public void setColorWhenListViewScrolling(int c) {
        if (scrollChoose != c) {
            scrollChoose = c;
            if (ListUtils.isEmpty(abcList)) {
                return;
            }
            String string = abcList.get(c);
            for (int i = c; i < b.length; ++i) {
                if (string.equals(b[i])) {
                    choose = i;
                    invalidate();
                    break;
                }
            }
        }
    }

    private void doOnActionDown(OnTouchingLetterChangedListener listener, int c) {
        if (c >= 0 && c < b.length) {
//			if (indexPositionMap.containsKey(b[c])) {
            Logger.d("doOnActionDown", "点击了");
            listener.onTouchingLetterChanged(b[c]);
            choose = c;
            invalidate();
            if (mTextDialog != null) {
                mTextDialog.setText(b[c]);
                mTextDialog.setVisibility(View.VISIBLE);
            }
//			} else {
//				c = c - 1;
//				doOnActionDown(listener, c);
//			}
        }
    }

    /**
     * 向外公开的方法
     */
    public void setOnTouchingLetterChangedListener(OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
        this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
    }

    /**
     * 接口
     */
    public interface OnTouchingLetterChangedListener {
        void onTouchingLetterChanged(String s);
    }

    /**
     * 请调用其方法，目的是根据listview滑动而使其变化
     */
    public void setIndexList(List<String> abcList, HashMap<String, Integer> indexPositionMap) {
        this.indexPositionMap = indexPositionMap;
        this.abcList = abcList;
        // b = new String[abcList.size()];
        // abcList.toArray(b);
        // invalidate();
    }
}