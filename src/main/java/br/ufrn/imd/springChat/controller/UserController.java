package br.ufrn.imd.springChat.controller;

import br.ufrn.imd.springChat.model.UserEntity;
import br.ufrn.imd.springChat.model.dto.TokenDTO;
import br.ufrn.imd.springChat.model.dto.UserDTO;
import br.ufrn.imd.springChat.service.JwtService;
import br.ufrn.imd.springChat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDTO userDTO) {
        try{
            UserEntity user = userService.register(userDTO);
            return ResponseEntity.ok(user);
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody(required = true) UserDTO userDTO) {
        try {
            TokenDTO token = userService.login(userDTO);
            return ResponseEntity.ok().body(token);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
