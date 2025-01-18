// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   TypeMismatchedException.java

package exceptions;

// Referenced classes of package exceptions:
//			SemanticException

public class DivideByZeroException extends SemanticException {

    public DivideByZeroException(int i, int j) {
	this(
		(new StringBuilder())
			.append("\nDivide by zero. \nThe error position is Line ")
			.append(i).append(" Column ").append(j).toString());
    }

    public DivideByZeroException(String s) {
	super(s);
    }
}
