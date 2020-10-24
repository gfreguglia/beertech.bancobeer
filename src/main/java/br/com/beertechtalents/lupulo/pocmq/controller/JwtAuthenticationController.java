package br.com.beertechtalents.lupulo.pocmq.controller;

import br.com.beertechtalents.lupulo.pocmq.controller.dto.JwtRequest;
import br.com.beertechtalents.lupulo.pocmq.controller.dto.JwtResponse;
import br.com.beertechtalents.lupulo.pocmq.service.ContaService;
import br.com.beertechtalents.lupulo.pocmq.util.JwtTokenUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@CrossOrigin
@AllArgsConstructor
public class JwtAuthenticationController {

    AuthenticationManager authenticationManager;
    JwtTokenUtil jwtTokenUtil;
    UserDetailsService userDetailsService;
    ContaService contaService;

    @PostMapping("/authenticate")
    public ResponseEntity<JwtResponse> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) {
        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    private void authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

}
