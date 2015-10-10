import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;
import java.util.ArrayList;
class Index
{
	int port = 0,admin_port = 5555, ch=0;
	String admin_ip = "";
	
	Index()
	{
		try
		{
			FileInputStream f = new FileInputStream("Admin.txt");
			while((ch=f.read())!=-1)
			admin_ip+=(char)ch;	
					
			Socket s = new Socket(admin_ip,admin_port);
			ObjectOutputStream oos=new ObjectOutputStream(s.getOutputStream());
			ObjectInputStream ois=new ObjectInputStream(s.getInputStream());
			InetAddress inet = (InetAddress)s.getLocalAddress();
			String ip = (String)inet.getHostAddress();
			oos.writeObject("PEER_GROUP_3_LOGIN");
			oos.writeObject(ip);
			
			boolean flg = (Boolean)ois.readObject();
			
			if(flg)
			{
				ArrayList info = (ArrayList)ois.readObject();
				
				int peer_count = (Integer)info.get(1);
				
				
				for(int i=0;i<peer_count;i++)
				{
					new RunPeer(i+1);
				}
			}
			else
			{
				JOptionPane.showMessageDialog(null,"Create The Peers!!!!");
			}
			s.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public static void main(String args[])
	{
		new Index();
	}
}

class RunPeer extends Thread implements ActionListener
{
	int peer_no=0;
	Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
	JComboBox jcob;
	JLabel jlimg;
	RunPeer(int j)
	{
		super();
		peer_no=j;
		start();
	}
	
	public void run()
	{
		JFrame j=new JFrame("PEER_GROUP_3_"+peer_no);
		ButtonGroup bg=new ButtonGroup();
		JRadioButton jrb=new JRadioButton("Create Congestion (Low Bandwidth)");
		JRadioButton jrb1=new JRadioButton("Default");
		JRadioButton jrb2=new JRadioButton("Free Memory (High Bandwidth)");
		JCheckBox jcb=new JCheckBox("Modify Data"); 
		JLabel jl=new JLabel("PEER_GROUP_3_"+peer_no);
		jlimg=new JLabel(new ImageIcon("Green-Fade.png"));
		JLabel jl1=new JLabel("Status");
		jcob=new JComboBox();
		
		j.setLayout(null);
		
		bg.add(jrb);
		bg.add(jrb1);
		bg.add(jrb2);
		jrb1.setSelected(true);
		
		jcob.addItem("ON");
		jcob.addItem("OFF");
				
		j.add(jrb);
		j.add(jrb1);
		j.add(jrb2);
		j.add(jcb);
		j.add(jl);
		j.add(jl1);
		j.add(jcob);
		j.add(jlimg);
		
		jl.setBounds(200,5,150,35);
		jrb.setBounds(15,50,250,35);
		jrb1.setBounds(15,100,250,35);
		jrb2.setBounds(15,150,250,35);
		jcb.setBounds(300,50,150,35);
		jl1.setBounds(300,125,100,35);
		jcob.setBounds(375,125,50,35);
		jlimg.setBounds(0,0,500,300);
		
		j.setLocation(d.width-250,peer_no*75);
		j.setSize(500,300);
		j.setVisible(true);
		
		jcob.addActionListener(this);
		
		new ServerListener(peer_no,j,jrb,jrb1,jrb2,jcb);
	}
	
	public void actionPerformed(ActionEvent ae)
	{
		if(ae.getSource()==jcob)
		{
			try
			{
				
				String status=(String)jcob.getSelectedItem();
				int peer_port=3000;
				
				if(status.equals("ON"))
				{
					jlimg.setIcon(new ImageIcon("Green-Fade.png"));
				}
				else
				{
					jlimg.setIcon(new ImageIcon("red-to-white-fade.jpg"));
				}
			
				int ch=0;
				String admin_ip="";
				
				FileInputStream f = new FileInputStream("Admin.txt");
				while((ch=f.read())!=-1)
				admin_ip+=(char)ch;
					
				Socket s = new Socket(admin_ip,5555);
				ObjectOutputStream oos=new ObjectOutputStream(s.getOutputStream());
				ObjectInputStream ois=new ObjectInputStream(s.getInputStream());
				
				oos.writeObject("PEER_GROUP_STATUS");
				oos.writeObject("1");
				oos.writeObject(peer_no);
				oos.writeObject(status.trim());
				s.close();
				
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}
