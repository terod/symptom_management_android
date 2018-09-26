package com.geo.sm.dao;

import com.google.common.base.Objects;

public class CheckIn {

	private long id;

	private long dateTime;

	private Patient checkInBy;

	private String imagePath;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getDateTime() {
		return dateTime;
	}

	public void setDateTime(long dateTime) {
		this.dateTime = dateTime;
	}

	public Patient getCheckInBy() {
		return checkInBy;
	}

	public void setCheckInBy(Patient checkInBy) {
		this.checkInBy = checkInBy;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(dateTime, imagePath);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CheckIn) {
			CheckIn other = (CheckIn) obj;
			return Objects.equal(dateTime, other.dateTime)
					&& Objects.equal(imagePath, other.imagePath);
		} else {
			return false;
		}
	}
}
