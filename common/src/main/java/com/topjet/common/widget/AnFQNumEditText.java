package com.topjet.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.topjet.common.R;
import com.topjet.common.utils.ResourceUtils;

/**
 * Created by tsj-004 on 2017/9/27.
 * 统计字数的view
 */

public class AnFQNumEditText extends RelativeLayout {
    //类型1(单数类型)：TextView显示总字数，然后根据输入递减.例：100，99，98
    //类型2(百分比类型)：TextView显示总字数和当前输入的字数，例：0/100，1/100，2/100
    public static final String SINGULAR = "Singular";//类型1(单数类型)
    public static final String PERCENTAGE = "Percentage";//类型2(百分比类型)
    private EditText etContent;//文本框
    private TextView tvNum;//字数显示TextView
    private String TYPES = SINGULAR;//类型
    private int MaxNum = 100;//最大字符
    private String text = "";

    public AnFQNumEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.anfq_num_edittext, this, true);
        etContent = (EditText) findViewById(R.id.etContent);
        tvNum = (TextView) findViewById(R.id.tvNum);


        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AnFQNumEditText); // 读取xml styleable，attrs是xml属性的集合

        MaxNum = a.getInt(R.styleable.AnFQNumEditText_maxLength, 0);
        TYPES = a.getString(R.styleable.AnFQNumEditText_type);
        String hint = a.getString(R.styleable.AnFQNumEditText_hint);
        int lines = a.getInteger(R.styleable.AnFQNumEditText_lines, 4);
        int contentTextColor = a.getColor(R.styleable.AnFQNumEditText_contentTextColor, ResourceUtils.getColor(R.color.text_color_222222));
        int contentTextColorHint = a.getColor(R.styleable.AnFQNumEditText_contentTextColorHint, ResourceUtils.getColor(R.color.color_CCCCCC));
//        float contentTextSize = a.getDimension(R.styleable.AnFQNumEditText_contentTextSize, ResourceUtils.getDimension(R.dimen.text_size15));
        int numberTextColor = a.getColor(R.styleable.AnFQNumEditText_numberTextColor, ResourceUtils.getColor(R.color.color_CCCCCC));
//        float numberTextSize = a.getDimension(R.styleable.AnFQNumEditText_numberTextSize, ResourceUtils.getDimension(R.dimen.text_size13));
        a.recycle();

        setEtHint(hint);
        etContent.setLines(lines);
        etContent.setMaxLines(lines);
        etContent.setBackgroundColor(0);
        etContent.setTextColor(contentTextColor);
        etContent.setHintTextColor(contentTextColorHint);
        etContent.setTextSize(15);
        tvNum.setTextColor(numberTextColor);
        tvNum.setTextSize(13);

        show();
    }

    /**
     * 设置显示
     */
    public AnFQNumEditText show() {
        if (TYPES.equals(SINGULAR)) {//类型1
            tvNum.setText(String.valueOf(MaxNum));
        } else if (TYPES.equals(PERCENTAGE)) {//类型2
            tvNum.setText(0 + "/" + MaxNum);
        }
        //设置长度
        etContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(MaxNum)});
        //监听输入
        etContent.addTextChangedListener(mTextWatcher);
        return this;
    }

    /**
     * 设置类型
     *
     * @param type --类型
     * @return
     */
    public AnFQNumEditText setType(String type) {
        TYPES = type;
        return this;
    }

    /**
     * 设置最大字数
     *
     * @param num --字数
     * @return
     */
    public AnFQNumEditText setLength(int num) {
        this.MaxNum = num;
        return this;
    }

    /**
     * 设置文本框的Hint
     * @param str --设置内容
     */
    public AnFQNumEditText setEtHint(String str) {
        etContent.setHint(str);
        return this;
    }

    /**
     * 设置文本框的最小高度
     *
     * @param px --最小高度(单位px)
     * @return
     */
    public AnFQNumEditText setEtMinHeight(int px) {
        etContent.setMinHeight(px);
        return this;
    }


    /**
     * 感觉这个方法是核心方法
     */
    private TextWatcher mTextWatcher = new TextWatcher() {
        private int editStart;
        private int editEnd;

        public void afterTextChanged(Editable s) {
            editStart = etContent.getSelectionStart();
            editEnd = etContent.getSelectionEnd();
            // 先去掉监听器，否则会出现栈溢出
            etContent.removeTextChangedListener(mTextWatcher);
            // 注意这里只能每次都对整个EditText的内容求长度，不能对删除的单个字符求长度
            // 因为是中英文混合，单个字符而言，calculateLength函数都会返回1
            while (calculateLength(s.toString()) > MaxNum) { // 当输入字符个数超过限制的大小时，进行截断操作
                s.delete(editStart - 1, editEnd);
                editStart--;
                editEnd--;
            }
            // 恢复监听器
            etContent.addTextChangedListener(mTextWatcher);
            setLeftCount();
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    };

    /**
     * 刷新剩余输入字数
     */
    private void setLeftCount() {
        if (TYPES.equals(SINGULAR)) {//类型1
            tvNum.setText(String.valueOf((MaxNum - getInputCount())));
        } else if (TYPES.equals(PERCENTAGE)) {//类型2
            tvNum.setText(MaxNum - (MaxNum - getInputCount()) + "/" + MaxNum);
        }

    }

    /**
     * 获取用户输入内容字数
     */
    private long getInputCount() {
        return calculateLength(etContent.getText().toString());
    }

    /**
     * 计算分享内容的字数，一个汉字=两个英文字母，一个中文标点=两个英文标点
     * 注意：该函数的不适用于对单个字符进行计算，因为单个字符四舍五入后都是1
     *
     * @param cs
     * @return
     */
    public static long calculateLength(CharSequence cs) {
        double len = 0;
        for (int i = 0; i < cs.length(); i++) {
            int tmp = (int) cs.charAt(i);
            if (tmp > 0 && tmp < 127) {
                len += 1;
            } else {
                len++;
            }
        }
        return Math.round(len);
    }

    public String getText() {
        if(etContent != null)
        {
            text = etContent.getText().toString();
        }
        return text;
    }

    public void setText(String text) {
        this.text = text;
        if(etContent != null)
        {
            etContent.setText(text);
        }
    }
}
