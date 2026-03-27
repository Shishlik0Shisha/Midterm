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
    public void testAdd_basic() {
        assertEquals(5, math.add(2, 3));
    }

    @Test
    public void testAdd_negative() {
        assertEquals(-1, math.add(2, -3));
    }

    @Test
    public void testSubtract_basic() {
        assertEquals(7, math.subtract(10, 3));
    }

    @Test
    public void testSubtract_negativeResult() {
        assertEquals(-5, math.subtract(5, 10));
    }

    @Test
    public void testMultiply_basic() {
        assertEquals(12, math.multiply(3, 4));
    }

    @Test
    public void testMultiply_byZero() {
        assertEquals(0, math.multiply(999, 0));
    }

    @Test
    public void testDivide_normal() {
        assertEquals(2.5, math.divide(5, 2), 1e-9);
    }

    @Test
    public void testDivide_negative() {
        assertEquals(-2.5, math.divide(-5, 2), 1e-9);
    }

    @Test
    public void testDivide_byZero_returnsMinusOne() {
        assertEquals(-1.0, math.divide(10, 0), 0.0);
    }
}
