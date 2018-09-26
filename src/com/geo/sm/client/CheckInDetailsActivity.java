package com.geo.sm.client;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import com.geo.sm.R;
import com.geo.sm.dao.CheckIn;
import com.geo.sm.dao.Qa;
import com.geo.sm.misc.CallableTask;
import com.geo.sm.misc.TaskCallback;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class CheckInDetailsActivity extends Activity {

	private final String LOGIN_NAME = "com.geo.sm.login_name";
	private final String LOGIN_PASSWORD = "com.geo.sm.login_password";

	private SharedPreferences prefs = null;

	private TextView medicineView;
	private TextView painStatusView;
	private TextView EatabilityView;
	private long checkin_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_checkin_screen);

		prefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		medicineView = (TextView) findViewById(R.id.textView1);
		painStatusView = (TextView) findViewById(R.id.textView2);
		EatabilityView = (TextView) findViewById(R.id.textView3);
	}

	@Override
	protected void onResume() {
		super.onResume();
		refreshVideos();

	}

	private void refreshVideos() {
		Intent incomingIntent = getIntent();
		checkin_id = incomingIntent.getLongExtra("checkin_id", 2l);
		medicineView.setText("The check in id is " + checkin_id);
		final UserSvcApi svc = UserSvc.getOrShowLogin(this);

		if (svc != null) {
			CallableTask.invoke(new Callable<Collection<Qa>>() {

				@Override
				public Collection<Qa> call() throws Exception {
					return svc.getQasByCheckInId(checkin_id);
				}
			}, new TaskCallback<Collection<Qa>>() {

				@Override
				public void success(Collection<Qa> result) {
					StringBuffer str = new StringBuffer();
					for (Qa q : result) {
						if (q.getQuestionType().getType().equals("type3"))
							str.append(q.getQuestion() + "      -     "
									+ q.getAnswer() + "\n");
						else if (q.getQuestionType().getType().equals("type1"))
							painStatusView.setText(q.getQuestion() + "\n"
									+ "*" + q.getAnswer().toUpperCase() + "*");
						else
							EatabilityView.setText(q.getQuestion() + "\n"
									+ "*" + q.getAnswer().toUpperCase() + "*");
					}
					medicineView.setText(str);

				}

				@Override
				public void error(Exception e) {
					Toast.makeText(
							CheckInDetailsActivity.this,
							"Unable to fetch the video list, please login again.",
							Toast.LENGTH_SHORT).show();

					startActivity(new Intent(CheckInDetailsActivity.this,
							LoginScreenActivity.class));
				}
			});
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.patient_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.logout:
			removeLoginFromSharedPreferences();
			Intent intent = new Intent(CheckInDetailsActivity.this,
					LoginScreenActivity.class);
			startActivity(intent);
			return true;
		default:
			return false;
		}
	}

	private void removeLoginFromSharedPreferences() {
		SharedPreferences.Editor editor = prefs.edit();
		editor.remove(LOGIN_NAME);
		editor.remove(LOGIN_PASSWORD);
		editor.commit();
		Log.i(CheckInDetailsActivity.class.getName(),
				prefs.getString(LOGIN_NAME, "Login name erased"));
	}

}
