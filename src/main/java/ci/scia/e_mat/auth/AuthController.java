package ci.scia.e_mat.auth;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @GetMapping("/login")
    public String login() {
        return "Redirection vers Keycloak pour l'authentification";
    }
}