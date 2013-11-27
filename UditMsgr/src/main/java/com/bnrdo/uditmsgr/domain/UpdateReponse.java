package com.bnrdo.uditmsgr.domain;

public class UpdateReponse<T> {
	private T update;
	
	public T getUpdate() {
		return update;
	}
	public void setUpdate(T update) {
		this.update = update;
	}
}
