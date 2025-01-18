/**
 * @Copyright(C) 2008 Software Engineering Laboratory (SELAB), Department of Computer
 * Science, SUN YAT-SEN UNIVERSITY. All rights reserved.
**/

package parser;

import exceptions.ExpressionException;

/**
 * Main program of the expression based calculator ExprEval
 *
 * @author 李英骏 21307099
 * @version 1.00 (Last update: 2024.5.18)
**/
public class Calculator
{
	/**
	 * The testmain program of the parser. You should substitute the body of this method
	 * with your experiment result.
	 *
	 * @param expression  user input to the calculator from GUI.
	 * @return  if the expression is well-formed, return the evaluation result of it.
	 * @throws ExpressionException  if the expression has error, a corresponding
	 *                              exception will be raised.
	**/
	public double calculate(String expression) throws ExpressionException {
		try {
			return new Parser(expression).opp();
		} catch (Exception e) {
			throw e;
		}
	}
}
