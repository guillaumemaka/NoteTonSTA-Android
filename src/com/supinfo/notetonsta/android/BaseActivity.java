package com.supinfo.notetonsta.android;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class BaseActivity extends Activity {
	Boolean error = false;
	String message;
	private static final CharSequence ActivityTitle = "Home";
	ListView lv;
	ImageButton barButtonLeft;
	ImageButton barButtonRight;
	TextView titleBar;
	ProgressBar progressIndicator;

	@Override
	public void setContentView(int layoutResID) {
		// TODO Auto-generated method stub
		super.setContentView(layoutResID);
		initializeNavBar();
	}

	private void initializeNavBar() {
		barButtonLeft = (ImageButton) findViewById(R.id.leftBarButton);
		barButtonRight = (ImageButton) findViewById(R.id.rightBarButton);

		titleBar = (TextView) findViewById(R.id.titleBar);
		titleBar.setText(ActivityTitle);

		barButtonLeft.setEnabled(false);
		barButtonLeft.setVisibility(View.INVISIBLE);

		progressIndicator = (ProgressBar) findViewById(R.id.progressIndicator);
		progressIndicator.setVisibility(View.INVISIBLE);
		progressIndicator.setIndeterminate(false);
	}

	protected Boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}
}
