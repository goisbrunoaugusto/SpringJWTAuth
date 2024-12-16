package br.ufrn.imd.springChat.service;

import br.ufrn.imd.springChat.model.UserDetailsImpl;
import br.ufrn.imd.springChat.model.UserEntity;
import br.ufrn.imd.springChat.model.dto.TokenDTO;
import br.ufrn.imd.springChat.model.dto.UserDTO;
import br.ufrn.imd.springChat.model.enums.UserRole;
import br.ufrn.imd.springChat.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserEntity registerUser(UserDTO userDTO) {
        if(userRepository.findByName(userDTO.getName()).isPresent()){
            throw new RuntimeException("User already exists");
        }
        if (userDTO.getName() == null || userDTO.getName().isEmpty()) {
            throw new RuntimeException("User name cannot be null or empty");
        }
        UserEntity user = new UserEntity();
        user.setName(userDTO.getName());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setRole(UserRole.USER);
        return userRepository.save(user);
    }

    public TokenDTO login(UserDTO userDTO) {
        try{
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDTO.getName(), userDTO.getPassword());
            Authentication authentication = authenticationManager.authenticate(authToken);
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            return new TokenDTO(jwtService.generateToken(userDetails));
        } catch (Exception e){
            throw new RuntimeException("Error Authenticating User");
        }
    }

    public UserEntity registerAdmin(UserDTO userDTO) {
        if(userRepository.findByName(userDTO.getName()).isPresent()){
            throw new RuntimeException("User already exists");
        }
        if (userDTO.getName() == null || userDTO.getName().isEmpty()) {
            throw new RuntimeException("User name cannot be null or empty");
        }
        UserEntity user = new UserEntity();
        user.setName(userDTO.getName());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setRole(UserRole.ADMIN);
        return userRepository.save(user);
    }
}
