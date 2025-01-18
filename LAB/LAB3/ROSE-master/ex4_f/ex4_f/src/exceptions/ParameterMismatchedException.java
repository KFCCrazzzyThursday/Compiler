// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ParameterMismatchedException.java

package exceptions;

// Referenced classes of package exceptions:
//			SemanticException

public class ParameterMismatchedException extends SemanticException {

    public ParameterMismatchedException(int i, int j) {
	this(
		(new StringBuilder())
			.append("\nThe number of parameters is incorrect. \nThe error position is Line ")
			.append(i).append(" Column ").append(j).toString());
    }

    public ParameterMismatchedException(String s) {
	super(s);
    }
}
