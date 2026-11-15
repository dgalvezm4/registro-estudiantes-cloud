package com.dgalvez.estudiantes.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.*;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

@Service
public class CaptchaService {
    private final boolean habilitado;
    private final String secret;
    private final ObjectMapper json;
    private final HttpClient http = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5)).build();

    public CaptchaService(@Value("${app.recaptcha.enabled:true}") boolean habilitado,
                          @Value("${app.recaptcha.secret-key}") String secret,
                          ObjectMapper json) {
        this.habilitado = habilitado;
        this.secret = secret;
        this.json = json;
    }

    public boolean verificar(String token) {
        if (!habilitado) return true;
        if (token == null || token.isBlank()) return false;
        try {
            String body = "secret=" + codificar(secret) + "&response=" + codificar(token);
            HttpRequest request = HttpRequest.newBuilder(URI.create("https://www.google.com/recaptcha/api/siteverify"))
                    .timeout(Duration.ofSeconds(10))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(body)).build();
            HttpResponse<String> response = http.send(request, HttpResponse.BodyHandlers.ofString());
            JsonNode result = json.readTree(response.body());
            return response.statusCode() == 200 && result.path("success").asBoolean(false);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("La verificación reCAPTCHA fue interrumpida");
        } catch (Exception ex) {
            throw new IllegalStateException("No fue posible verificar reCAPTCHA. Intente nuevamente");
        }
    }

    private String codificar(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}
