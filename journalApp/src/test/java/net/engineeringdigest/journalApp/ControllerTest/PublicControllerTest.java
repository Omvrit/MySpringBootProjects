package net.engineeringdigest.journalApp.ControllerTest;

import net.engineeringdigest.journalApp.controller.PublicController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
@SpringBootTest
public class PublicControllerTest{
    @Test
    public void testHealthCheck() {
        assertEquals("OK", new PublicController().healthCheck());
    }
    @ParameterizedTest
    @CsvSource(
            {
                    "1,2,3",
                    "2,3,5",
                    "3,4,7"
            }
    )
    public void testAdd(int a,int b,int expected) {
        assertEquals(expected,a+b);
    }
    @ParameterizedTest
    @ValueSource(
        ints = {
             6,7,8,9
        }
    )
    public void greaterThan5(int a){
        assertTrue(a>5,"failed on "+a);
    }


}

