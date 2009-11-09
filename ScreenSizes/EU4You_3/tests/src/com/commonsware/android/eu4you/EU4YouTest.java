package com.commonsware.android.eu4you;

import android.test.ActivityInstrumentationTestCase2;

/**
 * This is a simple framework for a test of an Application.	See
 * {@link android.test.ApplicationTestCase ApplicationTestCase} for more information on
 * how to write and extend Application tests.
 * <p/>
 * To run this test, you can type:
 * adb shell am instrument -w \
 * -e class com.commonsware.android.eu4you.EU4YouTest \
 * com.commonsware.android.eu4you.tests/android.test.InstrumentationTestRunner
 */
public class EU4YouTest extends ActivityInstrumentationTestCase2<EU4You> {

		public EU4YouTest() {
				super("com.commonsware.android.eu4you", EU4You.class);
		}

}