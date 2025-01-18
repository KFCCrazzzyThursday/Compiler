// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   IllegalOctalException.java

package exceptions;

// Referenced classes of package exceptions:
//			LexicalException

public class IllegalOctalException extends LexicalException {

    public IllegalOctalException(int i, int j) {
	this(
		(new StringBuilder())
			.append("\nThe octal integer  is illegal.\nThe error position is Line ")
			.append(i).append(" Column ").append(j).toString());
    }

    public IllegalOctalException(String s) {
	super(s);
    }
}
