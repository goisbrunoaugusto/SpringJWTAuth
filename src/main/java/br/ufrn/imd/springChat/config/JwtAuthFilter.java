package br.ufrn.imd.springChat.config;

import br.ufrn.imd.springChat.model.UserDetailsImpl;
import br.ufrn.imd.springChat.model.UserEntity;
import br.ufrn.imd.springChat.repository.UserRepository;
import br.ufrn.imd.springChat.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String token = recoverToken(authHeader);

        if (token != null) {
            String subject = jwtService.validateToken(token);
            UserEntity user;
            try {
                user = userRepository.findByName(subject).get();
            } catch (Exception e) {
                throw new RuntimeException("Inconsistent User");
            }
            UserDetailsImpl userDetails = new UserDetailsImpl(user);
            Authentication auth = new UsernamePasswordAuthenticationToken(userDetails.getUsername(),
                    null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filterChain.doFilter(request, response);
    }

    private String recoverToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
}
