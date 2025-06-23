package org.sid.otpwhatsapp.service.serviceImpl;

import lombok.AllArgsConstructor;
import org.sid.otpwhatsapp.dto.OtpResponseDTO;
import org.sid.otpwhatsapp.dto.SendOtpRequestDTO;
import org.sid.otpwhatsapp.dto.VerifyOtpRequestDTO;
import org.sid.otpwhatsapp.service.OtpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;


@Service
@AllArgsConstructor
public class OtpServiceImpl implements OtpService {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final Logger logger = LoggerFactory.getLogger(OtpServiceImpl.class);

    private static final long OTP_EXPIRATION_MINUTES = 2; // TTL for the OTP
    private static final String OTP_PREFIX = "otp";


    @Override
    public OtpResponseDTO generateAndCacheOtp(SendOtpRequestDTO sendOtpRequestDTO) {

        String phoneNumber = sendOtpRequestDTO.getPhoneNumber();
        String context = sendOtpRequestDTO.getContext();
        String cacheKey = generateCacheKey(context, phoneNumber);

        try {
            String otp = generateRandomOtp();
            logger.info("Generated OTP {} for key {}. Caching with {} minute TTL.", otp, cacheKey, OTP_EXPIRATION_MINUTES);

            // Store the OTP in Redis with the specified expiration
            redisTemplate.opsForValue().set(cacheKey, otp, OTP_EXPIRATION_MINUTES, TimeUnit.MINUTES);

            logger.info("OTP {} cached successfully for key: {}", otp, cacheKey);
            // Return the OTP response DTO with success status
            return OtpResponseDTO.builder()
                    .success(true)
                    .phoneNumber(phoneNumber)
                    .otpCode(otp)
                    .context(context)
                    .message("OTP generated and cached successfully.")
                    .build();

        } catch (Exception e) {
            logger.error("Error generating or caching OTP for key: {}", cacheKey, e);
            // Throw an exception to let the calling service know that the operation failed.
            throw new RuntimeException("Could not generate or cache OTP for phone number: " + phoneNumber);
        }
    }

    @Override
    public OtpResponseDTO verifyOtp(VerifyOtpRequestDTO verifyOtpRequestDTO) {

        String cacheKey = generateCacheKey(verifyOtpRequestDTO.getContext(), verifyOtpRequestDTO.getPhoneNumber());
        try {
            Object storedOtp = redisTemplate.opsForValue().get(cacheKey);
            if (storedOtp.toString().equals(verifyOtpRequestDTO.getOtpCode())) {
                logger.info("OTP verification successful for key: {}", cacheKey);
                redisTemplate.delete(cacheKey);
                return new OtpResponseDTO(true, verifyOtpRequestDTO.getPhoneNumber(), verifyOtpRequestDTO.getOtpCode(), verifyOtpRequestDTO.getContext() , "OTP verified successfully.");
            } else {
                logger.warn("Verification failed for key: {}. Mismatched OTP.", cacheKey);
                return new OtpResponseDTO(false, verifyOtpRequestDTO.getPhoneNumber(),verifyOtpRequestDTO.getOtpCode(),verifyOtpRequestDTO.getContext(), "Invalid OTP code.");
            }
        } catch (Exception e) {
            logger.error("Error during OTP verification for key: {}", cacheKey, e);
            return new OtpResponseDTO(false, verifyOtpRequestDTO.getPhoneNumber(),verifyOtpRequestDTO.getOtpCode(),verifyOtpRequestDTO.getContext(), "An error occurred during verification.");
        }
    }

    @Override
    public String generateCacheKey(String context, String phoneNumber) {
        return OTP_PREFIX + ":" + context.toUpperCase() + ":" + phoneNumber;
    }

    @Override
    public String generateRandomOtp() {
        SecureRandom random = new SecureRandom();
        int otpValue = 100_000 + random.nextInt(900_000); // 100000 - 999999
        return String.valueOf(otpValue);
    }
}
