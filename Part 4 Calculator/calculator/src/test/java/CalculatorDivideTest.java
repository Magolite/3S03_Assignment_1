
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CalculatorDivideTest {
    private Calculator calculator;
    @BeforeEach
    public void setUp() {
        calculator = new Calculator();
    }
    
    //start initial test set here
    // divide two posititive numbers
    @Test
    public void testDividePositiveIntegers() {
        assertEquals(4.0, calculator.divide(8, 2), "should be 4.0");
    }

    // divide by 1
    @Test
    public void testDivideByOne() {
        assertEquals(7.0, calculator.divide(7, 1), "should be 7.0");
    }

    // divide for decimal result
    @Test
    public void testDivideResultIsDecimal() {
        assertEquals(2.5, calculator.divide(5, 2), "should be 2.5");
    }

    // divide negative by positive
    @Test
    public void testDivideNegativeByPositive() {
        assertEquals(-3.0, calculator.divide(-9, 3), "should be -3.0");
    }
    
 //second improved test set

    // dividing by zero throws ArithmeticException
    @Test
    public void testDivideByZeroThrowsException() {
        assertThrows(ArithmeticException.class, () -> calculator.divide(10, 0),
                "should throw ArithmeticException");
    }

    // dividing0 by anything returns 0
    @Test
    public void testDivideZeroByNumber() {
        assertEquals(0.0, calculator.divide(0, 5), "should be 0.0");
    }

    // division of two negatives is positive
    @Test
    public void testDivideNegativeByNegative() {
        assertEquals(2.0, calculator.divide(-6, -3), "should be 2.0");
    }
}
