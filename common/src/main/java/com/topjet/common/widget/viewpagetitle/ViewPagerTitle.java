package com.topjet.common.widget.viewpagetitle;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.topjet.common.R;
import com.topjet.common.utils.ResourceUtils;

import java.util.ArrayList;



public class ViewPagerTitle extends HorizontalScrollView {

    private String[] titles;
    private ArrayList<TextView> textViews = new ArrayList<>();
    private OnTextViewClick onTextViewClick;
    private DynamicLine dynamicLine;
    private ViewPager viewPager;
    private MyOnPageChangeListener onPageChangeListener;
    private int margin;
    private LinearLayout.LayoutParams contentParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    private LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    private float defaultTextSize;
    private float selectedTextSize;
    private int defaultTextColor;
    private int shaderColorStart;
    private float lineHeight, lineWidth;
    private int shaderColorEnd;
    private float textPadding; //文字填充
    private int oldIndex = 0;

    public void setMargin(int margin) {
        this.margin = margin;
    }

    public void setDefaultTextSize(float defaultTextSize) {
        this.defaultTextSize = defaultTextSize;
    }

    public void setSelectedTextSize(float selectedTextSize) {
        this.selectedTextSize = selectedTextSize;
    }

    public void setDefaultTextColor(int defaultTextColor) {
        this.defaultTextColor = defaultTextColor;
    }

    public void setSelectedTextColor(int selectedTextColor) {
        this.selectedTextColor = selectedTextColor;
    }

    public void setAllTextViewLength(int allTextViewLength) {
        this.allTextViewLength = allTextViewLength;
    }

    public void setBackgroundContentColor(int backgroundContentColor) {
        this.backgroundColor = backgroundContentColor;
    }

    public void setItemMargins(float itemMargins) {
        this.itemMargins = itemMargins;
    }

    private int selectedTextColor;
    private int allTextViewLength;
    private int backgroundColor;
    private float itemMargins;


    public ViewPagerTitle(Context context) {
        this(context, null);
    }

    public ViewPagerTitle(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewPagerTitle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttributeSet(context, attrs);
    }

    private void initAttributeSet(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ViewPagerTitle);
        defaultTextColor = array.getColor(R.styleable.ViewPagerTitle_defaultTextViewColor, Color.GRAY);
        selectedTextColor = array.getColor(R.styleable.ViewPagerTitle_selectedTextViewColor, Color.BLACK);
        defaultTextSize = array.getDimension(R.styleable.ViewPagerTitle_defaultTextViewSize, 13);
        selectedTextSize = array.getDimension(R.styleable.ViewPagerTitle_defaultTextViewSize, 13);
        backgroundColor = array.getColor(R.styleable.ViewPagerTitle_background_content_color, Color.WHITE);
        itemMargins = array.getDimension(R.styleable.ViewPagerTitle_item_margins, 30);

        shaderColorStart = array.getColor(R.styleable.ViewPagerTitle_line_start_color, Color.parseColor("#ffc125"));
        shaderColorEnd = array.getColor(R.styleable.ViewPagerTitle_line_end_color, Color.parseColor("#ff4500"));
        lineHeight = array.getDimension(R.styleable.ViewPagerTitle_line_height, 20);
        lineWidth = array.getDimension(R.styleable.ViewPagerTitle_line_width, 28);

        textPadding = array.getDimension(R.styleable.ViewPagerTitle_text_padding, 9);

