package com.example.covoiturage.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping({"","/"})
    public String home() {
        return "index";
    }

    @GetMapping("/dashboardPassager")
    public String dashboardPassager() {
        return "dashboardPassager";
    }

    @GetMapping("/dashboardConducteur")
    public String dashboardConducteur() {
        return "dashboardConducteur";
    }

    @GetMapping("/ajoutTrajetConducteur")
    public String ajoutTrajetConducteur() {
        return "ajoutTrajetConducteur";
    }

    @GetMapping("/gestReservation")
    public String gestReservation() {
        return "gestReservation";
    }

}

