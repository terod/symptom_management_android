package com.geo.sm.dao;

import com.google.common.base.Objects;

public class Patient extends User {

	private String medicalRecordNumber;

	private long dob;

	public long getDob() {
		return dob;
	}

	public void setDob(long dob) {
		this.dob = dob;
	}

	private Doctor administeredBy;

	public Doctor getAdministeredBy() {
		return administeredBy;
	}

	public void setAdministeredBy(Doctor administeredBy) {
		this.administeredBy = administeredBy;
	}

	public String getMedicalRecordNumber() {
		return medicalRecordNumber;
	}

	public void setMedicalRecordNumber(String medicalRecordNumber) {
		this.medicalRecordNumber = medicalRecordNumber;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(medicalRecordNumber, getFirstName(),
				getLastName());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Patient) {
			Patient other = (Patient) obj;
			return Objects
					.equal(medicalRecordNumber, other.medicalRecordNumber)
					&& Objects.equal(getFirstName(), other.getLastName());
		} else {
			return false;
		}
	}

}
