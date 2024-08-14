package com.tvdgapp.controllers.auth;


import com.tvdgapp.securityconfig.JwtTokenProvider;
import com.tvdgapp.services.session.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
public class AuthController {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private SessionService sessionService;

    @PostMapping("/refreshToken")
    public ResponseEntity<String> refreshToken(@RequestHeader("Authorization") String authorizationHeader) {

//        System.out.println("Received token: " + token);

        // Extract the JWT token from the "Bearer" prefix
        String token = authorizationHeader.replace("Bearer ", "");

        // Remove extra quotes if present
        if (token.startsWith("\"") && token.endsWith("\"")) {
            token = token.substring(1, token.length() - 1);
        }

        // Remove any leading or trailing spaces
        token = token.trim();

        // Check and remove internal spaces (though this might indicate a malformed token)
        if (token.contains(" ")) {
            token = token.replaceAll("\\s", "");
        }
        String refreshedToken = jwtTokenProvider.generateToken(token);
        sessionService.refreshSession(token);
        return ResponseEntity.ok(refreshedToken);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        sessionService.revokeSession(token);
        return ResponseEntity.ok().build();
    }
}
