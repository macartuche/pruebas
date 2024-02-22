package ec.gob.mspz7.service.dto;

public class GuardarItem {

    private String nombre;
    private String codigo;
    private String codigoCatalogo;
    private Long catalogoId;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Long getCatalogoId() {
        return catalogoId;
    }

    public void setCatalogoId(Long catalogoId) {
        this.catalogoId = catalogoId;
    }

    public String getCodigoCatalogo() {
        return codigoCatalogo;
    }

    public void setCodigoCatalogo(String codigoCatalogo) {
        this.codigoCatalogo = codigoCatalogo;
    }

    @Override
    public String toString() {
        return (
            "GuardarItem [nombre=" +
            nombre +
            ", codigo=" +
            codigo +
            ", codigoCatalogo=" +
            codigoCatalogo +
            ", catalogoId=" +
            catalogoId +
            "]"
        );
    }
}
