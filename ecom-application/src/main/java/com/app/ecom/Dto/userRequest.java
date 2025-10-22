package com.app.ecom.Dto;


import lombok.Data;

@Data
public class userRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private  AddressDTO address;
}
