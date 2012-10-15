package de.illonis.eduras.networking;

import java.util.LinkedList;
import java.util.NoSuchElementException;

public class Buffer extends LinkedList<String> {
	public final static Object SYNCER = new Object();
	
	private static final long serialVersionUID = 1L;
	
	public String getNext() throws NoSuchElementException
	{
		return pop();
	}

}
