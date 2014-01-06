package de.mirkosertic.gameengine.expression.ge;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RPNEvaluatorTest {

    private Scanner scanner;
    private Parser parser;
    private RPNEvaluator evaluator;

    @Before
    public void setup() {
        scanner = new Scanner();
        parser = new Parser();
        evaluator = new RPNEvaluator(new DefaultVariableResolver(), new DefaultFunctionRegistry());

    }

    @Test
    public void testSimpleAdd() {
        int theResult = (Integer) evaluator.evaluate(parser.parse(scanner.scan("1+2")));
        assertEquals(3, theResult);
    }

    @Test
    public void testSimpleDoubleAdd() {
        double theResult = (Double) evaluator.evaluate(parser.parse(scanner.scan("1.0+2")));
        assertEquals(3, theResult, 0);
    }


    @Test
    public void testSimpleSubstract() {
        int theResult = (Integer) evaluator.evaluate(parser.parse(scanner.scan("2-3")));
        assertEquals(-1, theResult);
    }

    @Test
    public void testSimpleDoubleSubstract() {
        double theResult = (Double) evaluator.evaluate(parser.parse(scanner.scan("2.0-3")));
        assertEquals(-1, theResult, 0);
    }

    @Test
    public void testSimpleMultiply() {
        int theResult = (Integer) evaluator.evaluate(parser.parse(scanner.scan("2*6")));
        assertEquals(12, theResult);
    }

    @Test
    public void testSimpleDoubleMultiply() {
        double theResult = (Double) evaluator.evaluate(parser.parse(scanner.scan("2.0*6")));
        assertEquals(12, theResult, 0);
    }

    @Test
    public void testSimpleDivide() {
        int theResult = (Integer) evaluator.evaluate(parser.parse(scanner.scan("21/7")));
        assertEquals(3, theResult);
    }

    @Test
    public void testSimpleDoubleDivide() {
        double theResult = (Double) evaluator.evaluate(parser.parse(scanner.scan("21.0/7")));
        assertEquals(3, theResult, 0);
    }

    @Test
    public void testPrecedence() {
        int theResult = (Integer) evaluator.evaluate(parser.parse(scanner.scan("1+2*4")));
        assertEquals(9, theResult);
    }

    @Test
    public void testParenthesis() {
        int theResult = (Integer) evaluator.evaluate(parser.parse(scanner.scan("(1+2)*4/(2*3)")));
        assertEquals(2, theResult);
    }

    @Test
    public void testStringConcatenate() {
        String theResult = (String) evaluator.evaluate(parser.parse(scanner.scan("A|C|(2*4)")));
        assertEquals("AC8", theResult);
    }

    @Test
    public void testSimpleAddWithVariable() {
        evaluator = new RPNEvaluator(new VariableResolver() {
            @Override
            public Object resolveVariable(String aVariableName) {
                if ("X".equals(aVariableName)) {
                    return 8;
                }
                return null;
            }
        }, new DefaultFunctionRegistry());
        int theResult = (Integer) evaluator.evaluate(parser.parse(scanner.scan("X+2")));
        assertEquals(10, theResult);
    }

    @Test
    public void testSimpleAddWithCoplexVariable() {
        evaluator = new RPNEvaluator(new VariableResolver() {
            @Override
            public Object resolveVariable(String aVariableName) {
                if ("X.A".equals(aVariableName)) {
                    return 8;
                }
                return null;
            }
        }, new DefaultFunctionRegistry());
        int theResult = (Integer) evaluator.evaluate(parser.parse(scanner.scan("X.A+2")));
        assertEquals(10, theResult);
    }

    @Test
    public void testMaxFuction() {
        int theResult = (Integer) evaluator.evaluate(parser.parse(scanner.scan("2+max(2,5)")));
        assertEquals(7, theResult);
    }

    @Test
    public void testMinFuction() {
        int theResult = (Integer) evaluator.evaluate(parser.parse(scanner.scan("2+min(2,5)")));
        assertEquals(4, theResult);
    }
}
