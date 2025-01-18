// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   SemanticException.java

package exceptions;

// Referenced classes of package exceptions:
//			OberonException

public class SemanticException extends OberonException {

    public SemanticException(int i, int j) {
	this(
		(new StringBuilder())
			.append("\nSemantic error has been found.\n The error position is Line ")
			.append(i).append(" Column ").append(j).toString());
    }

    public SemanticException(String s) {
	super(s);
    }
}
