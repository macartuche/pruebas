package ec.gob.mspz7.domain;

import static ec.gob.mspz7.domain.CatalogoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ec.gob.mspz7.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CatalogoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Catalogo.class);
        Catalogo catalogo1 = getCatalogoSample1();
        Catalogo catalogo2 = new Catalogo();
        assertThat(catalogo1).isNotEqualTo(catalogo2);

        catalogo2.setId(catalogo1.getId());
        assertThat(catalogo1).isEqualTo(catalogo2);

        catalogo2 = getCatalogoSample2();
        assertThat(catalogo1).isNotEqualTo(catalogo2);
    }
}
