	import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.net.*;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
class CreateNode extends JFrame implements ActionListener
{
	JLabel jl,jl1,jl2;
	JTextField jtf,jtf1;
	JButton jb,jb1,jb2;
	
	static Connection con=null;
	static Node n=null;
	int port=5000;
	
	CreateNode()
	{
		super("CREATE-NODE");
		this.setLayout(null);	
		
		jl=new JLabel("Node Info");
		jl1=new JLabel("Node Name");
		jl2=new JLabel("Password");
		
		jtf=new JTextField();
		jtf1=new JTextField();
		
		jb=new JButton("Save");
		jb1=new JButton("Clear");
		jb2=new JButton("Store Data");
		
		
		this.add(jl);
		this.add(jl1);
		this.add(jl2);
		this.add(jtf);
		this.add(jtf1);
		this.add(jb);
		this.add(jb1);
		this.add(jb2);
		
		jl.setBounds(200,10,150,35);
		jl1.setBounds(100,50,150,35);
		jl2.setBounds(100,100,150,35);
		jtf.setBounds(185,50,150,35);
		jtf1.setBounds(185,100,150,35);
		jb.setBounds(75,155,125,35);
		jb1.setBounds(225,155,125,35);
		jb2.setBounds(150,210,125,35);
		
		this.setSize(500,300);
		this.setVisible(true);
		
		jb.addActionListener(this);
		jb1.addActionListener(this);
		jb2.addActionListener(this);
		
		n = new Node();
	}
	
	public void actionPerformed(ActionEvent a)
	{
		Object o=a.getSource();
		if(o==jb)
		{
			String name=jtf.getText().trim();
			String pwd=jtf1.getText().trim();
			
			boolean flg=false;
			if(name.equals("")|pwd.equals(""))
			{
				JOptionPane.showMessageDialog(this,"Some Fileds Missing!!!!");	
			}
			else
			{
				try
				{	
					flg = n.checkNode(name);
					if(flg)
					{
						InetAddress inet=InetAddress.getLocalHost();
						String ip=inet.getHostAddress();
						String[] info = new String[3];
						info[0]=pwd;
						info[1]=ip;
						info[2]=Integer.toString(port++);
						n.insertNode(name,info);
						JOptionPane.showMessageDialog(null,"User Registered Successfully!!!!");	
					}
					else
					{
						JOptionPane.showMessageDialog(null,"User Already Exists!!!!");
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				
			}
		}
		else if(o==jb2)
		{
			try
			{
				FileOutputStream fileOut = new FileOutputStream("node.ser");
	         	ObjectOutputStream out = new ObjectOutputStream(fileOut);
	         	System.out.println("AL "+n.reg_node);
	         	System.out.println("HM "+n.node_info);
	         	out.writeObject(n);
	         	out.close();
	         	fileOut.close();
	         	JOptionPane.showMessageDialog(null,"Successfully Stored Data to node.ser !!!!");
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
		}
		else if(o==jb1)
		{
			jtf.setText("");
			jtf1.setText("");
		}
		
	}
	
	public static void main(String args[])
	{
		new CreateNode();
	}
}