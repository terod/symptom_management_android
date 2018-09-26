package com.geo.sm.client;

import java.util.concurrent.Callable;

import com.geo.sm.R;
import com.geo.sm.dao.Doctor;
import com.geo.sm.dao.Patient;
import com.geo.sm.doctor.PatientListActivity;
import com.geo.sm.misc.CallableTask;
import com.geo.sm.misc.TaskCallback;
import com.geo.sm.patient.CheckInListActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 
 * This application uses ButterKnife. AndroidStudio has better support for
 * ButterKnife than Eclipse, but Eclipse was used for consistency with the other
 * courses in the series. If you have trouble getting the login button to work,
 * please follow these directions to enable annotation processing for this
 * Eclipse project:
 * 
 * http://jakewharton.github.io/butterknife/ide-eclipse.html
 * 
 */
public class LoginScreenActivity extends Activity {

	protected EditText userName_;

	protected EditText password_;

	protected EditText server_;

	private Button loginButton;

	private final String LOGIN_NAME = "com.geo.sm.login_name";
	private final String LOGIN_PASSWORD = "com.geo.sm.login_password";

	private SharedPreferences prefs = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_screen);

		prefs = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
		userName_ = (EditText) findViewById(R.id.userName);
		password_ = (EditText) findViewById(R.id.password);
		server_ = (EditText) findViewById(R.id.server);
		loginButton = (Button) findViewById(R.id.loginButton);
	}

	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		String username = prefs.getString(LOGIN_NAME, "auto_login_failed");
		String password = prefs.getString(LOGIN_PASSWORD, "auto_login_failed");

		if (!username.equals("auto_login_failed") && !username.equals(null)) {
			loginButton.setActivated(false);
			userName_.setText(username);
			password_.setText(password);
			loginPatient();
			Log.i(LoginScreenActivity.class.getName(),username + " is still present");
		}

		loginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				loginPatient();
			}
		});
	}

	private void loginPatient() {
		String user = userName_.getText().toString();
		String pass = password_.getText().toString();
		String server = server_.getText().toString();

		final UserSvcApi svc = UserSvc.init(server, user, pass);

		CallableTask.invoke(new Callable<Patient>() {

			@Override
			public Patient call() throws Exception {
				return svc.getPatientByUserName(userName_.getText().toString());
			}
		}, new TaskCallback<Patient>() {

			@Override
			public void success(Patient result) {
				// OAuth 2.0 grant was successful and we
				// can talk to the server, open up the video listing
				// startActivity(new Intent(
				// LoginScreenActivity.this,
				// VideoListActivity.class));
				if (result != null
						&& result.getUserName().equals(
								userName_.getText().toString())
						&& result.getAdministeredBy() != null) {
					Log.i(LoginScreenActivity.class.getName(), "MRN is "
							+ result.getMedicalRecordNumber());
					Log.i(LoginScreenActivity.class.getName(),
							"get patient by user name Successful");
					Intent intent = new Intent(LoginScreenActivity.this,
							CheckInListActivity.class);
					intent.putExtra("patient_id", result.getId());

					saveInSharedPreference();
					startActivity(intent);
				} else
					loginDoctor(svc);
			}

			@Override
			public void error(Exception e) {
				Log.e(LoginScreenActivity.class.getName(),
						"Error logging in via OAuth.", e);

				Toast.makeText(
						LoginScreenActivity.this,
						"Login failed, check your Internet connection and credentials.",
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	protected void saveInSharedPreference() {
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(LOGIN_NAME, userName_.getText().toString());
		editor.putString(LOGIN_PASSWORD, password_.getText().toString());
		editor.commit();

	}

	private void loginDoctor(final UserSvcApi svc) {
		Log.i(LoginScreenActivity.class.getName(), "Entered doctor's section");

		CallableTask.invoke(new Callable<Doctor>() {

			@Override
			public Doctor call() throws Exception {
				return svc.getDoctorByUserName(userName_.getText().toString());
			}
		}, new TaskCallback<Doctor>() {

			@Override
			public void success(Doctor result) {
				// OAuth 2.0 grant was successful and we
				// can talk to the server, open up the video listing
				// startActivity(new Intent(
				// LoginScreenActivity.this,
				// VideoListActivity.class));
				if (result != null
						&& result.getUserName().equals(
								userName_.getText().toString())) {
					Log.i(LoginScreenActivity.class.getName(), "Doctor id is "
							+ result.getDoctorId());
					Log.i(LoginScreenActivity.class.getName(),
							"get doctor by user name Successful");
					Intent intent = new Intent(LoginScreenActivity.this,
							PatientListActivity.class);
					intent.putExtra("doctor_id", result.getId());

					saveInSharedPreference();
					startActivity(intent);
				} else {
					Toast.makeText(
							LoginScreenActivity.this,
							"Login failed, You are not yet administered by a doctor. Please check with your doctor.",
							Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void error(Exception e) {
				Log.e(LoginScreenActivity.class.getName(),
						"Error logging in via OAuth.", e);

				Toast.makeText(
						LoginScreenActivity.this,
						"Login failed, check your Internet connection and credentials.",
						Toast.LENGTH_SHORT).show();
			}
		});
	}

}
