package com.example.oldbookmarket.shared.utils;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class Common {
    
    // public static final String IPN_URL_MOMO = "https://hotelservice-v5.herokuapp.com/api/v1/MomoConfirm";
//     public static final String IPN_URL_MOMO = "http://localhost:3000/momoWebViewConfirm";
    public static final String IPN_URL_MOMO = "http://localhost:8080/api/v1/MomoConfirm";
//
    // public static final String REDIRECT_URL_MOMO = "http://localhost:8080/api/v1/MomoConfirm";

    public static final String REDIRECT_URL_MOMO = "https://youtube.com";
    public static final String PARTNER_CODE = "MOMODJMX20220717";
    public static final String ACCESS_KEY = "WehkypIRwPP14mHb";
    public static final String SECRET_KEY = "3fq8h4CqAAPZcTTb3nCDpFKwEkQDsZzz";
    public static final String MOMO_URI = "https://test-payment.momo.vn/v2/gateway/api/create";


    // althogrithm
    public static final String HMACSHA256 = "HmacSHA256";
    public static final String HMACSHA512 = "HmacSHA512";

}
