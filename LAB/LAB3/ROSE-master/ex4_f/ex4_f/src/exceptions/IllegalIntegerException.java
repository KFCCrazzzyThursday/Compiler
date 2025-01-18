// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   IllegalIntegerException.java

package exceptions;

// Referenced classes of package exceptions:
//			LexicalException

public class IllegalIntegerException extends LexicalException {

    public IllegalIntegerException(int i, int j) {
	this(
		(new StringBuilder())
			.append("\nThe integer is illegal.\nThe error position is Line ")
			.append(i).append(" Column ").append(j).toString());
    }

    public IllegalIntegerException(String s) {
	super(s);
    }
}
