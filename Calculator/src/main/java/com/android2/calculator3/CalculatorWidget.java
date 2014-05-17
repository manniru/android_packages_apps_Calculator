package com.android2.calculator3;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.RemoteViews;

import com.android2.calculator3.BaseModule.Mode;

import org.javia.arity.SyntaxException;

public class CalculatorWidget extends AppWidgetProvider {
	public final static String PREFERENCE_WIDGET_PREAMBLE = "com.android2.calculator3.CALC_WIDGET_VALUE_";
	public static final String DIGIT_0 = "com.android2.calculator3.0";
	public static final String DIGIT_1 = "com.android2.calculator3.1";
	public static final String DIGIT_2 = "com.android2.calculator3.2";
	public static final String DIGIT_3 = "com.android2.calculator3.3";
	public static final String DIGIT_4 = "com.android2.calculator3.4";
	public static final String DIGIT_5 = "com.android2.calculator3.5";
	public static final String DIGIT_6 = "com.android2.calculator3.6";
	public static final String DIGIT_7 = "com.android2.calculator3.7";
	public static final String DIGIT_8 = "com.android2.calculator3.8";
	public static final String DIGIT_9 = "com.android2.calculator3.9";
	public static final String DOT = "com.android2.calculator3.dot";
	public static final String PLUS = "com.android2.calculator3.plus";
	public static final String MINUS = "com.android2.calculator3.minus";
	public static final String MUL = "com.android2.calculator3.mul";
	public static final String DIV = "com.android2.calculator3.div";
	public static final String EQUALS = "com.android2.calculator3.equals";
	public static final String CLR = "com.android2.calculator3.clear";
	public static final String DEL = "com.android2.calculator3.delete";
	public static final String SHOW_CLEAR = "com.android2.calculator3.show_clear";

	private boolean mClearText = false;

	private static String getValue(Context context, int appWidgetId) {
		return PreferenceManager.getDefaultSharedPreferences(context).getString(PREFERENCE_WIDGET_PREAMBLE + appWidgetId, "");
	}

	private static void setValue(Context context, int appWidgetId, String newValue) {
		PreferenceManager.getDefaultSharedPreferences(context).edit().putString(PREFERENCE_WIDGET_PREAMBLE + appWidgetId, newValue).commit();
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		for (int appWidgetID : appWidgetIds) {
			updateAppWidget(context, appWidgetManager, appWidgetID);
		}
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 0);
		String value = getValue(context, appWidgetId);
		if (value.equals(context.getResources().getString(R.string.error))) value = "";
		mClearText = intent.getBooleanExtra(SHOW_CLEAR, false);

