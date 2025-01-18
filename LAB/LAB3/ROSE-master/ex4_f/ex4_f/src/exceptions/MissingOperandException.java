// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   MissingOperandException.java

package exceptions;

// Referenced classes of package exceptions:
//			SyntacticException

public class MissingOperandException extends SyntacticException {

    public MissingOperandException(int i, int j) {
	this((new StringBuilder())
		.append("\nMiss operand.\n The error position is Line ")
		.append(i).append(" Column ").append(j).toString());
    }

    public MissingOperandException(String s) {
	super(s);
    }
}
