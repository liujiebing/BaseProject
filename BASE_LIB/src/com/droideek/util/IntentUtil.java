package com.droideek.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

public class IntentUtil {

	public static void startIntent(Context context, Class<?> c) {
		Intent intent = new Intent(context, c);
		context.startActivity(intent);
	}

	public static void startIntent(Context context, Intent intent) {
		context.startActivity(intent);
	}

	public static void startIntentForResult(Fragment frg, Activity context, Class<?> c, int requestCode) {
		Intent intent = new Intent(context, c);
		frg.startActivityForResult(intent, requestCode);
	}

	public static void startIntentForResult(Activity context, Intent intent, int requestCode) {
		context.startActivityForResult(intent, requestCode);
	}
	
	public static void finishActivity(Activity activity) {
		if (activity == null) return;
		activity.finish();
	}

}
