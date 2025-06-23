package org.sid.otpwhatsapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OtpResponseDTO {
    private boolean success;
    private String phoneNumber;
    private String otpCode;
    private String context;
    private String message;
}
