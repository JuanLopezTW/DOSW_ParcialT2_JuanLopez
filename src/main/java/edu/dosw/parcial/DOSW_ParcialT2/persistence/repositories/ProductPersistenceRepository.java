package edu.dosw.parcial.DOSW_ParcialT2.persistence.repositories;

import edu.dosw.parcial.DOSW_ParcialT2.persistence.entities.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductPersistenceRepository extends JpaRepository<ProductEntity, String> {
    Optional<ProductEntity> findByCodQR(String codQR);
}
