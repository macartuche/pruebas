package ec.gob.mspz7.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * The Item entity.
 * @author macartuche
 */
@Entity
@Table(name = "item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "item")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Item implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    /**
     * codigo
     */
    @NotNull
    @Column(name = "codigo", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String codigo;

    /**
     * codigoCatalogo
     */
    @NotNull
    @Column(name = "codigo_catalogo", nullable = false)
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String codigoCatalogo;

    /**
     * descripcion
     */
    @Column(name = "descripcion")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    private Catalogo catalogo;

    @JsonIgnoreProperties(value = { "region" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "region")
    @org.springframework.data.annotation.Transient
    private Pais pais;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Item id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return this.codigo;
    }

    public Item codigo(String codigo) {
        this.setCodigo(codigo);
        return this;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigoCatalogo() {
        return this.codigoCatalogo;
    }

    public Item codigoCatalogo(String codigoCatalogo) {
        this.setCodigoCatalogo(codigoCatalogo);
        return this;
    }

    public void setCodigoCatalogo(String codigoCatalogo) {
        this.codigoCatalogo = codigoCatalogo;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Item descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Catalogo getCatalogo() {
        return this.catalogo;
    }

    public void setCatalogo(Catalogo catalogo) {
        this.catalogo = catalogo;
    }

    public Item catalogo(Catalogo catalogo) {
        this.setCatalogo(catalogo);
        return this;
    }

    public Pais getPais() {
        return this.pais;
    }

    public void setPais(Pais pais) {
        if (this.pais != null) {
            this.pais.setRegion(null);
        }
        if (pais != null) {
            pais.setRegion(this);
        }
        this.pais = pais;
    }

    public Item pais(Pais pais) {
        this.setPais(pais);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Item)) {
            return false;
        }
        return getId() != null && getId().equals(((Item) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Item{" +
            "id=" + getId() +
            ", codigo='" + getCodigo() + "'" +
            ", codigoCatalogo='" + getCodigoCatalogo() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            "}";
    }
}
