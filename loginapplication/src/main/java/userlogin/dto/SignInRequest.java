package userlogin.dto;

import lombok.Data;

@Data
public class SignInRequest {
	
	private String email;
	private String passsword;
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPasssword() {
		return passsword;
	}
	public void setPasssword(String passsword) {
		this.passsword = passsword;
	}
	
	

}
