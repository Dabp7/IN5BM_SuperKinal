package org.diegobercian.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

public final class Validation {
    private Validation() { }
    public static Object parse(String label, String type, int length, String input, boolean optional) {
        String value = input == null ? "" : input.trim();
        if (value.isEmpty() && !optional) throw new IllegalArgumentException(label + ": complete este campo.");
        try {
            if ("int".equals(type)) {
                int number = Integer.parseInt(value);
                if (number <= 0) throw new IllegalArgumentException(label + ": ingrese un entero mayor que cero.");
                return number;
            }
            if ("decimal".equals(type)) {
                BigDecimal amount = new BigDecimal(value).setScale(2, RoundingMode.UNNECESSARY);
                if (amount.signum() < 0 || amount.precision() > 10) throw new IllegalArgumentException(label + ": use un importe entre 0 y 99999999.99.");
                return amount;
            }
            if ("date".equals(type)) return LocalDate.parse(value);
            if (value.length() > length) throw new IllegalArgumentException(label + ": máximo " + length + " caracteres.");
            String name=java.text.Normalizer.normalize(label,java.text.Normalizer.Form.NFD).replaceAll("\\p{M}","").toLowerCase(java.util.Locale.ROOT);
            if (!value.isEmpty() && (name.contains("correo") || name.contains("email")) && !value.matches("[^\\s@]+@[^\\s@]+\\.[^\\s@]+")) throw new IllegalArgumentException(label + ": ingrese un correo válido.");
            if (!value.isEmpty() && (name.contains("telefono") || name.contains("numeroPrincipal".toLowerCase()) || name.contains("numerosecundario")) && !value.matches("[0-9]{8}")) throw new IllegalArgumentException(label + ": ingrese 8 dígitos.");
            return value;
        } catch (NumberFormatException e) { throw new IllegalArgumentException(label + ": ingrese un número válido."); }
        catch (ArithmeticException e) { throw new IllegalArgumentException(label + ": use como máximo dos decimales."); }
        catch (java.time.format.DateTimeParseException e) { throw new IllegalArgumentException(label + ": use una fecha válida AAAA-MM-DD."); }
    }
}
