package Florea_Flaviu_ISS.domain;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Optional;

public enum Genuri implements Serializable {
    Comedie(0), Drama(1), Romantic(2), Musical(3);
    private final int value;

    Genuri(int value) {
        this.value = value;
    }

    public static Genuri valueOf(int value) {
        Optional<Genuri> genuri = Arrays.stream(values())
                .filter(a -> a.value == value)
                .findFirst();
        return genuri.orElse(null);
    }
}
