package com.app.ecom.Dto;

import com.app.ecom.model.user.UserRole;
import lombok.Data;

@Data

public class userResponse {

    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private UserRole userRole;
    private  AddressDTO address;

}
