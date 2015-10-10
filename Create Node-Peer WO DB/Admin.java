import java.util.*;
import java.io.*;
import java.net.*;

class Admin extends Thread
{
	public static Node n = null;
	public static Peer p = null;
	public static ArrayList log_user = new ArrayList();
	Admin()
	{
		super();
		try
		{
			 FileInputStream fileIn = new FileInputStream("node.ser");
	         ObjectInputStream in = new ObjectInputStream(fileIn);
	         n = (Node) in.readObject();
	         in.close();
	         fileIn.close();
	         
	         FileInputStream fileIn1 = new FileInputStream("peer.ser");
	         ObjectInputStream in1 = new ObjectInputStream(fileIn1);
	         p = (Peer) in1.readObject();
	         in1.close();
	         fileIn1.close();
	    	this.start();     
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	public void run()
	{
		try
		{
			ServerSocket ss = new ServerSocket(5555);
			while(true)
			{
				Socket s = ss.accept();
				new HandleRequest(s);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	public static void main(String args[])
	{
		new Admin();
	}
}

class HandleRequest extends Thread
{
	Socket s;
	HandleRequest(Socket soc)
	{
		super();
		s = soc;
		start();
	}
	
	public void run()
	{
		try
		{
			ObjectOutputStream oos=new ObjectOutputStream(s.getOutputStream());
			ObjectInputStream ois=new ObjectInputStream(s.getInputStream());
			String req = (String)ois.readObject();
			System.out.println("REQ "+ req);
			if(req.equals("USER_LOGIN"))
			{
				String name = (String)ois.readObject();
				String pwd = (String)ois.readObject();
				String ip = (String)ois.readObject();
				
				ArrayList reg_nd = Admin.n.reg_node;
				ArrayList log_nd = Admin.log_user;
				Map nd_inf =  Admin.n.node_info;
				
				if(!log_nd.contains(name))
				{
					if(reg_nd.contains(name))
					{
						String[] info = (String[])nd_inf.get(name);
						if(info[0].equals(pwd))
						{
							String[] neigh_nd = new String[reg_nd.size()];
							for(int i=0;i<reg_nd.size();i++)
							{
								neigh_nd[i] = (String)reg_nd.get(i);	
							}
							info[1]=ip;
							oos.writeObject(true);
							oos.writeObject(neigh_nd);
							oos.writeObject(info[2]);
							oos.writeObject(Admin.p.peer_info.get("1"));
							
							Admin.n.node_info.put(name,info);
							Admin.log_user.add(name);
						}
						else
						{
							oos.writeObject(false);
							oos.writeObject("Check Your User Name and Password !!!");
						}
					}
					else
					{
						oos.writeObject(false);
						oos.writeObject("User Dosen't Exists!!!");
					}
				}
				else 
				{
					oos.writeObject(false);
					oos.writeObject("User Already Logged In!!!");
				}
				
			}
			else if(req.equals("PEER_GROUP_1_LOGIN"))
			{
				String ip = (String)ois.readObject();
				
				Map reg_pr = Admin.p.peer_info;
				ArrayList log_nd = Admin.log_user;
				
				if(!log_nd.contains("PEER_GROUP_1"))
				{
					if(reg_pr.containsKey("1"))
					{
						ArrayList al = (ArrayList)reg_pr.get("1");
						ArrayList adj = (ArrayList)reg_pr.get("2");
						
						oos.writeObject(true);
						oos.writeObject(al);
						oos.writeObject(adj);
						
						log_nd.add("PEER_GROUP_1");
						ArrayList nu_al = new ArrayList();
						nu_al.add(0,ip);
						nu_al.add(1,al.get(1));
						Admin.p.peer_info.put("1",nu_al);
						System.out.println("AL "+nu_al);
						
					}
					else
					{
						oos.writeObject(false);
						oos.writeObject("User Dosen't Exists!!!");
					}
				}
				else 
				{
					oos.writeObject(false);
					oos.writeObject("User Already Logged In!!!");
				}
				
			} 
			else if(req.equals("PEER_GROUP_2_LOGIN"))
			{
				String ip = (String)ois.readObject();
				
				Map reg_pr = Admin.p.peer_info;
				ArrayList log_nd = Admin.log_user;
				
				if(!log_nd.contains("PEER_GROUP_2"))
				{
					if(reg_pr.containsKey("2"))
					{
						ArrayList al = (ArrayList)reg_pr.get("2");
						ArrayList adj = (ArrayList)reg_pr.get("3");
						
						oos.writeObject(true);
						oos.writeObject(al);
						oos.writeObject(adj);
						
						log_nd.add("PEER_GROUP_2");
						ArrayList nu_al = new ArrayList();
						nu_al.add(0,ip);
						nu_al.add(1,al.get(1));
						Admin.p.peer_info.put("2",nu_al);
					}
					else
					{
						oos.writeObject(false);
						oos.writeObject("User Dosen't Exists!!!");
					}
				}
				else 
				{
					oos.writeObject(false);
					oos.writeObject("User Already Logged In!!!");
				}
				
			} 
			else if(req.equals("PEER_GROUP_3_LOGIN"))
			{
				String ip = (String)ois.readObject();
				
				Map reg_pr = Admin.p.peer_info;
				ArrayList log_nd = Admin.log_user;
				
				if(!log_nd.contains("PEER_GROUP_3"))
				{
					if(reg_pr.containsKey("3"))
					{
						ArrayList al = (ArrayList)reg_pr.get("3");
												
						oos.writeObject(true);
						oos.writeObject(al);
											
						log_nd.add("PEER_GROUP_3");
						ArrayList nu_al = new ArrayList();
						nu_al.add(0,ip);
						nu_al.add(1,al.get(1));
						Admin.p.peer_info.put("3",nu_al);
					}
					else
					{
						oos.writeObject(false);
						oos.writeObject("User Dosen't Exists!!!");
					}
				}
				else 
				{
					oos.writeObject(false);
					oos.writeObject("User Already Logged In!!!");
				}
				
			}
			else if(req.equals("NEXT_NODE_INFO"))
			{
				String peer = (String)ois.readObject();
				if(peer.equals("PEER_GROUP_3"))
				{
					String usr = (String)ois.readObject();
					String[] info = (String[])Admin.n.node_info.get(usr);	
					oos.writeObject(true);
					oos.writeObject(info[1]);
					oos.writeObject(Integer.parseInt(info[2]));
				}
				
			}
			else if(req.equals("PEER_GROUP_STATUS"))
			{
				String peer_id = (String)ois.readObject();
				int peer_no = (Integer)ois.readObject();
				String status = (String)ois.readObject();
				
				if(peer_id.equals("1"))
				{
					for(int i=0;i<Admin.log_user.size();i++)
					{
						String user = (String)Admin.log_user.get(i);
						if(Admin.n.node_info.containsKey(user))
						{
							String[] node_inf = (String[])Admin.n.node_info.get(user);
							Socket s = new Socket(node_inf[1],Integer.parseInt(node_inf[2]));
							ObjectOutputStream oos1=new ObjectOutputStream(s.getOutputStream());
							ObjectInputStream ois1=new ObjectInputStream(s.getInputStream());
							
							oos1.writeObject("STATUS");
							oos1.writeObject(peer_no);
							oos1.writeObject(status);
							s.close();
						}
						
				
					}
				}
				else if(peer_id.equals("2"))
				{
					int port=2000;
					String ip = "";
					for(int i=0;i<Admin.log_user.size();i++)
					{
						if(Admin.p.peer_info.containsKey("1"))
						{
							ArrayList peer_inf = (ArrayList)Admin.p.peer_info.get("1");
							ip = (String)peer_inf.get(0);
							
							for(int ii=1;ii<=peer_inf.size();ii++)
							{
								System.out.println("I "+ii+" P "+port);
								port = port+ii;
								Socket s = new Socket(ip,port);
								ObjectOutputStream oos1=new ObjectOutputStream(s.getOutputStream());
								ObjectInputStream ois1=new ObjectInputStream(s.getInputStream());
								
								oos1.writeObject("STATUS");
								oos1.writeObject(peer_no);
								oos1.writeObject(status);
								s.close();
							}
							
						}
						
				
					}
				} 
				else if(peer_id.equals("3"))
				{
					int port=3000;
					String ip = "";
					for(int i=0;i<Admin.log_user.size();i++)
					{
						if(Admin.p.peer_info.containsKey("2"))
						{
							ArrayList peer_inf = (ArrayList)Admin.p.peer_info.get("2");
							ip = (String)peer_inf.get(0);
							
							for(int ii=0;ii<peer_inf.size();ii++)
							{
								port = port+1;
								Socket s = new Socket(ip,port);
								ObjectOutputStream oos1=new ObjectOutputStream(s.getOutputStream());
								ObjectInputStream ois1=new ObjectInputStream(s.getInputStream());
								
								oos1.writeObject("STATUS");
								oos1.writeObject(peer_no);
								oos1.writeObject(status);
								s.close();
							}
							
						}
						
				
					}
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}