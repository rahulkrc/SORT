import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
class ClientForm extends JFrame 
{
	JTabbedPane jtp;
	static String usr_nme="";
	static int peer_cnt=0;
	static String peer_ip="";
	ClientForm(String U_Name,String[] neigh_nodes,int peer_count,String ip,int port)
	{
		super("CLIENT :"+U_Name);
		this.setLayout(null);
		
		usr_nme=U_Name;
		peer_cnt=peer_count;
		peer_ip=ip;
		
		jtp=new JTabbedPane();
		jtp.addTab("Send",new Send(U_Name,neigh_nodes));
		jtp.addTab("Peer_Info",new Peer(peer_count));
		jtp.addTab("Received",new Received());
				
		this.add(jtp);
				
		jtp.setBounds(0,0,500,500);
				
		this.setSize(400,400);
		this.setVisible(true);
		
		new ServerListener(port,peer_count);
		
		
		System.out.println("User Name "+U_Name);
	}
	
	
}