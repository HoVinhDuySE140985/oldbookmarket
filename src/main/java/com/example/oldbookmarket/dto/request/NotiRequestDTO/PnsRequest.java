package com.example.oldbookmarket.dto.request.NotiRequestDTO;

public class PnsRequest {
    private String token;
	private String title;
	private String message;
	public PnsRequest(String token, String title, String message) {
		super();
		this.token = token;
		this.title = title;
		this.message = message;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}
