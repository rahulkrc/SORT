import java.util.*;

class Node implements java.io.Serializable
{
	public ArrayList reg_node;
	public Map<String,String[]> node_info; 
	Node()
	{
		reg_node = new ArrayList();
		node_info = new HashMap<String,String[]>();
	}
	
	public boolean checkNode(String name)
	{
		boolean flg = false;
		System.out.println("DATA "+reg_node);
		if(!reg_node.contains(name))
		{
			flg = true;
		}
		
	return flg;
	}
	
	public void insertNode(String name,String[] info)
	{
		node_info.put(name,info);
		reg_node.add(name);
	}
	
	
}