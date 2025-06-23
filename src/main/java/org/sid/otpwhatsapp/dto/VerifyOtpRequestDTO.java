package org.sid.otpwhatsapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerifyOtpRequestDTO {
    private String phoneNumber;
    private String otpCode;
    private String context; // This can be used to identify the context of the OTP verification

}