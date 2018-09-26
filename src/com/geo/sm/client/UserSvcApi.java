package com.geo.sm.client;

import java.util.Collection;

import com.geo.sm.dao.CheckIn;
import com.geo.sm.dao.Doctor;
import com.geo.sm.dao.Patient;
import com.geo.sm.dao.Qa;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * This interface defines an API for a VideoSvc. The interface is used to
 * provide a contract for client/server interactions. The interface is annotated
 * with Retrofit annotations so that clients can automatically convert the
 * 
 * 
 * @author jules
 * 
 */
public interface UserSvcApi {

	public static final String PASSWORD_PARAMETER = "password";

	public static final String USERNAME_PARAMETER = "username";

	public static final String TOKEN_PATH = "/oauth/token";

	public static final String PATIENT_NAME_PARAMETER = "name";

	public static final String DOCTOR_SVC_PATH = "/doctor";
	public static final String PATIENT_SVC_PATH = "/patient";
	
	public static final String CHECKIN_SVC_PATH = "/checkin";
	
	public static final String PATIENT_NAME_SEARCH_PATH = PATIENT_SVC_PATH
			+ "/find";

	@POST(PATIENT_SVC_PATH)
	public Patient addPatient(@Body Patient c);

	@POST(DOCTOR_SVC_PATH)
	public Doctor addDoctor(@Body Doctor c);

	@GET(DOCTOR_SVC_PATH + "/{id}/patients")
	public Collection<Patient> getPatientListByDoctorId(@Path("id") Long id);

	@GET(DOCTOR_SVC_PATH + "/{id}")
	public Doctor getDoctorById(@Path("id") Long id);

	@GET(PATIENT_SVC_PATH + "/{id}")
	public Patient getPatientById(@Path("id") Long id);

	@GET(PATIENT_NAME_SEARCH_PATH)
	public Collection<Patient> getPatientListByFirstOrLastName(
			@Query(PATIENT_NAME_PARAMETER) String name);

	@GET(PATIENT_SVC_PATH + "/findbyusername")
	public Patient getPatientByUserName(
			@Query(PATIENT_NAME_PARAMETER) String name);

	@GET(DOCTOR_SVC_PATH + "/findbyusername")
	public Doctor getDoctorByUserName(@Query(PATIENT_NAME_PARAMETER) String name);
	
	@GET(UserSvcApi.PATIENT_SVC_PATH + "/{id}/checkins")
	public Collection<CheckIn> getCheckInsByPatientId(@Path("id") Long id);
	
	@GET(CHECKIN_SVC_PATH + "/{id}/qas")
	public Collection<Qa> getQasByCheckInId(@Path("id") Long id);
}
