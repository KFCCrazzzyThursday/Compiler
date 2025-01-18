
import java.util.*;

public class Type 
{
	public int m_id;	
	public String m_name;
	
	public Type m_arrayElement;
	public Vector<Type> m_recordElement;
	
	public Type()
	{
		this.m_id = 0;
		this.m_name = null;
		this.m_arrayElement = null;  
		this.m_recordElement = null; 
	}
	
	public Type(int id, String name, Type m_arrayElement)
	{
		this.m_id = id;
		this.m_name = name;
		this.m_arrayElement = m_arrayElement;
		this.m_recordElement = null;
	}
	
	public Type(int id, String name, Vector<Type> m_recordElement)
	{
		this.m_id = id;
		this.m_name = name;
		this.m_arrayElement = null;
		this.m_recordElement = m_recordElement;
	}
	
	public Type(int id, String name)
	{
		this.m_id = id;
		this.m_name = name;
		this.m_arrayElement = null;
		this.m_recordElement = null;
	}

	public Type(int id)
	{
		this.m_id = id;
		this.m_name = null;
		this.m_arrayElement = null;
		this.m_recordElement = null;
	}

	public Type(Type t)
	{
		this.m_id = t.m_id;
		this.m_name = t.m_name;
		this.m_recordElement = t.m_recordElement;
		this.m_arrayElement = t.m_arrayElement;
	}
}
