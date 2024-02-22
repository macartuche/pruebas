package ec.gob.mspz7.repository;

import ec.gob.mspz7.domain.Item;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Item entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query("SELECT i FROM Item i WHERE i.catalogo.codigo=:codigoCatalogo ")
    List<Item> obtenerItemsPorCodigoCatalogo(@Param("codigoCatalogo") String codigoCatalogo);
}
