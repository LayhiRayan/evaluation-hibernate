package ma.projet.util;

import java.time.LocalDate;

public final class Dates {
    private Dates() {}
    public static void intervalle(LocalDate debut, LocalDate fin) {
        if (debut == null || fin == null || fin.isBefore(debut)) {
            throw new IllegalArgumentException("Dates obligatoires, avec fin supérieure ou égale au début.");
        }
    }
}
