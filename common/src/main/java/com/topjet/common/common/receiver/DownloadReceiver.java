package com.topjet.common.common.receiver;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;

import com.topjet.common.config.CMemoryData;
import com.topjet.common.utils.Logger;

/**
 * Created by wangshuo on 2015/10/20.
 */
public class DownloadReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent.getAction())) {
			long apkDownloadId = CMemoryData.getApkDownloadId();
			long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

			if (apkDownloadId == downloadId) {
				DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
				validateDownload(context, downloadManager, apkDownloadId);
			}
		}
	}

	private void validateDownload(Context context, DownloadManager downloadManager, long downloadId) {
		Cursor cursor = downloadManager.query(new DownloadManager.Query().setFilterById(downloadId));
		if (cursor.moveToFirst()) {
			int columnStatus = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
			int status = cursor.getInt(columnStatus);
			int columnReason = cursor.getColumnIndex(DownloadManager.COLUMN_REASON);
			int reason = cursor.getInt(columnReason);

			if (DownloadManager.STATUS_SUCCESSFUL == status) {
				jumpToInstallPage(context, downloadManager, downloadId);
			} else if (DownloadManager.STATUS_FAILED == status) {
				logFailed(reason);
			} else if (DownloadManager.STATUS_PAUSED == status) {
				logPaused(reason);
			} else if (DownloadManager.STATUS_PENDING == status) {
				Logger.d("CCC", "STATUS_PENDING");
			} else if (DownloadManager.STATUS_RUNNING == status) {
				Logger.d("CCC", "STATUS_RUNNING");
			}
		}
	}

	private void jumpToInstallPage(Context context, DownloadManager downloadManager, long downloadId) {
		Uri uri;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			uri = downloadManager.getUriForDownloadedFile(downloadId);
		} else {
			try {
				Cursor cursor = downloadManager.query(new DownloadManager.Query().setFilterById(downloadId));
				cursor.moveToFirst();
				String strUri = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
				uri = Uri.parse(strUri);
			} catch (Exception e) {
				uri = null;
			}
		}

		Intent targetIntent = new Intent(Intent.ACTION_VIEW);
		targetIntent.setDataAndType(uri, "application/vnd.android.package-archive");
		targetIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		try {
			context.startActivity(targetIntent);
		} catch (Exception e) {
			Logger.d("", e);
		}

	}

	private void logFailed(int reason) {
		String failedReason = "ERROR_OTHERS";
		switch (reason) {
			case DownloadManager.ERROR_CANNOT_RESUME:
				failedReason = "ERROR_CANNOT_RESUME";
				break;
			case DownloadManager.ERROR_DEVICE_NOT_FOUND:
				failedReason = "ERROR_DEVICE_NOT_FOUND";
				break;
			case DownloadManager.ERROR_FILE_ALREADY_EXISTS:
				failedReason = "ERROR_FILE_ALREADY_EXISTS";
				break;
			case DownloadManager.ERROR_FILE_ERROR:
				failedReason = "ERROR_FILE_ERROR";
				break;
			case DownloadManager.ERROR_HTTP_DATA_ERROR:
				failedReason = "ERROR_HTTP_DATA_ERROR";
				break;
			case DownloadManager.ERROR_INSUFFICIENT_SPACE:
				failedReason = "ERROR_INSUFFICIENT_SPACE";
				break;
			case DownloadManager.ERROR_TOO_MANY_REDIRECTS:
				failedReason = "ERROR_TOO_MANY_REDIRECTS";
				break;
			case DownloadManager.ERROR_UNHANDLED_HTTP_CODE:
				failedReason = "ERROR_UNHANDLED_HTTP_CODE";
				break;
			case DownloadManager.ERROR_UNKNOWN:
				failedReason = "ERROR_UNKNOWN";
				break;
		}
		Logger.d("CCC", "failedReason:" + failedReason);
	}

	private void logPaused(int reason) {
		String pausedReason = "PAUSED_OTHERS";
		switch (reason) {
			case DownloadManager.PAUSED_QUEUED_FOR_WIFI:
				pausedReason = "PAUSED_QUEUED_FOR_WIFI";
				break;
			case DownloadManager.PAUSED_UNKNOWN:
				pausedReason = "PAUSED_UNKNOWN";
				break;
			case DownloadManager.PAUSED_WAITING_FOR_NETWORK:
				pausedReason = "PAUSED_WAITING_FOR_NETWORK";
				break;
			case DownloadManager.PAUSED_WAITING_TO_RETRY:
				pausedReason = "PAUSED_WAITING_TO_RETRY";
				break;
		}
		Logger.d("CCC", "pausedReason:" + pausedReason);
	}

}
