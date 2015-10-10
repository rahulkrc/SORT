import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.*;


class CreatePeer 
{
	int[] peer=new int[3];
	int i=0;
	
	static Connection con=null;
	Peer p = null;
	CreatePeer()
	{
		p = new Peer();
		
		for(i=0;i<3;i++)
		{
			getPeerNum(i);
		}
		
		if(peer.length==3)
		{
			try
			{
				for(int ii=0;ii<3;ii++)
				{
					int j = ii+1;
					boolean flg = p.checkPeer(Integer.toString(j));
					if(flg)
					{
						ArrayList info = new ArrayList();
						info.add(0,"IP");
						info.add(1,peer[ii]);
						info.add(2,"ON");
						p.insertPeer(Integer.toString(j),info);
					}
					
					
				}
				try
				{
					FileOutputStream fileOut = new FileOutputStream("peer.ser");
		         	ObjectOutputStream out = new ObjectOutputStream(fileOut);
		         	out.writeObject(p);
		         	out.close();
		         	fileOut.close();
		         	JOptionPane.showMessageDialog(null,"Successfully Stored Data to peer.ser !!!!");
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				
				
			}	
			catch(Exception e)
			{
				e.printStackTrace();
			}		
		}		
	}
	
	public void getPeerNum(int j)
	{
		try
		{
			int k=j+1;
			String per="";
			per=JOptionPane.showInputDialog(null,"Number of Peers in Peer Group "+k);
			peer[j]=Integer.parseInt(per);
			if(peer[j]<=0)
			{
				JOptionPane.showMessageDialog(null,"Invalid Entry !!!");
				getPeerNum(j);
			}		
		}
		catch(NumberFormatException ee)
		{
			JOptionPane.showMessageDialog(null,"Invalid Entry !!!");
			getPeerNum(j);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	public static void main(String args[])
	{
		new CreatePeer();
	}
}