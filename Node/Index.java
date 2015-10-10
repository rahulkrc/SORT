import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.io.*;
import java.net.*;
import java.util.*;

class Index extends JFrame implements ActionListener
{
	JTextField jtf;
	JPasswordField jpf;
	JButton jb,jb1;
	Index()
	{
		super("LOGIN");
		this.setLayout(null);
		
		JLabel jl=new JLabel("LOGIN");
		JLabel jl0=new JLabel("User Name");
		JLabel jl1=new JLabel("Password");
		jtf=new JTextField();
		jpf=new JPasswordField();
		jb=new JButton("Login");
		jb1=new JButton("Clear");
		
		this.add(jl);
		this.add(jl0);
		this.add(jl1);
		this.add(jtf);
		this.add(jpf);
		this.add(jb);
		this.add(jb1);
		
		jl.setBounds(150,0,150,35);
		jl0.setBounds(85,35,100,35);
		jl1.setBounds(85,65,100,35);
		jtf.setBounds(175,38,100,25);
		jpf.setBounds(175,68,100,25);
		jb.setBounds(65,115,100,35);
		jb1.setBounds(185,115,100,35);
		
		jb.addActionListener(this);
		jb1.addActionListener(this);
		
		this.setSize(350,200);
		this.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent ae)
	{
		if(ae.getSource()==jb)
		{
			String name=jtf.getText().trim();
			String pwd=jpf.getText().trim();
			int port = 0,admin_port = 5555, ch=0;
			String admin_ip = "";
			if((!name.equals(""))&(!pwd.equals("")))
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
					oos.writeObject("USER_LOGIN");
					oos.writeObject(name);
					oos.writeObject(pwd);
					oos.writeObject(ip);
					
					boolean flg = (Boolean)ois.readObject();
					if(flg)
					{
						String[] neigh_node = (String[])ois.readObject();
						port = Integer.parseInt((String)ois.readObject());
						ArrayList adj_peer_info = (ArrayList)ois.readObject();
						String adj_peer_ip = (String)adj_peer_info.get(0);
						System.out.println("port "+ adj_peer_info);
						int adj_peer_count=(Integer)adj_peer_info.get(1);
						new ClientForm(name,neigh_node,adj_peer_count,adj_peer_ip,port);
						this.setVisible(false);
					}
					else
					{
						JOptionPane.showMessageDialog(null,(String)ois.readObject());
					}
					s.close();
				}				
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
			else
			{
				JOptionPane.showMessageDialog(null,"Some Field Missing!!!!");
			}
			
		}
		else
		{
			jtf.setText("");
			jpf.setText("");
		}
	}
	
	public static void main(String args[])
	{
		new Index();
	}
}