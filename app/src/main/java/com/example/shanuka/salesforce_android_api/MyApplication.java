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

package com.example.shanuka.salesforce_android_api;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;


public class MyApplication extends MultiDexApplication {

    public MyApplication() {
        super();
    }

    @Override
    public void onCreate() {

        super.onCreate();
        //MultiDex.install(this);

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/RobotoCondensed-Regular.ttf")
                .setFontAttrId(R.attr.fontPath).build());

        MultiDex.install(this);
    }


//	public void initLocation(final Context context) {
//
//		Log.d("TestApplication", "onCreate()");
//
//		// output debug to LogCat, with tag LittleFluffyLocationLibrary
//		LocationLibrary.showDebugOutput(true);
//		LocationLibrary.useFineAccuracyForRequests(true);
//
//		try {
//			// in most cases the following initialising code using defaults is
//			// probably sufficient:
//			//
//			// LocationLibrary.initialiseLibrary(getBaseContext(),
//			// "com.your.package.name");
//			//
//			// however for the purposes of the test app, we will request
//			// unrealistically frequent location broadcasts
//			// every 1 minute, and force a location update if there hasn't been
//			// one for 2 minutes.
//			LocationLibrary.initialiseLibrary(context, 60 * 60 * 1000,
//					60 * 60 * 1000, "sg.medicloud");
//		} catch (UnsupportedOperationException ex) {
//			Log.d("TestApplication",
//					"UnsupportedOperationException thrown - the device doesn't have any location providers");
//		}
//	}

}