		if (intent.getAction().equals(DIGIT_0)) {
			if (mClearText) {
				value = "";
				mClearText = false;
			}
			value += "0";
		} else if (intent.getAction().equals(DIGIT_1)) {
			if (mClearText) {
				value = "";
				mClearText = false;
			}
			value += "1";
		} else if (intent.getAction().equals(DIGIT_2)) {
			if (mClearText) {
				value = "";
				mClearText = false;
			}
			value += "2";
		} else if (intent.getAction().equals(DIGIT_3)) {
			if (mClearText) {
				value = "";
				mClearText = false;
			}
			value += "3";
		} else if (intent.getAction().equals(DIGIT_4)) {
			if (mClearText) {
				value = "";
				mClearText = false;
			}
			value += "4";
		} else if (intent.getAction().equals(DIGIT_5)) {
			if (mClearText) {
				value = "";
				mClearText = false;
			}
			value += "5";
		} else if (intent.getAction().equals(DIGIT_6)) {
			if (mClearText) {
				value = "";
				mClearText = false;
			}
			value += "6";
		} else if (intent.getAction().equals(DIGIT_7)) {
			if (mClearText) {
				value = "";
				mClearText = false;
			}
			value += "7";
		} else if (intent.getAction().equals(DIGIT_8)) {
			if (mClearText) {
				value = "";
				mClearText = false;
			}
			value += "8";
		} else if (intent.getAction().equals(DIGIT_9)) {
			if (mClearText) {
				value = "";
				mClearText = false;
			}
			value += "9";
		} else if (intent.getAction().equals(DOT)) {
			if (mClearText) {
				value = "";
				mClearText = false;
			}
			value += context.getResources().getString(R.string.dot);
		} else if (intent.getAction().equals(DIV)) {
			value += context.getResources().getString(R.string.div);
		} else if (intent.getAction().equals(MUL)) {
			value += context.getResources().getString(R.string.mul);
		} else if (intent.getAction().equals(MINUS)) {
			value += context.getResources().getString(R.string.minus);
		} else if (intent.getAction().equals(PLUS)) {
			value += context.getResources().getString(R.string.plus);
		} else if (intent.getAction().equals(EQUALS)) {
			if (mClearText) {
				value = "";
				mClearText = false;
			} else {
				mClearText = true;
			}
			final String input = value;
			if (input.isEmpty()) return;

			final Logic logic = new Logic(context);
			logic.setLineLength(7);

			try {
				value = logic.evaluate(input);
			} catch (SyntaxException e) {
				value = context.getResources().getString(R.string.error);
			}

			// Try to save it to history
			if (!value.equals(context.getResources().getString(R.string.error))) {
				final Persist persist = new Persist(context);
				persist.load();
				if (persist.getMode() == null) persist.setMode(Mode.DECIMAL);
				final History history = persist.mHistory;
				history.enter(input, value);
				persist.save();
			}
		} else if (intent.getAction().equals(CLR)) {
			value = "";
		} else if (intent.getAction().equals(DEL)) {
			if (value.length() > 0) value = value.substring(0, value.length() - 1);
		}
		setValue(context, appWidgetId, value);

		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
		int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, CalculatorWidget.class));
		for (int appWidgetID : appWidgetIds) {
			updateAppWidget(context, appWidgetManager, appWidgetID);
		}
		super.onReceive(context, intent);
	}

	private void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);

		String value = getValue(context, appWidgetId);

		if (CalculatorSettings.digitGrouping(context)) {
			final Logic logic = new Logic(context, null);
			BaseModule bm = logic.getBaseModule();
			value = bm.groupSentence(value, value.length());
			value = value.replace(String.valueOf(BaseModule.SELECTION_HANDLE), "");
		}

		int displayId = android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.JELLY_BEAN_MR1 ? R.id.display_long_clickable : R.id.display;

		remoteViews.setViewVisibility(R.id.background, CalculatorSettings.showWidgetBackground(context) ? View.VISIBLE : View.GONE);
		remoteViews.setViewVisibility(displayId, View.VISIBLE);
		remoteViews.setTextViewText(displayId, value);
		remoteViews.setTextViewText(R.id.display, value);
		remoteViews.setViewVisibility(R.id.delete, mClearText ? View.GONE : View.VISIBLE);
		remoteViews.setViewVisibility(R.id.clear, mClearText ? View.VISIBLE : View.GONE);
		setOnClickListeners(context, appWidgetId, remoteViews);

		try {
			appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
		} catch (Exception e) {
		}
	}

	private void setOnClickListeners(Context context, int appWidgetId, RemoteViews remoteViews) {
		final Intent intent = new Intent(context, CalculatorWidget.class);
		intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
		intent.putExtra(SHOW_CLEAR, mClearText);

		// The pending intent request code must be unique
		// Not just for these 17 buttons, but for each widget as well
		// Painful T_T Right?
		// So take the id and shift it over 5 bits (enough to store our 17
		// values)
		int shiftedAppWidgetId = appWidgetId << 5;
		// And add our button values (0-16)

		intent.setAction(DIGIT_0);
		remoteViews.setOnClickPendingIntent(R.id.digit0, PendingIntent.getBroadcast(context, shiftedAppWidgetId + 0, intent, 0));

		intent.setAction(DIGIT_1);
		remoteViews.setOnClickPendingIntent(R.id.digit1, PendingIntent.getBroadcast(context, shiftedAppWidgetId + 1, intent, 0));

		intent.setAction(DIGIT_2);
		remoteViews.setOnClickPendingIntent(R.id.digit2, PendingIntent.getBroadcast(context, shiftedAppWidgetId + 2, intent, 0));

		intent.setAction(DIGIT_3);
		remoteViews.setOnClickPendingIntent(R.id.digit3, PendingIntent.getBroadcast(context, shiftedAppWidgetId + 3, intent, 0));

		intent.setAction(DIGIT_4);
		remoteViews.setOnClickPendingIntent(R.id.digit4, PendingIntent.getBroadcast(context, shiftedAppWidgetId + 4, intent, 0));

		intent.setAction(DIGIT_5);
		remoteViews.setOnClickPendingIntent(R.id.digit5, PendingIntent.getBroadcast(context, shiftedAppWidgetId + 5, intent, 0));

		intent.setAction(DIGIT_6);
		remoteViews.setOnClickPendingIntent(R.id.digit6, PendingIntent.getBroadcast(context, shiftedAppWidgetId + 6, intent, 0));

		intent.setAction(DIGIT_7);
		remoteViews.setOnClickPendingIntent(R.id.digit7, PendingIntent.getBroadcast(context, shiftedAppWidgetId + 7, intent, 0));

		intent.setAction(DIGIT_8);
		remoteViews.setOnClickPendingIntent(R.id.digit8, PendingIntent.getBroadcast(context, shiftedAppWidgetId + 8, intent, 0));

		intent.setAction(DIGIT_9);
		remoteViews.setOnClickPendingIntent(R.id.digit9, PendingIntent.getBroadcast(context, shiftedAppWidgetId + 9, intent, 0));

		intent.setAction(DOT);
		remoteViews.setOnClickPendingIntent(R.id.dot, PendingIntent.getBroadcast(context, shiftedAppWidgetId + 10, intent, 0));

		intent.setAction(DIV);
		remoteViews.setOnClickPendingIntent(R.id.div, PendingIntent.getBroadcast(context, shiftedAppWidgetId + 11, intent, 0));

		intent.setAction(MUL);
		remoteViews.setOnClickPendingIntent(R.id.mul, PendingIntent.getBroadcast(context, shiftedAppWidgetId + 12, intent, 0));

		intent.setAction(MINUS);
		remoteViews.setOnClickPendingIntent(R.id.minus, PendingIntent.getBroadcast(context, shiftedAppWidgetId + 13, intent, 0));

		intent.setAction(PLUS);
		remoteViews.setOnClickPendingIntent(R.id.plus, PendingIntent.getBroadcast(context, shiftedAppWidgetId + 14, intent, 0));

		intent.setAction(EQUALS);
		remoteViews.setOnClickPendingIntent(R.id.equal, PendingIntent.getBroadcast(context, shiftedAppWidgetId + 15, intent, 0));

		intent.setAction(DEL);
		remoteViews.setOnClickPendingIntent(R.id.delete, PendingIntent.getBroadcast(context, shiftedAppWidgetId + 16, intent, 0));

		intent.setAction(CLR);
		remoteViews.setOnClickPendingIntent(R.id.clear, PendingIntent.getBroadcast(context, shiftedAppWidgetId + 17, intent, 0));
	}
}
