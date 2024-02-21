package ec.gob.mspz7.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link ec.gob.mspz7.domain.Pais} entity.
 */
@Schema(description = "The Entity entity.\n@author A true hipster")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaisDTO implements Serializable {

    private Long id;

    /**
     * nombre
     */
    @Schema(description = "nombre")
    private String nombre;

    private ItemDTO region;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ItemDTO getRegion() {
        return region;
    }

    public void setRegion(ItemDTO region) {
        this.region = region;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaisDTO)) {
            return false;
        }

        PaisDTO paisDTO = (PaisDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, paisDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaisDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", region=" + getRegion() +
            "}";
    }
}
