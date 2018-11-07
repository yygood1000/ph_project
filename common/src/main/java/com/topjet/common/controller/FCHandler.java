package com.topjet.common.controller;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;

import com.topjet.common.App;
import com.topjet.common.R;
import com.topjet.common.config.CPersisData;
import com.topjet.common.utils.FileUtils;
import com.topjet.common.utils.Logger;
import com.topjet.common.utils.PathHelper;
import com.topjet.common.utils.StringUtils;
import com.topjet.common.utils.SystemUtils;
import com.topjet.common.utils.TimeUtils;
import com.topjet.common.utils.Toaster;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class FCHandler implements UncaughtExceptionHandler {

	private static UncaughtExceptionHandler mSystemHandler;

	private HashMap<String, String> mBaseInfos;

	private static final long UNCAUGHT_SLEEP_TIME_MILLS = 1200;

	public static final String TAG = "FCHandler";

	private FCHandler() {
	}

	public static void init() {
		if (mSystemHandler == null) {

			mSystemHandler = Thread.getDefaultUncaughtExceptionHandler();

			FCHandler fcHandler = new FCHandler();

			Thread.setDefaultUncaughtExceptionHandler(fcHandler);
		}

	}

	@Override
	public void uncaughtException(Thread thread, Throwable throwable) {
		Logger.e(TAG, throwable);
		CPersisData.setHasJustCrashed(true);
		if (caughtException(throwable)) {
			SystemClock.sleep(UNCAUGHT_SLEEP_TIME_MILLS);

			SystemUtils.killProcess(App.getInstance());
		} else {
			if (mSystemHandler != null) {
				mSystemHandler.uncaughtException(thread, throwable);
			}
		}
	}

	private boolean caughtException(Throwable throwable) {
		if (throwable == null) {
			return false;
		}

		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				/**
				 *  android.view.ViewRootImpl$CalledFromWrongThreadException:
				 *  Only the original thread that created a view hierarchy can touch its views.
				 *  at android.view.ViewRootImpl.checkThread
				 *  这里显示吐司会报错，在子线程中setText,更新UI
				 */
                new MessageHandle().sendEmptyMessage(0);
				Looper.loop();
			}
		}.start();

		return logToExternalStorage(throwable);
	}

	public HashMap<String, String> getBaseInfos() {
		if (mBaseInfos == null || mBaseInfos.size() == 0) {
			mBaseInfos = new HashMap<String, String>();

			try {
				Context context = App.getInstance();
				PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(),
						PackageManager.GET_ACTIVITIES);

				if (packageInfo != null) {
					mBaseInfos.put("versionName", StringUtils.nullToEmpty(packageInfo.versionName));

					mBaseInfos.put("versionCode", String.valueOf(packageInfo.versionCode));
				}

			} catch (NameNotFoundException e) {
				Logger.e(TAG, e);
			}

			Field[] fields = Build.class.getDeclaredFields();

			for (Field field : fields) {
				try {
					field.setAccessible(true);

					mBaseInfos.put(field.getName(), field.get(null).toString());
				} catch (Exception e) {
					Logger.e(TAG, e);
				}
			}
		}

		return mBaseInfos;
	}

	private boolean logToExternalStorage(Throwable throwable) {
		getBaseInfos();

		StringBuilder sBuilder = new StringBuilder();

		if (mBaseInfos != null && mBaseInfos.size() > 0) {
			for (Map.Entry<String, String> entry : mBaseInfos.entrySet()) {
				sBuilder.append(entry.getKey() + "=" + entry.getValue() + "\n");
			}
			sBuilder.append("\n");
		}

		String strCause = getStrThrowableCause(throwable);
		if (strCause != null) {
			sBuilder.append(strCause);
		}

		//保存错误日志
		CPersisData.setExceptionDescription(sBuilder.toString());

		try {
			String fileName = TimeUtils.getFormatDate(new Date(), TimeUtils.FC_HANDLER_TIME_PATTERN) + ".log";
			String filePath = PathHelper.externalFCLog() + "/" + fileName;

			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				return FileUtils.writeFile(filePath, sBuilder.toString(), true);
			}

		} catch (Exception e) {
			Logger.e(TAG, e);
		}
		return false;
	}

	private String getStrThrowableCause(Throwable throwable) {

		String strCause = null;

		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);

		throwable.printStackTrace(printWriter);
		Throwable causeThrowable = throwable.getCause();
		while (causeThrowable != null) {
			causeThrowable.printStackTrace(printWriter);
			causeThrowable = causeThrowable.getCause();
		}

		strCause = writer.toString();

		try {
			printWriter.close();
			writer.close();
		} catch (IOException e) {
			Logger.e(TAG, e);
		}

		return strCause;
	}

	class MessageHandle extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Toaster.showShortToast(R.string.fc_toast, true);
        }
    }

}