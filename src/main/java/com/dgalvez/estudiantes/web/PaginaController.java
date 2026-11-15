package com.dgalvez.estudiantes.web;

import com.dgalvez.estudiantes.service.EstudianteService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.time.LocalDate;

@Controller
public class PaginaController {
    private final String siteKey;
    public PaginaController(@Value("${app.recaptcha.site-key}") String siteKey) { this.siteKey = siteKey; }

    @GetMapping("/") public String inicio() { return "redirect:/registro"; }
    @GetMapping("/registro") public String registro(Model model) {
        model.addAttribute("carreras", EstudianteService.CARRERAS);
        model.addAttribute("fechaMaxima", LocalDate.now().minusYears(16));
        model.addAttribute("recaptchaSiteKey", siteKey);
        return "registro";
    }
    @GetMapping("/consulta") public String consulta() { return "consulta"; }
}
