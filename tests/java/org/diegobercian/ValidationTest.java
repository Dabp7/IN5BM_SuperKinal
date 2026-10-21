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
        assertThrows(IllegalArgumentException.class,()->Validation.parse("Teléfono","text",8,"abcdefgh",false));
        assertThrows(IllegalArgumentException.class,()->AuthService.validate("user",new char[0],true));
    }
    @Test public void sqlTableIdentifiersUsePortableCasing() throws Exception {
        String schema=new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get("database/schema.sql")),java.nio.charset.StandardCharsets.UTF_8);
        java.util.Map<String,String> tables=new java.util.HashMap<>();
        java.util.regex.Matcher ddl=java.util.regex.Pattern.compile("CREATE TABLE (\\w+)").matcher(schema);
        while(ddl.find())tables.put(ddl.group(1).toLowerCase(java.util.Locale.ROOT),ddl.group(1));
        java.util.regex.Matcher references=java.util.regex.Pattern.compile("(?i)(?:FROM|JOIN|UPDATE|INTO|REFERENCES)\\s+(\\w+)").matcher(schema);
        while(references.find()) {
            String name=references.group(1), canonical=tables.get(name.toLowerCase(java.util.Locale.ROOT));
            if(canonical!=null)assertEquals(canonical,name);
        }
    }
    @Test public void saltedPasswordsAndVerification() throws Exception {
        String one=PasswordHash.hash("una clave larga 2026".toCharArray()),two=PasswordHash.hash("una clave larga 2026".toCharArray());
        assertNotEquals(one,two);assertTrue(PasswordHash.verify("una clave larga 2026".toCharArray(),one));
        assertFalse(PasswordHash.verify("otra clave".toCharArray(),one));assertFalse(PasswordHash.verify("123456".toCharArray(),"123456"));
        assertFalse(PasswordHash.verify("x".toCharArray(),"pbkdf2-sha256$0$x$x"));
    }
}
