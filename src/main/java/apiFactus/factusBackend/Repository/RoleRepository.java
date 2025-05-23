package apiFactus.factusBackend.Repository;

import apiFactus.factusBackend.Domain.Entity.Role;
import apiFactus.factusBackend.Domain.enums.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(ERole name);
}
