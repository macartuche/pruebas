package ec.gob.mspz7.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CatalogoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Catalogo getCatalogoSample1() {
        return new Catalogo().id(1L).codigo("codigo1").descripcion("descripcion1").nuevocampo("nuevocampo1");
    }

    public static Catalogo getCatalogoSample2() {
        return new Catalogo().id(2L).codigo("codigo2").descripcion("descripcion2").nuevocampo("nuevocampo2");
    }

    public static Catalogo getCatalogoRandomSampleGenerator() {
        return new Catalogo()
            .id(longCount.incrementAndGet())
            .codigo(UUID.randomUUID().toString())
            .descripcion(UUID.randomUUID().toString())
            .nuevocampo(UUID.randomUUID().toString());
    }
}
