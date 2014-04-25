package sg.togoparts;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.facebook.AppEventsLogger;

public class Splash extends Activity {
	
	public static final String FACEBOOK_ID = "198306676966429";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);

		new DownloadFilesTask().execute(1);
	}

	@Override
	protected void onResume() {
		AppEventsLogger.activateApp(this, FACEBOOK_ID);
		super.onResume();
	}

	private class DownloadFilesTask extends
			AsyncTask<Integer, Integer, Integer> {

		protected void onProgressUpdate(Integer... progress) {
		}

		protected void onPostExecute(Integer result) {
			startActivity(new Intent(Splash.this, TabsActivityMain.class));
			finish();
		}

		@Override
		protected Integer doInBackground(Integer... params) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return 1;
		}

	}
}
