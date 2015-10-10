import javax.swing.*;
import java.net.*;
import java.io.*;
import java.sql.*;
import java.awt.*;
import java.util.*;

class ServerListener extends Thread
{
	static Map<String,ArrayList> adj_peer_inf = new HashMap<String,ArrayList>();
	static int TOT_TRANS = 0;
	
	int port=0;
	int port_id=0;
	int peer_cnt=0;
	ServerSocket ss;

	ServerListener(int port,int peer_count)
	{
		super();
		port_id=port;
		peer_cnt=peer_count;
		start();
	}
	
	public void run()
	{
		try
		{
			ss=new ServerSocket(port_id);
			
			for(int ii=1;ii<=peer_cnt;ii++)
			{
				ArrayList al = new ArrayList();
				al.add(0,5.0f);
				al.add(1,10.0f); 
				al.add(2,"ON");
				adj_peer_inf.put("P_G_1_"+Integer.toString(ii),al);
			}
			
			while(true)
			{
				Socket s=ss.accept();
				
				ObjectOutputStream oos=new ObjectOutputStream(s.getOutputStream());
				ObjectInputStream ois=new ObjectInputStream(s.getInputStream());
				
				String req=(String)ois.readObject();
				System.out.println("REQ "+req);
				
				if(req.equals("DATA"))
				{
					String sender=(String)ois.readObject();
					String dest=(String)ois.readObject();
					String msg=(String)ois.readObject();
					System.out.println("DATA RECEIVED "+msg);
					Received.jta.setText("Sender : "+sender+"\n Message : "+msg);
				}
				else if(req.equals("STATUS"))
				{
					int peer=(Integer)ois.readObject();
					String status = (String)ois.readObject();
					System.out.println("STATUS UPDATE FROM "+peer+" STATUS "+status);
					
					
					if(status.equals("ON"))
					{
						Peer.jlper[peer].setForeground(new Color(0,100,254));
					}
					else
					{
						Peer.jlper[peer].setForeground(Color.red);
					}
					
					
					ArrayList al = adj_peer_inf.get("P_G_1_"+peer);
					ArrayList nu_al = new ArrayList();
					nu_al.add(0,(Float)al.get(0));
					nu_al.add(1,(Float)al.get(1));
					nu_al.add(2,status);
					adj_peer_inf.put("P_G_1_"+peer,nu_al);
					
					System.out.println("");
					System.out.println("============================================");
				}
			}
		}
		catch(BindException be)
		{
			System.exit(0);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
}
