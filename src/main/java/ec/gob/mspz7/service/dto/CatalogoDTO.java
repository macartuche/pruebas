package ec.gob.mspz7.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link ec.gob.mspz7.domain.Catalogo} entity.
 */
@Schema(description = "The Catalogo entity.\n@author macartuche")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CatalogoDTO implements Serializable {

    private Long id;

    /**
     * Codigo de catalogo
     */
    @NotNull
    @Schema(description = "Codigo de catalogo", required = true)
    private String codigo;

    /**
     * descripcion
     */
    @Schema(description = "descripcion")
    private String descripcion;

    /**
     * nuevocampo
     */
    @Schema(description = "nuevocampo")
    private String nuevocampo;

    /**
     * valor
     */
    @Schema(description = "valor")
    private BigDecimal valor;

    /**
     * activo
     */
    @Schema(description = "activo")
    private Boolean activo;

    /**
     * observacion
     */
    @Schema(description = "observacion")
    private String observacion;

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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNuevocampo() {
        return nuevocampo;
    }

    public void setNuevocampo(String nuevocampo) {
        this.nuevocampo = nuevocampo;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CatalogoDTO)) {
            return false;
        }

        CatalogoDTO catalogoDTO = (CatalogoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, catalogoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CatalogoDTO{" +
            "id=" + getId() +
            ", codigo='" + getCodigo() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", nuevocampo='" + getNuevocampo() + "'" +
            ", valor=" + getValor() +
            ", activo='" + getActivo() + "'" +
            ", observacion='" + getObservacion() + "'" +
            "}";
    }
}
