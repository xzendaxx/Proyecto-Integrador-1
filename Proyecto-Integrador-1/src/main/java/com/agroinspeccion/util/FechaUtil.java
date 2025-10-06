package com.agroinspeccion.util;

import java.sql.Date;
import java.time.LocalDate;

/**
 * Métodos de utilidad para conversión de fechas entre {@link LocalDate} y {@link Date}.
 */
public final class FechaUtil {

    private FechaUtil() {
    }

    public static Date toSqlDate(LocalDate fecha) {
        return fecha != null ? Date.valueOf(fecha) : null;
    }

    public static LocalDate toLocalDate(Date fecha) {
        return fecha != null ? fecha.toLocalDate() : null;
    }
}
