package com.geo.sm.patient;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import com.geo.sm.R;
import com.geo.sm.client.CheckInDetailsActivity;
import com.geo.sm.client.LoginScreenActivity;
import com.geo.sm.client.UserSvc;
import com.geo.sm.client.UserSvcApi;
import com.geo.sm.dao.CheckIn;
import com.geo.sm.doctor.PatientListActivity;
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

public class CheckInListActivity extends Activity {

	protected ListView patientList_;

	private final String LOGIN_NAME = "com.geo.sm.login_name";
	private final String LOGIN_PASSWORD = "com.geo.sm.login_password";

	private SharedPreferences prefs = null;

	private long patient_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.patient_home_activity);

		prefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());

		patientList_ = (ListView) findViewById(R.id.myCheckinsList);
		patientList_.setTextFilterEnabled(true);

	}

	@Override
	protected void onResume() {
		super.onResume();
		refreshVideos();
		patientList_.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				/*Date date = null;
				try {
					date = SimpleDateFormat.getDateTimeInstance().parse(
							((TextView) view).getText().toString());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
				int index = ((TextView) view).getText().toString().indexOf(':');
				long checkInId = Long.parseLong(((TextView) view).getText().toString().substring(0, index));
				Log.i(CheckInListActivity.class.getName(), "Parsing of checkin id successful " + checkInId);
				Intent intent = new Intent(getApplicationContext(),CheckInDetailsActivity.class);
				intent.putExtra("checkin_id", checkInId);
				startActivity(intent);
			}
		});
	}

	private void refreshVideos() {
		final UserSvcApi svc = UserSvc.getOrShowLogin(this);
		Intent incomingIntent = getIntent();
		patient_id = incomingIntent.getLongExtra("patient_id", 2l);

		if (svc != null) {
			CallableTask.invoke(new Callable<Collection<CheckIn>>() {

				@Override
				public Collection<CheckIn> call() throws Exception {
					return svc.getCheckInsByPatientId(patient_id);
				}
			}, new TaskCallback<Collection<CheckIn>>() {

				@Override
				public void success(Collection<CheckIn> result) {
					List<String> names = new ArrayList<String>();
					for (CheckIn c : result) {
						Date date = new Date(c.getDateTime());
						DateFormat dateFormatter = SimpleDateFormat
								.getDateTimeInstance();
						names.add(c.getId() + ": " + dateFormatter.format(date));
					}
					patientList_.setAdapter(new ArrayAdapter<String>(
							CheckInListActivity.this,
							android.R.layout.simple_list_item_1, names));
				}

				@Override
				public void error(Exception e) {
					Toast.makeText(
							CheckInListActivity.this,
							"Unable to fetch the video list, please login again.",
							Toast.LENGTH_SHORT).show();

					startActivity(new Intent(CheckInListActivity.this,
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
			Intent intent = new Intent(CheckInListActivity.this,
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
		Log.i(CheckInListActivity.class.getName(),
				prefs.getString(LOGIN_NAME, "Login name erased"));
	}

}
