package com.geo.sm.doctor;

import com.geo.sm.R;
import com.geo.sm.client.LoginScreenActivity;
import com.geo.sm.client.UserSvc;
import com.geo.sm.client.UserSvcApi;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

public class PatientListActivity extends Activity {

	protected ListView patientList_;

	private final String LOGIN_NAME = "com.geo.sm.login_name";
	private final String LOGIN_PASSWORD = "com.geo.sm.login_password";

	private SharedPreferences prefs = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.doctor_home_activity);

		prefs = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());

		patientList_ = (ListView) findViewById(R.id.myPatientList);
	}

	@Override
	protected void onResume() {
		super.onResume();

		refreshVideos();
	}

	private void refreshVideos() {
		final UserSvcApi svc = UserSvc.getOrShowLogin(this);

		/*
		 * if (svc != null) { CallableTask.invoke(new
		 * Callable<Collection<Video>>() {
		 * 
		 * @Override public Collection<Video> call() throws Exception { return
		 * svc.getVideoList(); } }, new TaskCallback<Collection<Video>>() {
		 * 
		 * @Override public void success(Collection<Video> result) {
		 * List<String> names = new ArrayList<String>(); for (Video v : result)
		 * { names.add(v.getName()); } videoList_.setAdapter(new
		 * ArrayAdapter<String>( VideoListActivity.this,
		 * android.R.layout.simple_list_item_1, names)); }
		 * 
		 * @Override public void error(Exception e) { Toast.makeText(
		 * VideoListActivity.this,
		 * "Unable to fetch the video list, please login again.",
		 * Toast.LENGTH_SHORT).show();
		 * 
		 * startActivity(new Intent(VideoListActivity.this,
		 * LoginScreenActivity.class)); } }); }
		 */
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
			Intent intent = new Intent(PatientListActivity.this,
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
	}

}
