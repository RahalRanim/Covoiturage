package com.example.covoiturage.ServicesImpl;

import com.example.covoiturage.Entity.Utilisateur;
import com.example.covoiturage.Repository.UtilisateurRepository;
import com.example.covoiturage.Services.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UtilisateurServiceImpl implements UtilisateurService {
    @Autowired
    private UtilisateurRepository utilisateurRepository;
    @Override
    public Utilisateur findByEmail(String email) {
        return utilisateurRepository.findByEmail(email);
    }

}
