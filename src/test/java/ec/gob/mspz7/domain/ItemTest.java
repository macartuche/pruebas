package ec.gob.mspz7.domain;

import static ec.gob.mspz7.domain.CatalogoTestSamples.*;
import static ec.gob.mspz7.domain.ItemTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ec.gob.mspz7.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Item.class);
        Item item1 = getItemSample1();
        Item item2 = new Item();
        assertThat(item1).isNotEqualTo(item2);

        item2.setId(item1.getId());
        assertThat(item1).isEqualTo(item2);

        item2 = getItemSample2();
        assertThat(item1).isNotEqualTo(item2);
    }

    @Test
    void catalogoTest() throws Exception {
        Item item = getItemRandomSampleGenerator();
        Catalogo catalogoBack = getCatalogoRandomSampleGenerator();

        item.setCatalogo(catalogoBack);
        assertThat(item.getCatalogo()).isEqualTo(catalogoBack);

        item.catalogo(null);
        assertThat(item.getCatalogo()).isNull();
    }
}
