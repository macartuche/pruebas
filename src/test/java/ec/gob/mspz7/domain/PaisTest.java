package ec.gob.mspz7.domain;

import static ec.gob.mspz7.domain.ItemTestSamples.*;
import static ec.gob.mspz7.domain.PaisTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ec.gob.mspz7.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PaisTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Pais.class);
        Pais pais1 = getPaisSample1();
        Pais pais2 = new Pais();
        assertThat(pais1).isNotEqualTo(pais2);

        pais2.setId(pais1.getId());
        assertThat(pais1).isEqualTo(pais2);

        pais2 = getPaisSample2();
        assertThat(pais1).isNotEqualTo(pais2);
    }

    @Test
    void regionTest() throws Exception {
        Pais pais = getPaisRandomSampleGenerator();
        Item itemBack = getItemRandomSampleGenerator();

        pais.setRegion(itemBack);
        assertThat(pais.getRegion()).isEqualTo(itemBack);

        pais.region(null);
        assertThat(pais.getRegion()).isNull();
    }
}
