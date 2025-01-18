package test;

import exceptions.DividedByZeroException;
import exceptions.ExpressionException;
import exceptions.MissingOperatorException;
import exceptions.TypeMismatchedException;
import org.junit.Test;
import parser.Parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class ParserTest {

    @Test
    public void testSimpleAddition() throws ExpressionException {
        Parser parser = new Parser("3 + 4");
        double result = parser.opp();
        assertEquals(7.0, result, 0.0001);
    }

    @Test
    public void testSimpleSubtraction() throws ExpressionException {
        Parser parser = new Parser("10 - 3");
        double result = parser.opp();
        assertEquals(7.0, result, 0.0001);
    }

    @Test
    public void testTypeMismatchedException() {
        assertThrows(TypeMismatchedException.class, () -> {
            Parser parser = new Parser("32.5|65");
            parser.opp();
        });
    }

    @Test
    public void testSimpleMultiplication() throws ExpressionException {
        Parser parser = new Parser("6 * 7");
        double result = parser.opp();
        assertEquals(42.0, result, 0.0001);
    }

    @Test
    public void testSimpleDivision() throws ExpressionException {
        Parser parser = new Parser("8 / 2");
        double result = parser.opp();
        assertEquals(4.0, result, 0.0001);
    }
    @Test
    public void testSin() throws ExpressionException {
        Parser parser = new Parser("sin(3.1415926)");
        double result = parser.opp();
        assertEquals(0, result, 0.0001);
    }

    @Test
    public void testComplexExpression() throws ExpressionException {
        Parser parser = new Parser("(5 > 3) & true? 15 : 16");
        double result = parser.opp();
        assertEquals(15, result, 0.0001);
    }

    @Test(expected = DividedByZeroException.class)
    public void testDivisionByZero() throws ExpressionException {
        Parser parser = new Parser("8 / 0");
        parser.opp();
    }
    @Test
    public void testMin() throws ExpressionException {
        Parser parser = new Parser("MIN(7e3,3.14e-0)");
        double result = parser.opp();
        assertEquals(3.14, result, 0.0001);
    }

    @Test(expected = DividedByZeroException.class)
    public void testDivisionByZero2() throws ExpressionException {
        Parser parser = new Parser("1 / sin(0)");
        parser.opp();
    }

    @Test(expected = MissingOperatorException.class)
    public void testMissingOperator() throws ExpressionException {
        Parser parser = new Parser("8 8");
        parser.opp();
    }

    @Test(expected = TypeMismatchedException.class)
    public void testTypeMismatch() throws ExpressionException {
        Parser parser = new Parser("true + 3");
        parser.opp();
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("test.ParserTest");
    }
}
