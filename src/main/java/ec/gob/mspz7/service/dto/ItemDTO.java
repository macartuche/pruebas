package ec.gob.mspz7.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link ec.gob.mspz7.domain.Item} entity.
 */
@Schema(description = "The Item entity.\n@author macartuche")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ItemDTO implements Serializable {

    private Long id;

    /**
     * codigo
     */
    @NotNull
    @Schema(description = "codigo", required = true)
    private String codigo;

    /**
     * codigoCatalogo
     */
    @NotNull
    @Schema(description = "codigoCatalogo", required = true)
    private String codigoCatalogo;

    /**
     * descripcion
     */
    @Schema(description = "descripcion")
    private String descripcion;

    private CatalogoDTO catalogo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigoCatalogo() {
        return codigoCatalogo;
    }

    public void setCodigoCatalogo(String codigoCatalogo) {
        this.codigoCatalogo = codigoCatalogo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public CatalogoDTO getCatalogo() {
        return catalogo;
    }

    public void setCatalogo(CatalogoDTO catalogo) {
        this.catalogo = catalogo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ItemDTO)) {
            return false;
        }

        ItemDTO itemDTO = (ItemDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, itemDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ItemDTO{" +
            "id=" + getId() +
            ", codigo='" + getCodigo() + "'" +
            ", codigoCatalogo='" + getCodigoCatalogo() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", catalogo=" + getCatalogo() +
            "}";
    }
}
