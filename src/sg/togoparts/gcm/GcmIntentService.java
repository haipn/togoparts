/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package sg.togoparts.gcm;

import java.io.InputStream;

import sg.togoparts.BikeShopDetail;
import sg.togoparts.DetailActivity;
import sg.togoparts.FSActivity_Home;
import sg.togoparts.FilterActivity;
import sg.togoparts.R;
import sg.togoparts.SearchResultActivity;
import sg.togoparts.TabsActivityMain;
import sg.togoparts.app.Const;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

/**
 * This {@code IntentService} does the actual handling of the GCM message.
 * {@code GcmBroadcastReceiver} (a {@code WakefulBroadcastReceiver}) holds a
 * partial wake lock for this service while the service does its work. When the
 * service is finished, it calls {@code completeWakefulIntent()} to release the
 * wake lock.
 */
public class GcmIntentService extends IntentService {
	public static final int NOTIFICATION_ID = 1;
	private NotificationManager mNotificationManager;
	NotificationCompat.Builder builder;
	private DisplayImageOptions options;
	private String msg;
	private String title;
	private int mNotificationId;

	public GcmIntentService() {
		super("GcmIntentService");
		options = new DisplayImageOptions.Builder()
				// .showStubImage(R.drawable.stub_image)
				// .showImageForEmptyUri(R.drawable.stub_image)
				.cacheInMemory().cacheOnDisc()
				.bitmapConfig(Bitmap.Config.RGB_565).build();
	}

	public static final String TAG = "GCM Demo";
	private static final int HOME_ID = 0;
	private static final int LIST_ID = 1;
	private static final int SHOP_ID = 2;
	private static final int AD_ID = 3;
	private static final int LINK_ID =4;

	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		// The getMessageType() intent parameter must be the intent you received
		// in your BroadcastReceiver.
		Log.d(TAG, "dskajflsk");
		String messageType = gcm.getMessageType(intent);
		if (!extras.isEmpty()) { // has effect of unparcelling Bundle
			if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
				Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());
				// Post notification of received message.
				sendNotification(extras);
				Log.i(TAG, "Received: " + extras.toString());
			}
		}
		// Release the wake lock provided by the WakefulBroadcastReceiver.
		GcmBroadcastReceiver.completeWakefulIntent(intent);
	}

	// Put the message into a notification and post it.
	// This is just one simple example of what you might choose to do with
	// a GCM message.
	private void sendNotification(Bundle bundle) {
		msg = bundle.getString("msg");
		String loc = bundle.getString("loc");
		title = bundle.getString("title");
		String icon = bundle.getString("ico");
		String name = bundle.getString("name");
		String opt_val = bundle.getString("opt_val");
		PendingIntent pendingIntent;
		Intent notificationIntent = new Intent();

		// notificationIntent.setAction(Intent.ACTION_MAIN);
		// notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
		if (loc.equalsIgnoreCase("home")) {
			notificationIntent.setClass(this, TabsActivityMain.class);
			pendingIntent = PendingIntent.getActivity(this, 0,
					notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			mNotificationId = HOME_ID;
		} else {
			TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
			if (loc.equalsIgnoreCase("list_ads")) {
				notificationIntent.setClass(this, SearchResultActivity.class);
				notificationIntent.putExtra(FilterActivity.PARAM, opt_val);
				stackBuilder.addParentStack(SearchResultActivity.class);
				mNotificationId = LIST_ID;
			} else if (loc.equalsIgnoreCase("shop")) {
				notificationIntent.setClass(this, BikeShopDetail.class);
				notificationIntent.putExtra(Const.SHOP_ID, opt_val);
				stackBuilder.addParentStack(BikeShopDetail.class);
				mNotificationId = SHOP_ID;
			} else if (loc.equalsIgnoreCase("mp_ad")) {
				notificationIntent.setClass(this, DetailActivity.class);
				notificationIntent.putExtra(Const.ADS_ID, opt_val);
				stackBuilder.addParentStack(DetailActivity.class);
				mNotificationId = AD_ID;
			} else if (loc.equalsIgnoreCase("link")) {
				notificationIntent = new Intent(Intent.ACTION_VIEW);
				notificationIntent.setData(Uri.parse(opt_val));
				mNotificationId = LINK_ID;
			}
			stackBuilder.addNextIntent(notificationIntent);
			pendingIntent = stackBuilder.getPendingIntent(0,
					PendingIntent.FLAG_UPDATE_CURRENT);
		}

		// notifiy the notification using NotificationManager
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				GcmIntentService.this).setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle(title).setContentText(msg)
				.setContentIntent(pendingIntent);
		
//		NotificationManager mNotificationManager =
//			    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//			// mId allows you to update the notification later on.
//			mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
		DownloadImageTask download = new DownloadImageTask(mBuilder);
		download.execute(icon);
	}

	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

		private NotificationCompat.Builder builder;

		public DownloadImageTask(NotificationCompat.Builder bmImage) {
			this.builder = bmImage;
		}

		protected void onPreExecute() {

		}

		protected Bitmap doInBackground(String... urls) {
			String urldisplay = urls[0];
			Bitmap mIcon11 = null;
			try {
				InputStream in = new java.net.URL(urldisplay).openStream();
				mIcon11 = BitmapFactory.decodeStream(in);
			} catch (Exception e) {
				Log.e("Error", "image download error");
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return mIcon11;
		}

		protected void onPostExecute(Bitmap result) {
			// set image of your imageview
			builder.setLargeIcon(result);
			final Notification notification = builder.build();
			// default phone settings for notifications
			notification.defaults |= Notification.DEFAULT_VIBRATE;
			notification.defaults |= Notification.DEFAULT_SOUND;
			notification.defaults |= Notification.DEFAULT_LIGHTS;

			// cancel notification after click
			notification.flags |= Notification.FLAG_AUTO_CANCEL
					| Notification.FLAG_SHOW_LIGHTS;
			// show scrolling text on status bar when notification arrives
			notification.tickerText = title + "\n" + msg;
			NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			notificationManager.notify(mNotificationId, notification);
			// close
		}
	}
}
