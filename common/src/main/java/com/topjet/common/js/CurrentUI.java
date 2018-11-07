package com.topjet.common.js;

import com.topjet.common.utils.Logger;
import com.topjet.common.utils.ResourceUtils;

import android.app.Activity;

/**
 * 返回当前脚本所运行时的界面对象
 * @author huangb
 *
 */
public class CurrentUI {
	
	public Activity mActivity;
	
	public CurrentUI(Activity mActivity){
		this.mActivity = mActivity;		
	}
	
	/**
	 * 根据控件名称反找对应控件
	 * @param strName
	 * @return
	 */
	public MyView getItemByName(String strName){
		try {
			int resId = ResourceUtils.getIdByName(mActivity, "id", strName);	
			
			return (MyView)mActivity.findViewById(resId);
		} catch (Exception e) {
			Logger.i("===MyView===", e.getMessage());
		}
		
		return null;
	}
}
