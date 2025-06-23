package org.sid.otpwhatsapp.service;

import org.sid.otpwhatsapp.dto.OtpResponseDTO;
import org.sid.otpwhatsapp.dto.SendOtpRequestDTO;
import org.sid.otpwhatsapp.dto.VerifyOtpRequestDTO;

public interface OtpService {

    OtpResponseDTO generateAndCacheOtp(SendOtpRequestDTO sendOtpRequestDTO);

    OtpResponseDTO verifyOtp(VerifyOtpRequestDTO verifyOtpRequestDTO);

    String generateCacheKey(String context, String phoneNumber);
    String generateRandomOtp();
}
