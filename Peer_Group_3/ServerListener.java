import javax.swing.*;
import java.net.*;
import java.io.*;
import java.sql.*;
class ServerListener extends Thread
{
	int port=4000;
	int port_id=0;
	int ii=0;
	ServerSocket ss;
	JRadioButton jr1,jr2,jr3;
	JCheckBox jcbox;
	
	int admin_port = 5555, ch=0;
	String admin_ip = "";
	
	ServerListener(int i,JFrame j,JRadioButton r1,JRadioButton r2,JRadioButton r3,JCheckBox jcb)
	{
		super();
		ii=i;
		jr1=r1;
		jr2=r2;
		jr3=r3;
		port_id=port+i;
		jcbox=jcb;
		start();
	}
	
	public void run()
	{
		try
		{
			ss=new ServerSocket(port_id);
			while(true)
			{
				Socket s=ss.accept();
				ObjectOutputStream oos=new ObjectOutputStream(s.getOutputStream());
				ObjectInputStream ois=new ObjectInputStream(s.getInputStream());
				String req=(String)ois.readObject();
				System.out.println("REQ "+req+" RECEIVED IN PEER_3 "+ii);
				admin_ip = "";
				if(req.equals("DATA"))
				{
					String sender=(String)ois.readObject();
					String dest=(String)ois.readObject();
					String msg=(String)ois.readObject();
					
					String node_IP="";
					int node_PORT=0;
					ch=0;
					
					try
					{
						FileInputStream f = new FileInputStream("Admin.txt");
						while((ch=f.read())!=-1)
						admin_ip+=(char)ch;	
								
						Socket s1 = new Socket(admin_ip,admin_port);
						ObjectOutputStream oos2=new ObjectOutputStream(s1.getOutputStream());
						ObjectInputStream ois2=new ObjectInputStream(s1.getInputStream());
						
						oos2.writeObject("NEXT_NODE_INFO");
						oos2.writeObject("PEER_GROUP_3");
						oos2.writeObject(dest);
						
						boolean flg = (Boolean)ois2.readObject();
						if(flg)
						{
							node_IP=(String)ois2.readObject();
							node_PORT=(Integer)ois2.readObject();
						}
						s1.close();
					
						Socket soc=new Socket(node_IP,node_PORT);
						ObjectOutputStream oos1=new ObjectOutputStream(soc.getOutputStream());
						ObjectInputStream ois1=new ObjectInputStream(soc.getInputStream());
						
					//	long start=System.currentTimeMillis();
						
						if(jr1.isSelected())
						{
							Thread.sleep(2000);
						}
						else if(jr2.isSelected())
						{
							Thread.sleep(1000);
						}
						else if(jr3.isSelected())
						{
							Thread.sleep(100);
						}
						
						oos1.writeObject("DATA");
						oos1.writeObject(sender);
						oos1.writeObject(dest);
						if(jcbox.isSelected())
						{
							msg+="NEW DATA ADDED";
						}
						oos1.writeObject(msg);
						
						oos.writeObject(msg);
						
						//String reply=(String)ois1.readObject();
						//long end=System.currentTimeMillis();
						oos.close();
						ois.close();
						soc.close();
						
					//	long tt=end-start;
						
					//	System.out.println("TT "+tt);
						
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
					
				//	oos.writeObject("aaaa");
					
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
