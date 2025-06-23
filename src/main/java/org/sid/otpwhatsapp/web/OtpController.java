package org.sid.otpwhatsapp.web;

import lombok.AllArgsConstructor;
import org.sid.otpwhatsapp.dto.OtpResponseDTO;
import org.sid.otpwhatsapp.dto.SendOtpRequestDTO;
import org.sid.otpwhatsapp.dto.VerifyOtpRequestDTO;
import org.sid.otpwhatsapp.service.OtpService;
import org.sid.otpwhatsapp.service.WhatsAppService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/otp")
@AllArgsConstructor
public class OtpController {

    private final OtpService otpService;
    private final WhatsAppService whatsappService;

    @PostMapping("/send")
    public ResponseEntity<OtpResponseDTO> sendOtp(@RequestBody SendOtpRequestDTO sendOtpRequestDTO) {
        OtpResponseDTO otpResponse = otpService.generateAndCacheOtp(sendOtpRequestDTO);
        whatsappService.sendOtp(otpResponse);
        return ResponseEntity.ok(otpResponse);
    }

    @PostMapping("/verify")
    public ResponseEntity<OtpResponseDTO> verifyOtp(@RequestBody VerifyOtpRequestDTO verifyOtpRequestDTO) {
        OtpResponseDTO verifiedOtp = otpService.verifyOtp(verifyOtpRequestDTO);
        return ResponseEntity.ok(verifiedOtp);
    }
}
