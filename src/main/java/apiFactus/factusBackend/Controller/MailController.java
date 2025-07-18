package apiFactus.factusBackend.Controller;

import apiFactus.factusBackend.Dto.EmailAsesoriaRequest;
import apiFactus.factusBackend.Service.RecuperarContrasenaTokenService;
import apiFactus.factusBackend.integration.notification.email.service.EmailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mail")
public class MailController {

    private final RecuperarContrasenaTokenService recuperarContrasenaTokenService;
    private final EmailService emailService;

    @PostMapping("/sendMail")
    public ResponseEntity<Void> enviarRecuperarContrasenaEmail(@RequestBody Map<String, String> request) throws Exception {
        String email = request.get("email");
        recuperarContrasenaTokenService.createAndSendPasswordResetToken(email);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/reset/check/{token}")
    public ResponseEntity<Boolean> verificarTokenValido(@PathVariable String token) {
        boolean isValid = recuperarContrasenaTokenService.isValidToken(token);
        return new ResponseEntity<>(isValid, HttpStatus.OK);
    }
    @PostMapping("/reset/{token}")
    public ResponseEntity<Void> recuperarContrasena(@PathVariable String token, @RequestBody Map<String, String> request){
        String newContrasena = request.get("newContrasena");
        recuperarContrasenaTokenService.recuperarContrasena(token, newContrasena);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //ELIMINAR ESTA LINEAS DE CODIGO DESPUES DE TERMINAR DE CONFIGURAR COMPLETAMENTE EL SERVICIO DE CORREO ELECTRONICO
    @PostMapping("/enviar/confirmacion/asesoria")
    public ResponseEntity<Map<String, String>> enviarCorreoAsesoria(@RequestBody @Valid EmailAsesoriaRequest request) {
        emailService.enviarCorreoAsesoria(request);

        Map<String, String> response = new HashMap<>();
        response.put("mensaje", "Correo enviado correctamente");

        return ResponseEntity.ok(response);
    }


}
