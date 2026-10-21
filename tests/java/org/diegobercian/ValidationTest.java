package org.diegobercian;
import static org.junit.Assert.*;
import org.junit.Test;
import org.diegobercian.security.PasswordHash;
import org.diegobercian.service.Validation;
import org.diegobercian.service.AuthService;
import java.math.BigDecimal;
import java.time.LocalDate;

public class ValidationTest {
    @Test public void moneyIsExact() {assertEquals(new BigDecimal("0.10"),Validation.parse("costo","decimal",10,"0.10",false));}
    @Test public void validLeapDay() {assertEquals(LocalDate.of(2024,2,29),Validation.parse("fecha","date",0,"2024-02-29",false));}
    @Test public void rejectsInvalidInputs() {
        for(String value:new String[]{"","abc","0","-1","2147483648"}) assertThrows(IllegalArgumentException.class,()->Validation.parse("cantidad","int",0,value,false));
        for(String value:new String[]{"-0.01","1.001","100000000","NaN"}) assertThrows(IllegalArgumentException.class,()->Validation.parse("costo","decimal",10,value,false));
        assertThrows(IllegalArgumentException.class,()->Validation.parse("fecha","date",0,"2026-02-29",false));
        assertThrows(IllegalArgumentException.class,()->Validation.parse("nombre","text",3,"largo",false));
        assertThrows(IllegalArgumentException.class,()->Validation.parse("correo","text",60,"bad",false));
        assertThrows(IllegalArgumentException.class,()->AuthService.validate("user",new char[0],true));
    }
    @Test public void saltedPasswordsAndVerification() throws Exception {
        String one=PasswordHash.hash("una clave larga 2026".toCharArray()),two=PasswordHash.hash("una clave larga 2026".toCharArray());
        assertNotEquals(one,two);assertTrue(PasswordHash.verify("una clave larga 2026".toCharArray(),one));
        assertFalse(PasswordHash.verify("otra clave".toCharArray(),one));assertFalse(PasswordHash.verify("123456".toCharArray(),"123456"));
        assertFalse(PasswordHash.verify("x".toCharArray(),"pbkdf2-sha256$0$x$x"));
    }
}
