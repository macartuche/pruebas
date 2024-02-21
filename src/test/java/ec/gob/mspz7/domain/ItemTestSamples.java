package ec.gob.mspz7.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ItemTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Item getItemSample1() {
        return new Item().id(1L).codigo("codigo1").codigoCatalogo("codigoCatalogo1").descripcion("descripcion1");
    }

    public static Item getItemSample2() {
        return new Item().id(2L).codigo("codigo2").codigoCatalogo("codigoCatalogo2").descripcion("descripcion2");
    }

    public static Item getItemRandomSampleGenerator() {
        return new Item()
            .id(longCount.incrementAndGet())
            .codigo(UUID.randomUUID().toString())
            .codigoCatalogo(UUID.randomUUID().toString())
            .descripcion(UUID.randomUUID().toString());
    }
}
