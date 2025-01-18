// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   LexicalException.java

package exceptions;

// Referenced classes of package exceptions:
//			OberonException

public class LexicalException extends OberonException {

    public LexicalException(int i, int j) {
	this(
		(new StringBuilder())
			.append("\nLexicalExceptions have been found.\nThe error position is Line ")
			.append(i).append(" Column ").append(j).toString());
    }

    public LexicalException(String s) {
	super(s);
    }
}
