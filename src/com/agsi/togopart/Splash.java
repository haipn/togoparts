package com.agsi.togopart;

import java.net.URL;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

public class Splash extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		
		new DownloadFilesTask().execute(1);
	}

	private class DownloadFilesTask extends AsyncTask<Integer, Integer, Integer> {
		
		protected void onProgressUpdate(Integer... progress) {
		}

		protected void onPostExecute(Integer result) {
			startActivity(new Intent(Splash.this, MainActivity.class));
			finish();
		}

		@Override
		protected Integer doInBackground(Integer... params) {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return 1;
		}

	}
}
