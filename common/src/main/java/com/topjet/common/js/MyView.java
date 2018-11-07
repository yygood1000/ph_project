package com.topjet.common.js;

import android.content.Context;
import android.view.View;

/**
 * 处理控件相关操作
 * @author huangb
 *
 */
public class MyView extends View {

	public MyView(Context context) {
		super(context);
	}
	
	/**
	 * 显示或者隐藏当前界面对象
	 * @param bHide
	 */
	public void hide(boolean bHide){
		if(bHide){
			this.setVisibility(View.GONE);
		}else{
			this.setVisibility(View.VISIBLE);
		}
	}
	
	/**
	 * 禁用或者启用当前界面对象
	 * @param bDisable
	 */
	public void disable(boolean bDisable){		
		this.setClickable(bDisable);
	}	
}
