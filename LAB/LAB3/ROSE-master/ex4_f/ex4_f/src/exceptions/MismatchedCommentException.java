// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   MismatchedCommentException.java

package exceptions;

// Referenced classes of package exceptions:
//			LexicalException

public class MismatchedCommentException extends LexicalException {

    public MismatchedCommentException(int i, int j) {
	this(
		(new StringBuilder())
			.append("\nComment is Mismatched.\nThe error position is Line ")
			.append(i).append(" Column ").append(j).toString());
    }

    public MismatchedCommentException(String s) {
	super(s);
    }
}
