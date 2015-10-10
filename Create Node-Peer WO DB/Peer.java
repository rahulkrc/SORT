import java.util.*;

class Peer implements java.io.Serializable
{
	public ArrayList reg_peer;
	public Map<String,ArrayList> peer_info; 
	Peer()
	{
		reg_peer = new ArrayList();
		peer_info = new HashMap<String,ArrayList>();
	}
	
	public boolean checkPeer(String name)
	{
		boolean flg = false;
		System.out.println("DATA "+reg_peer);
		if(!reg_peer.contains(name))
		{
			flg = true;
		}
		
	return flg;
	}
	
	public void insertPeer(String name,ArrayList info)
	{
		peer_info.put(name,info);
		reg_peer.add(name);
	}
	
	
}