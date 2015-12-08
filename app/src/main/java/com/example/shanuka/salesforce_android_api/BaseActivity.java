package com.example.shanuka.salesforce_android_api;

import android.content.Context;

import com.octo.android.robospice.Jackson2SpringAndroidSpiceService;
import com.octo.android.robospice.SpiceManager;

import roboguice.activity.RoboAppCompatActivity;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class BaseActivity extends RoboAppCompatActivity {

	protected SpiceManager spiceManager = new SpiceManager(
			Jackson2SpringAndroidSpiceService.class);

	public BaseActivity() {
		super();
	}

	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	}

	@Override
	public void onStart() {
		super.onStart();
		if (!spiceManager.isStarted()) {
			spiceManager.start(this);
		}

	}

	@Override
	public void onStop() {

		if (spiceManager.isStarted()) {
			spiceManager.shouldStop();
		}
		super.onStop();

	}

}