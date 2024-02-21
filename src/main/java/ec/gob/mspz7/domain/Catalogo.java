package ec.gob.mspz7.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * The Catalogo entity.
 * @author macartuche
 */
@Entity
@Table(name = "catalogo")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "catalogo")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Catalogo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    /**
     * Codigo de catalogo
     */
    @NotNull
    @Column(name = "codigo", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String codigo;

    /**
     * descripcion
     */
    @Column(name = "descripcion")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String descripcion;

    /**
     * nuevocampo
     */
    @Column(name = "nuevocampo")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String nuevocampo;

    /**
     * valor
     */
    @Column(name = "valor", precision = 21, scale = 2)
    private BigDecimal valor;

    /**
     * activo
     */
    @Column(name = "activo")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean activo;

    /**
     * observacion
     */
    @Column(name = "observacion")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String observacion;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Catalogo id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return this.codigo;
    }

    public Catalogo codigo(String codigo) {
        this.setCodigo(codigo);
        return this;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Catalogo descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNuevocampo() {
        return this.nuevocampo;
    }

    public Catalogo nuevocampo(String nuevocampo) {
        this.setNuevocampo(nuevocampo);
        return this;
    }

    public void setNuevocampo(String nuevocampo) {
        this.nuevocampo = nuevocampo;
    }

    public BigDecimal getValor() {
        return this.valor;
    }

    public Catalogo valor(BigDecimal valor) {
        this.setValor(valor);
        return this;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Boolean getActivo() {
        return this.activo;
    }

    public Catalogo activo(Boolean activo) {
        this.setActivo(activo);
        return this;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getObservacion() {
        return this.observacion;
    }

    public Catalogo observacion(String observacion) {
        this.setObservacion(observacion);
        return this;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Catalogo)) {
            return false;
        }
        return getId() != null && getId().equals(((Catalogo) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Catalogo{" +
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
