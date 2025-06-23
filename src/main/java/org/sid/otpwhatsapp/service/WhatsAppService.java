package org.sid.otpwhatsapp.service;

import org.sid.otpwhatsapp.dto.OtpResponseDTO;
import org.springframework.http.ResponseEntity;

public interface WhatsAppService {

    ResponseEntity<String> sendOtp(OtpResponseDTO otpResponseDTO);
}
