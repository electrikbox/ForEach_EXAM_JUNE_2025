package com.cocktailbar.backend;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cocktailbar.backend.DTO.LoginResponse;
import com.cocktailbar.backend.DTO.LoginUserDto;
import com.cocktailbar.backend.DTO.RegisterUserDto;
import com.cocktailbar.backend.model.Utilisateur;
import com.cocktailbar.backend.repository.UtilisateurRepository;

@Service
public class AuthService {
    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(UtilisateurRepository utilisateurRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public Utilisateur register(RegisterUserDto input) {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNomUtilisateur(input.getNomUtilisateur());
        utilisateur.setMotDePasse(passwordEncoder.encode(input.getMotDePasse()));
        utilisateur.setRoleUtilisateur(input.getRoleUtilisateur());
        return utilisateurRepository.save(utilisateur);
    }

    public LoginResponse login(LoginUserDto input) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(input.getNomUtilisateur(), input.getMotDePasse())
        );
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String jwt = jwtService.generateToken(userDetails);
        return new LoginResponse() {{
            setToken(jwt);
            setExpiresIn(jwtService.getExpirationTime());
        }};
    }
} 