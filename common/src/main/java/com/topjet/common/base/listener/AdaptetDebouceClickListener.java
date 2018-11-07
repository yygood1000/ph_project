package com.topjet.common.base.listener;

import android.util.SparseArray;
import android.view.View;
import android.widget.AdapterView;

public abstract class AdaptetDebouceClickListener implements AdapterView.OnItemClickListener {
	public static long MIN_CLICK_INTERVAL = 2000; // min click interval

	private SparseArray<Long> mClickIntervalMap = new SparseArray<Long>();

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		synchronized (v) {
			Long lastClickTime = mClickIntervalMap.get(v.getId());
			long currClickTime = System.currentTimeMillis();

			if (lastClickTime == null) {
				onStandardClick(v, currClickTime,position);
			} else {
				if (currClickTime - lastClickTime.longValue() >= MIN_CLICK_INTERVAL) {
					onStandardClick(v, currClickTime,position);
				}
			}
		}
	}

	private void onStandardClick(View v, long time,int position) {
		mClickIntervalMap.put(v.getId(), time);
		performClick(v,position);
	}

	public abstract void performClick(View v,int position);
}
