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
	
	public static int adj_peer_count=0;
	public static String adj_peer_ip="";
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
			oos.writeObject("PEER_GROUP_1_LOGIN");
			oos.writeObject(ip);
			
			boolean flg = (Boolean)ois.readObject();
			
			if(flg)
			{
				ArrayList info = (ArrayList)ois.readObject();
				ArrayList adj_info = (ArrayList)ois.readObject();
				int peer_count = (Integer)info.get(1);
				adj_peer_count = (Integer)adj_info.get(1);
				adj_peer_ip = (String)adj_info.get(0);
				
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
	JLabel[] jlp;
	JLabel[] jlper;
	
	Font f=new Font("Dialog",Font.PLAIN,20);
	Font f1=new Font("Dialog",Font.BOLD,20);
	
	RunPeer(int j)
	{
		super();
		peer_no=j;
		start();
	}
	
	public void run()
	{
		JFrame j=new JFrame("PEER_GROUP_1_"+peer_no);
		ButtonGroup bg=new ButtonGroup();
		JRadioButton jrb=new JRadioButton("Create Congestion (Low Bandwidth)");
		JRadioButton jrb1=new JRadioButton("Default");
		JRadioButton jrb2=new JRadioButton("Free Memory (High Bandwidth)");
		JCheckBox jcb=new JCheckBox("Modify Data"); 
		JLabel jl=new JLabel("PEER_GROUP_1_"+peer_no);
		jlimg=new JLabel(new ImageIcon("Green-Fade.png"));
		JLabel jl1=new JLabel("Status");
		jcob=new JComboBox();
		
		jlp=new JLabel[Index.adj_peer_count];
		jlper=new JLabel[Index.adj_peer_count];
		
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
		
		
		jl.setBounds(200,5,150,35);
		jrb.setBounds(15,50,250,35);
		jrb1.setBounds(15,100,250,35);
		jrb2.setBounds(15,150,250,35);
		jcb.setBounds(300,50,150,35);
		jl1.setBounds(300,125,100,35);
		jcob.setBounds(375,125,50,35);
		
		
		
		for(int i=0;i<Index.adj_peer_count;i++)
		{
			int jj=i+1;
			jlp[i]=new JLabel("P_G_2_"+jj);
			j.add(jlp[i]);
			jlp[i].setBounds(150,240+(i*50),100,30);
			jlp[i].setFont(f);
			
			jlper[i]=new JLabel("100%");
			j.add(jlper[i]);
			jlper[i].setBounds(325,240+(i*50),100,30);
			jlper[i].setFont(f1);
			jlper[i].setForeground(new Color(0,100,254));
			
		}
		
		j.add(jlimg);
		jlimg.setBounds(0,0,500,300+Index.adj_peer_count*100);
		
		
		j.setLocation(d.width/12,peer_no*75);
		j.setSize(500,300+Index.adj_peer_count*100);
		j.setVisible(true);
		
		jcob.addActionListener(this);
		
		new ServerListener(peer_no,Index.adj_peer_count,jrb,jrb1,jrb2,jlper,jcb);
	}
	
	
	
	public void actionPerformed(ActionEvent ae)
	{
		if(ae.getSource()==jcob)
		{
			try
			{
				
				String status=(String)jcob.getSelectedItem();
				int peer_port=0;
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
