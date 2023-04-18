package com.example.oldbookmarket.dto.request.LoginGoogleRequest;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Getter
@Setter
public class OAuth2Request {
    @JsonProperty("id")
    private String id;
    @JsonProperty("fullname")
    private String fullname;
    @JsonProperty("givenname")
    private String givenname;
    @JsonProperty("familyname")
    private String familyname;
    @JsonProperty("imageUrl")
    private String imageUrl;
    @JsonProperty("email")
    private String email;
    
}
