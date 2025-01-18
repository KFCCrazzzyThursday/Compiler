// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   OberonException.java

package exceptions;

public class OberonException extends Exception {

    public OberonException(int i, int j) {
	this(
		(new StringBuilder())
			.append("\nErrors have been found.\nThe error position is Line ")
			.append(i).append(" Column ").append(j).toString());
    }

    public OberonException(String s) {
	super(s);
    }
}
