package org.sid.otpwhatsapp.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.sid.otpwhatsapp.dto.OtpResponseDTO;
import org.sid.otpwhatsapp.service.WhatsAppService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class WhatsAppServiceImpl implements WhatsAppService {

    @Value("${whatsapp.api.url}")
    private String whatsappApiUrl;

    @Value("${whatsapp.api.token}")
    private String whatsappApiKey;

    @Override
    public ResponseEntity<String> sendOtp(OtpResponseDTO otpResponseDTO) {

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + whatsappApiKey);

        // Create the request body JSON object
        JSONObject requestBody = new JSONObject();
        requestBody.put("messaging_product", "whatsapp");
        requestBody.put("to", otpResponseDTO.getPhoneNumber());
        requestBody.put("type", "template");

        // Create the template JSON object
        JSONObject template = new JSONObject();
        template.put("name", "2fa_whatsapp");
        template.put("language", new JSONObject()
                .put("code", "en")
                .put("policy", "deterministic"));

        // Create the components JSON array
        JSONArray components = new JSONArray();

        // Add the header component
        JSONObject header = new JSONObject();
        header.put("type", "header");
        components.put(header);

        // Add the body component
        JSONObject body = new JSONObject();
        body.put("type", "body");
        body.put("parameters", new JSONArray()
                .put(new JSONObject()
                        .put("type", "text")
                        .put("text",otpResponseDTO.getOtpCode())));
        components.put(body);

        template.put("components", components);
        requestBody.put("template", template);

        HttpEntity<String> request = new HttpEntity<>(requestBody.toString(), headers);
        return restTemplate.postForEntity(whatsappApiUrl, request, String.class);
    }
}

