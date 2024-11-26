package fr.insa.soa.userservice.repository;

import fr.insa.soa.userservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // The repository layer is responsible for data access. It interacts with the database and performs CRUD operations.

    // Custom query methods
    User findById(int id);

    // TODO : need to specify this for the type to be int not LONG
    void deleteById(int id);
}