        array.recycle();
    }


    /**
     * 初始化时，调用这个方法。ViewPager需要设置Adapter，且titles的数据长度需要与Adapter中的数据长度一置。
     * @param titles 标题1、标题2 etc
     * @param viewPager
     * @param defaultIndex 默认选择的第几个页面
     */
    public void initData(String[] titles, ViewPager viewPager, int defaultIndex) {
        this.titles = titles;
        this.viewPager = viewPager;
        createDynamicLine();
        createTextViews(titles);

        setDefaultIndex(defaultIndex);
        checkIndex(defaultIndex);

        setViewPagerListener();
    }

    public void setViewPagerListener(){
        int fixLeftDis = getFixLeftDis();
        onPageChangeListener = new MyOnPageChangeListener(getContext(), viewPager, dynamicLine, this, allTextViewLength, margin, fixLeftDis);
        if(viewPager != null) {
            viewPager.addOnPageChangeListener(onPageChangeListener);
        }
    }

    /**
     * 设置文本显示
     * @param titles
     */
    public void setTitles(String[] titles){
        if(textViews!=null && textViews.size() == titles.length){
            for (int i=0; i<titles.length; i++){
                textViews.get(i).setText(titles[i]);
            }
        }

    }
    public void initData(String[] titles, int defaultIndex){
        initData(titles, null, defaultIndex);
    }

    public void initData(int[] titlesResourceId, int defaultIndex){
        String titleStr[] = new String[titlesResourceId.length];
        for(int i = 0; i < titlesResourceId.length; i++) {
            titleStr[i] = ResourceUtils.getString(titlesResourceId[i]);
        }
        initData(titleStr, null, defaultIndex);
    }

    private int getFixLeftDis() {
        TextView textView = new TextView(getContext());
        textView.setTextSize(defaultTextSize);
        textView.setText(titles[0]);
        float defaultTextSize = Tool.getTextViewLength(textView);
        textView.setTextSize(selectedTextSize);
        float selectTextSize = Tool.getTextViewLength(textView);
        return (int) (selectTextSize - defaultTextSize) / 2;
    }

    public ArrayList<TextView> getTextView() {
        return textViews;
    }


    private void createDynamicLine() {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) lineHeight);
        dynamicLine = new DynamicLine(getContext(), shaderColorStart, shaderColorEnd, lineHeight, lineWidth);
        params.addRule(RelativeLayout.CENTER_IN_PARENT,RelativeLayout.TRUE);
        dynamicLine.setLayoutParams(params);
    }


    private void createTextViews(String[] titles) {
        removeAllViews();
        LinearLayout contentLl = new LinearLayout(getContext());
        contentLl.setBackgroundColor(backgroundColor);
        contentLl.setLayoutParams(contentParams);
        contentLl.setOrientation(LinearLayout.VERTICAL);
        addView(contentLl);


        LinearLayout textViewLl = new LinearLayout(getContext());
        textViewLl.setLayoutParams(contentParams);
        textViewLl.setOrientation(LinearLayout.HORIZONTAL);

        margin = getTextViewMargins(titles);

        // 设置上下左右填充
        int padding = (int) textPadding;
        textViewParams.setMargins(margin, padding, margin, padding);

        for (int i = 0; i < titles.length; i++) {
            TextView textView = new TextView(getContext());
            textView.setText(titles[i]);
            textView.setTextColor(Color.GRAY);
            textView.setTextSize(defaultTextSize);
            textView.setLayoutParams(textViewParams);
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
            textView.setOnClickListener(onClickListener);
            textView.setTag(i);
            textViews.add(textView);
            textViewLl.addView(textView);
        }
        contentLl.addView(textViewLl);

        //加灰线
        RelativeLayout.LayoutParams rlLineParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams lineParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
        RelativeLayout rlLine = new RelativeLayout(getContext());
        rlLine.setLayoutParams(rlLineParams);
        View line = new View(getContext());
        line.setBackgroundColor(Color.parseColor("#dddddd"));
        lineParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,RelativeLayout.TRUE);
        line.setLayoutParams(lineParams);
        rlLine.addView(line);
        rlLine.addView(dynamicLine);

        contentLl.addView(rlLine);
    }

    private int getTextViewMargins(String[] titles) {
        float countLength = 0;
        TextView textView = new TextView(getContext());
        textView.setTextSize(defaultTextSize);
        TextPaint paint = textView.getPaint();


        for (int i = 0; i < titles.length; i++) {
            countLength = countLength + itemMargins + paint.measureText(titles[i]) + itemMargins;
        }
        int screenWidth = Tool.getScreenWidth(getContext());

        if (countLength <= screenWidth) {
            allTextViewLength = screenWidth;
            return (screenWidth / titles.length - (int) paint.measureText(titles[0])) / 2;
        } else {
            allTextViewLength = (int) countLength;
            return (int) itemMargins;
        }
    }


    private OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            int index = (int) v.getTag();
            setCurrentItem(index);
            if(viewPager == null) {
               checkIndex(index);
            } else {
                viewPager.setCurrentItem((int) v.getTag());
            }
            if (onTextViewClick != null) {
                onTextViewClick.textViewClick((TextView) v, (int) v.getTag());
            }
            oldIndex = index;

        }
    };

    /**
     * 点击的滑动动画
     * @param index 点击按钮的位置
     *              只有当viewpager为空才生效，这个动画
     */
    private void checkIndex(final int index){
        final int everyLength = allTextViewLength/titles.length;
        final float lineWidth = Tool.getTextViewLength(textViews.get(0));
        float w = index * everyLength + margin + getFixLeftDis();
        float oldW = oldIndex * everyLength + margin + getFixLeftDis();
        final ValueAnimator valueAnimator = ValueAnimator.ofFloat(oldW, w);
        int duration = 500;
        valueAnimator.setDuration(duration);
        valueAnimator.setRepeatCount(0);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float)animation.getAnimatedValue();
                dynamicLine.updateView(animatedValue, animatedValue + lineWidth);
            }
        });
        valueAnimator.start();
    }
    public void setDefaultIndex(int index) {
        setCurrentItem(index);
    }

    public void setCurrentItem(int index) {
        for (int i = 0; i < textViews.size(); i++) {
            if (i == index) {
                textViews.get(i).setTextColor(selectedTextColor);
                textViews.get(i).setTextSize(selectedTextSize);
            } else {
                textViews.get(i).setTextColor(defaultTextColor);
                textViews.get(i).setTextSize(defaultTextSize);
            }
        }
    }

    public interface OnTextViewClick {
        void textViewClick(TextView textView, int index);
    }

    public void setOnTextViewClickListener(OnTextViewClick onTextViewClick) {
        this.onTextViewClick = onTextViewClick;
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(viewPager != null) {
            viewPager.removeOnPageChangeListener(onPageChangeListener);
        }
    }


}
