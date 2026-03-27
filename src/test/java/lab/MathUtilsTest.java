package lab;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MathUtilsTest {

    private MathUtils math;

    @Before
    public void setUp() {
        math = new MathUtils();
    }

    @After
    public void tearDown() {
        math = null;
    }

    @Test
    public void testAdd() {
        assertEquals(5, math.add(2, 3));
        assertEquals(0, math.add(0, 0));
        assertEquals(-3, math.add(-1, -2));
    }

    @Test
    public void testSubtract() {
        assertEquals(1, math.subtract(3, 2));
        assertEquals(-1, math.subtract(2, 3));
        assertEquals(5, math.subtract(3, -2));
    }

    @Test
    public void testMultiply() {
        assertEquals(6, math.multiply(2, 3));
        assertEquals(0, math.multiply(0, 99));
        assertEquals(-6, math.multiply(-2, 3));
    }

    @Test
    public void testDivide() {
        assertEquals(2.5, math.divide(5, 2), 0.0001);
        assertEquals(-1.0, math.divide(10, 0), 0.0001);
        assertEquals(-2.0, math.divide(4, -2), 0.0001);
    }
}
