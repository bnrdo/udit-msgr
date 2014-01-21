package com.bnrdo.uditmsgr.domain;

import com.bnrdo.uditmsgr.util.Constants.Status;

public class User{
	
	private String userName;
	private String fullName;
	private String motherName;
	private String fatherName;
	private String personalStatus;
	private String hobby;
	private String motto;
	private String ipAddress;
	private Status status;
	
	public User(){}
	
	public User(User user){
		this.userName = user.getUserName();
		this.ipAddress = user.getIpAddress();
		this.fullName = user.getFullName();
		this.motherName = user.getMotherName();
		this.fatherName = user.getFatherName();
		this.personalStatus = user.getPersonalStatus();
		this.hobby = user.getHobby();
		this.motto = user.getMotto();
		this.status = user.getStatus();
	}
	
	public User(String userName, String fullName, String motherName,
			String fatherName, String personalStatus, String hobby,
			String motto, String ipAddress, Status status) {
		super();
		this.userName = userName;
		this.fullName = fullName;
		this.motherName = motherName;
		this.fatherName = fatherName;
		this.personalStatus = personalStatus;
		this.hobby = hobby;
		this.motto = motto;
		this.ipAddress = ipAddress;
		this.status = status;
	}

	public User(String userName, String ipAddress){
		this.userName = userName;
		this.ipAddress = ipAddress;
	}
	
	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	
	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getMotherName() {
		return motherName;
	}

	public void setMotherName(String motherName) {
		this.motherName = motherName;
	}

	public String getFatherName() {
		return fatherName;
	}

	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}

	public String getPersonalStatus() {
		return personalStatus;
	}

	public void setPersonalStatus(String personalStatus) {
		this.personalStatus = personalStatus;
	}

	public String getHobby() {
		return hobby;
	}

	public void setHobby(String hobby) {
		this.hobby = hobby;
	}

	public String getMotto() {
		return motto;
	}

	public void setMotto(String motto) {
		this.motto = motto;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((fatherName == null) ? 0 : fatherName.hashCode());
		result = prime * result
				+ ((fullName == null) ? 0 : fullName.hashCode());
		result = prime * result + ((hobby == null) ? 0 : hobby.hashCode());
		result = prime * result
				+ ((ipAddress == null) ? 0 : ipAddress.hashCode());
		result = prime * result
				+ ((motherName == null) ? 0 : motherName.hashCode());
		result = prime * result + ((motto == null) ? 0 : motto.hashCode());
		result = prime * result
				+ ((personalStatus == null) ? 0 : personalStatus.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result
				+ ((userName == null) ? 0 : userName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (fatherName == null) {
			if (other.fatherName != null)
				return false;
		} else if (!fatherName.equals(other.fatherName))
			return false;
		if (fullName == null) {
			if (other.fullName != null)
				return false;
		} else if (!fullName.equals(other.fullName))
			return false;
		if (hobby == null) {
			if (other.hobby != null)
				return false;
		} else if (!hobby.equals(other.hobby))
			return false;
		if (ipAddress == null) {
			if (other.ipAddress != null)
				return false;
		} else if (!ipAddress.equals(other.ipAddress))
			return false;
		if (motherName == null) {
			if (other.motherName != null)
				return false;
		} else if (!motherName.equals(other.motherName))
			return false;
		if (motto == null) {
			if (other.motto != null)
				return false;
		} else if (!motto.equals(other.motto))
			return false;
		if (personalStatus == null) {
			if (other.personalStatus != null)
				return false;
		} else if (!personalStatus.equals(other.personalStatus))
			return false;
		if (status != other.status)
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}

	@Override
	public String toString(){
		return userName;
	}
}
