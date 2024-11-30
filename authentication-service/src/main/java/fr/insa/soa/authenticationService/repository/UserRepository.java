package fr.insa.soa.authenticationService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.insa.soa.authenticationService.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserName(String userName);  // Méthode pour rechercher un utilisateur par son nom d'utilisateur
}

