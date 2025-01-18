// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   MissingLeftParenthesisException.java

package exceptions;

// Referenced classes of package exceptions:
//			SyntacticException

public class MissingSemicolonException extends SyntacticException {

	public MissingSemicolonException(int i, int j) {
		this((new StringBuilder()).append("\nMissing semicolon.\nThe error position is Line ").append(i)
				.append(" Column ").append(j).toString());
	}

	public MissingSemicolonException(String s) {
		super(s);
	}
}
