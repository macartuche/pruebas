package ec.gob.mspz7.repository;

import ec.gob.mspz7.domain.MiPais;
import ec.gob.mspz7.domain.Pais;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Pais entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaisRepository extends JpaRepository<Pais, Long> {
    @Query("SELECT entidad FROM Pais entidad WHERE entidad.region.codigo=:region")
    List<Pais> obtenerPorRegion(String region);

    @Query(value = "Select pais.id, pais.nombre FROM pais ", nativeQuery = true)
    List<MiPais> buscarPersonalizado();
}
