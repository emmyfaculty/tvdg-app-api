package com.tvdgapp.dtos.auth;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.tvdgapp.models.user.UserType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
//@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.ALWAYS)
public class UserDto {
	private long userId;
	private String firstName;
	private String lastName;
    private String email;
	private String phone;
	private String profilePic;
	private String status;
	private Long lastLogin;
	private UserType userType;
//    @JsonProperty("organisation")
//	private OrganisationInfo organisationInfo;
//    private long organisationId;

	public UserDto(long userId, String firstName, String lastName, String email, String phone, String profilePic, String status, Long lastLogin, UserType userType) {
		this.userId = userId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phone = phone;
		this.profilePic =profilePic;
		this.status = status;
		this.lastLogin = lastLogin;
		this.userType = userType;
	}

//	public UserDto(long userId, String firstName, String lastName, String email,Long organisationId,String clientType,String location) {
//		this.userId = userId;
//		this.firstName = firstName;
//		this.lastName = lastName;
//		this.email = email;
//	}


//	@Data
//	public class OrganisationInfo{
//    	public long id;
//    	public String customerType;
//    	public String location;
//    	@JsonProperty("isSubscribingClient")
//    	public boolean isSubscribingClient;
//	}
}
