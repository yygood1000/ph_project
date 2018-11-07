package com.topjet.common.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import java.util.List;
import java.util.Stack;


public class AppManager {
	private static Stack<Activity> activityStack = new Stack<Activity>();
	private static AppManager instance;

	private AppManager() {
	}

	public static AppManager getInstance() {
		if (instance == null) {
			instance = new AppManager();
		}
		return instance;
	}

	public void addActivity(Activity activity) {
		if (activityStack == null) {
			activityStack = new Stack<Activity>();
		}

		activityStack.add(activity);
	}

	public int getActivityStackSize() {
		if (activityStack == null) {
			activityStack = new Stack<Activity>();
		}
		return activityStack.size();
	}

	/**
	 * Method name: getActivityByClass <BR>
	 * Description: 根据activity的class获得activity栈中的对应对象 <BR>
	 * Remark: <BR>
	 *
	 * @param cls
	 * @return Activity<BR>
	 */
	public Activity getActivityByClass(Class<?> cls) {

		for (Activity activity : activityStack) {
			if (activity.getClass().equals(cls)) {
				return activity;
			}
		}
		return null;
	}

	public Activity currentActivity() {
		if (!activityStack.isEmpty()) {
            return activityStack.lastElement();
		}
		return null;
	}

	public void finishActivity() {
		Activity activity = activityStack.lastElement();
		finishActivity(activity);
	}

	public void finishActivity(Activity activity) {
		if (activity != null) {
			activityStack.remove(activity);
			activity.finish();
			activity = null;
		}
	}

	public Boolean setActivity(Class<?> cls) {
		for (Activity activity : activityStack) {
			if (activity.getClass().equals(cls)) {
				return true;
			}
		}
		return false;
	}

	public void finishActivity(Class<?> cls) {
		Activity tmpActivity = null;
		for (Activity activity : activityStack) {
			if (activity.getClass().equals(cls)) {
				if (activity != null) {
					tmpActivity = activity;
					activity.finish();
					activity = null;
				}
			}
		}
		activityStack.remove(tmpActivity);
	}

	public void finishAllActivity() {
		for (int i = 0, size = activityStack.size(); i < size; i++) {
			if (null != activityStack.get(i)) {
				activityStack.get(i).finish();
			}
		}
		activityStack.clear();
	}


	public void finishAllActivity(Class<?> cls) {
		for (int i = 0, size = activityStack.size(); i < size; i++) {
			if (null != activityStack.get(i)) {
				if (cls != activityStack.get(i).getClass()) {
					activityStack.get(i).finish();
				}
			}
		}
	}

	// if (!activity.isFinishing()) {
	// activity.finish();
	// }

	/**
	 * Method name: finishOtherActivity <BR>
	 * Description: 关闭除当前activity外所有activity <BR>
	 * Remark: <BR>
	 * void<BR>
	 */
	public void finishOtherActivity() {

		for (int i = 0, size = activityStack.size(); i < size - 1; i++) {
			if (null != activityStack.get(i)) {
				activityStack.get(i).finish();
			}
		}
	}

	public void finishOtherActivityByName(List<String> names) {

		for (int i = 0, size = activityStack.size(); i < size; i++) {
			if (null != activityStack.get(i)) {

				String stackExistsName = activityStack.get(i).getClass().getSimpleName();

				if (!(names.contains(stackExistsName))) {
					activityStack.get(i).finish();
				}
			}
		}
	}

	public void AppExit(Context context) {
		try {
			finishAllActivity();
			ActivityManager activityMgr = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
			activityMgr.restartPackage(context.getPackageName());
			System.exit(0);
		} catch (Exception e) {
		}
	}

	/**
	 * 获取栈顶的activity
	 */
	public Activity getTopActivity(){
		return activityStack.lastElement();
	}
}
