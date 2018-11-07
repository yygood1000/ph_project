package com.topjet.common.base.listener;

import android.util.SparseArray;
import android.view.View;

public abstract class DebounceClickListener implements View.OnClickListener {
	public static long MIN_CLICK_INTERVAL = 1500; // min click interval

	private SparseArray<Long> mClickIntervalMap = new SparseArray<Long>();

	@Override
	public void onClick(View v) {
		synchronized (v) {
			Long lastClickTime = mClickIntervalMap.get(v.getId());
			long currClickTime = System.currentTimeMillis();

			if (lastClickTime == null) {
				onStandardClick(v, currClickTime);
			} else {
				if (currClickTime - lastClickTime.longValue() >= MIN_CLICK_INTERVAL) {
					onStandardClick(v, currClickTime);
				}
			}
		}
	}

	private void onStandardClick(View v, long time) {
		mClickIntervalMap.put(v.getId(), time);
		performClick(v);
	}

	public abstract void performClick(View v);
}
