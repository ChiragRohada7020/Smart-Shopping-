<<<<<<< HEAD
package com.cdac.temp;

import java.time.LocalDateTime;

public class TempUser {
    private String username;
    private String email;
    private String encodedPassword;
    private String otp;
    private LocalDateTime otpExpiry;

    
    public TempUser(String username, String email, String encodedPassword, String otp, LocalDateTime otpExpiry) {
        this.username = username;
        this.email = email;
        this.encodedPassword = encodedPassword;
        this.otp = otp;
        this.otpExpiry = otpExpiry;
    }

   
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getEncodedPassword() { return encodedPassword; }
    public String getOtp() { return otp; }
    public LocalDateTime getOtpExpiry() { return otpExpiry; }
}
=======
package com.cdac.temp;

import java.time.LocalDateTime;

public class TempUser {
    private String username;
    private String email;
    private String encodedPassword;
    private String otp;
    private LocalDateTime otpExpiry;

    
    public TempUser(String username, String email, String encodedPassword, String otp, LocalDateTime otpExpiry) {
        this.username = username;
        this.email = email;
        this.encodedPassword = encodedPassword;
        this.otp = otp;
        this.otpExpiry = otpExpiry;
    }

   
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getEncodedPassword() { return encodedPassword; }
    public String getOtp() { return otp; }
    public LocalDateTime getOtpExpiry() { return otpExpiry; }
}
>>>>>>> cartUpdate
