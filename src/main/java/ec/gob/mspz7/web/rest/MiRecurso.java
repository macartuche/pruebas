package ec.gob.mspz7.web.rest;

import ec.gob.mspz7.domain.Item;
import ec.gob.mspz7.service.MiServicio;
import ec.gob.mspz7.service.dto.CatalogoDTO;
import ec.gob.mspz7.service.dto.GuardarItem;
import ec.gob.mspz7.service.dto.ItemDTO;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/servicios")
public class MiRecurso {

    private final MiServicio miServicio;
    private final Logger log = LoggerFactory.getLogger(MiRecurso.class);

    public MiRecurso(MiServicio miServicio) {
        this.miServicio = miServicio;
    }

    @GetMapping("/items-codigoCatalogo/{codigoCatalogo}")
    public ResponseEntity<List<Item>> itemsPorCodigoCatalogo(@RequestParam String codigoCatalogo) {
        log.debug("Rest codigoCatalogo {}", codigoCatalogo);
        return ResponseEntity.ok().body(miServicio.obtenerPorCodigoCatalogo(codigoCatalogo));
    }

    @PostMapping("/guardar-item")
    public ResponseEntity<ItemDTO> guardarItem(@RequestBody GuardarItem request) {
        log.debug("Rest guardarItem {}", request);

        CatalogoDTO catalogo = new CatalogoDTO();
        catalogo.setId(request.getCatalogoId());

        ItemDTO dto = new ItemDTO();
        dto.setCodigo(request.getCodigo());
        dto.setDescripcion(request.getNombre());
        dto.setCatalogo(catalogo);
        dto.setCodigoCatalogo(request.getCodigoCatalogo());

        return ResponseEntity.ok().body(miServicio.guardar(dto));
    }
}
