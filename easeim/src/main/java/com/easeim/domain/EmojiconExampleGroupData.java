package com.easeim.domain;

import com.easeim.R;
import com.hyphenate.easeui.domain.EaseEmojicon;
import com.hyphenate.easeui.domain.EaseEmojiconGroupEntity;

import java.util.Arrays;

/**
 * 自定义表情
 */
public class EmojiconExampleGroupData {
    
    private static int[] icons = new int[]{
        R.drawable.icon_001_cover,
        R.drawable.icon_002_cover,
        R.drawable.icon_003_cover,
        R.drawable.icon_004_cover,
        R.drawable.icon_005_cover,
        R.drawable.icon_006_cover,
        R.drawable.icon_007_cover,
        R.drawable.icon_008_cover,
        R.drawable.icon_009_cover,
        R.drawable.icon_010_cover,
        R.drawable.icon_011_cover,
        R.drawable.icon_012_cover,
        R.drawable.icon_013_cover,
        R.drawable.icon_014_cover,
        R.drawable.icon_015_cover,
        R.drawable.icon_016_cover,
    };

    private static int[] bigIcons = new int[]{
        R.drawable.icon_001,
        R.drawable.icon_002,
        R.drawable.icon_003,
        R.drawable.icon_004,
        R.drawable.icon_005,
        R.drawable.icon_006,
        R.drawable.icon_007,
        R.drawable.icon_008,
        R.drawable.icon_009,
        R.drawable.icon_010,
        R.drawable.icon_011,
        R.drawable.icon_012,
        R.drawable.icon_013,
        R.drawable.icon_014,
        R.drawable.icon_015,
        R.drawable.icon_016,
    };

    private static String [] iconName = new String[]{
            "OK",
            "放心",
            "恭喜发财",
            "开饭啦",
            "靠谱",
            "把帐算一下",
            "老司机",
            "谁有货",
            "睡觉了",
            "谢谢老板",
            "一路平安",
            "有人吗？",
            "有条子",
            "早",
            "只要钱到位",
            "赚钱啦，赚钱啦"
    };


    private static final EaseEmojiconGroupEntity DATA = createData();

    private static EaseEmojiconGroupEntity createData(){
        EaseEmojiconGroupEntity emojiconGroupEntity = new EaseEmojiconGroupEntity();
        EaseEmojicon[] datas = new EaseEmojicon[icons.length];
        for(int i = 0; i < icons.length; i++){
            datas[i] = new EaseEmojicon(icons[i], null, EaseEmojicon.Type.BIG_EXPRESSION);
            datas[i].setBigIcon(bigIcons[i]);
            //you can replace this to any you want
            datas[i].setName(iconName[i]);
            datas[i].setIdentityCode("em"+ (1000+i+1));
        }
        emojiconGroupEntity.setEmojiconList(Arrays.asList(datas));
        emojiconGroupEntity.setIcon(R.drawable.icon_001_cover);
        emojiconGroupEntity.setType(EaseEmojicon.Type.BIG_EXPRESSION);
        return emojiconGroupEntity;
    }
    
    
    public static EaseEmojiconGroupEntity getData(){
        return DATA;
    }
}
