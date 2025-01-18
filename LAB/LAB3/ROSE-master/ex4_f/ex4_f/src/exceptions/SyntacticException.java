// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   SyntacticException.java

package exceptions;

// Referenced classes of package exceptions:
//			OberonException

public class SyntacticException extends OberonException {

    public SyntacticException(int i, int j) {
	this(
		(new StringBuilder())
			.append("\nSyntax error has been found.\nThe error position is Line ")
			.append(i).append(" Column ").append(j).toString());
    }

    public SyntacticException(String s) {
	super(s);
    }
}
