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
	
	int port=2000;
	int port_id=0;
	int jj=0;
	int apc=0;
	ServerSocket ss;
	float peer_TT[];
	float peer_SER[];
	float peer_TW[];
	float peer_TR[];
	JLabel[] jlper;
	int peer_count=0;
	JRadioButton jr1,jr2,jr3;
	JCheckBox jcbox;
	ServerListener(int i,int adj_peer_count,JRadioButton r1,JRadioButton r2,JRadioButton r3,JLabel[] jlp,JCheckBox jcb)
	{
		super();
		jj=i;
		jr1=r1;
		jr2=r2;
		jr3=r3;
		apc=adj_peer_count;
		port_id=port+i;
		jlper=jlp;
		jcbox=jcb;
		start();
	}
	
	public void run()
	{
		
		
		try
		{
			ss=new ServerSocket(port_id);
			System.out.println("PORT ID "+port_id);
			
			for(int ii=1;ii<=apc;ii++)
			{
				ArrayList al = new ArrayList();
				al.add(0,5.0f);
				al.add(1,10.0f); 
				al.add(2,"ON");
				adj_peer_inf.put("P_G_2_"+Integer.toString(ii),al);
			}
		
			while(true)
			{
				Socket s=ss.accept();
				System.out.println("IN "+port_id);
				ObjectOutputStream oos=new ObjectOutputStream(s.getOutputStream());
				ObjectInputStream ois=new ObjectInputStream(s.getInputStream());
				String req=(String)ois.readObject();
				System.out.println("REQ "+req+" RECEIVED IN PEER_1 "+jj);
				if(req.equals("DATA"))
				{
					String sender=(String)ois.readObject();
					String dest=(String)ois.readObject();
					String msg=(String)ois.readObject();
					
					String peer_IP="";
					int peer_PORT=3000;
		
					int nxt_peer=checkNxtPeer();
					peer_PORT+=nxt_peer+1;
					
					try
					{
						
						peer_IP=Index.adj_peer_ip;
												
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
						
						
						Socket soc=new Socket(peer_IP,peer_PORT);
						ObjectOutputStream oos1=new ObjectOutputStream(soc.getOutputStream());
						ObjectInputStream ois1=new ObjectInputStream(soc.getInputStream());
						
						long start=System.currentTimeMillis();
						
						oos1.writeObject("DATA");
						oos1.writeObject(sender);
						oos1.writeObject(dest);
						if(jcbox.isSelected())
						{
							msg+="NEW DATA ADDED";
						}
						oos1.writeObject(msg);
						
						oos.writeObject(msg);
						
						String reply=(String)ois1.readObject();
						long end=System.currentTimeMillis();
						
						oos.close();
						ois.close();
						soc.close();
						
						long tt=end-start;
						
						float service=0;
						float trust=0;
						
						if(msg.hashCode()==reply.hashCode())
						{
							trust=1/2f;
						}
						else
						{
							trust=-1/2f;
						}
						
						
						if(tt>=2000)
						{
							service=-1/2f;
						}
						if(tt>=1000&tt<2000)
						{
							service=0f;
						}
						else if(tt<1000)
						{
							service=1/2f;
						}
						
						System.out.println("");
						System.out.println("Selected PEER "+"P_G_2_"+Integer.toString(nxt_peer+1));
						System.out.println("");
						
						int ii=nxt_peer+1;
			
						Map peer_info = ServerListener.adj_peer_inf;
						int TOT_TRAN = ServerListener.TOT_TRANS;
						
						ArrayList al = (ArrayList)peer_info.get("P_G_2_"+Integer.toString(ii));
						
						int DB_TT=TOT_TRAN;
						float DB_SER=(Float)al.get(0);
						float DB_TWT=(Float)al.get(1);
								
						DB_TT=DB_TT+1;
						DB_SER=DB_SER+service;
						DB_TWT=DB_TWT+trust;
								
						String Nu_STATUS="ON";
						int Nu_TT=DB_TT;
						float Nu_SER=DB_SER;
						float Nu_TWT=DB_TWT;
						if(DB_SER<=1)
						{
							Nu_SER=0;
							Nu_STATUS="OFF";
						}
						if(DB_SER>=10)
						{
							Nu_SER=10;
						}
								
						if(DB_TWT<=1)
						{
							Nu_TWT=0;
							Nu_STATUS="OFF";
						}
						if(DB_TWT>=10)
						{
							Nu_TWT=10;
						}
						String peer="P_G_2_"+Integer.toString(ii);
						
						boolean flg =false;
						if(true)
						{
							ServerListener.TOT_TRANS = Nu_TT;
							ArrayList nu_al = new ArrayList();
							nu_al.add(0,Nu_SER);
							nu_al.add(1,Nu_TWT);
							nu_al.add(2,Nu_STATUS);
							ServerListener.adj_peer_inf.put(peer,nu_al);
							flg = true;
						}
				
						
						if(flg)
						{
							int max_peer_index=-1;
							float max_peer_value=0;
							int indx=0;
							float ser=0,tw=0;
										
							peer_TT=new float[Index.adj_peer_count];
							peer_SER=new float[Index.adj_peer_count];
							peer_TW=new float[Index.adj_peer_count];
							peer_TR=new float[Index.adj_peer_count];
							max_peer_value=0;
							max_peer_index=-1;
							
							Map nu_peer_info = ServerListener.adj_peer_inf;
							int NU_TOT_TRAN = ServerListener.TOT_TRANS;
						
						
							for(int i=0;i<nu_peer_info.size();i++)
							{
								int ij=i+1;
								ArrayList nu_al = (ArrayList)nu_peer_info.get("P_G_2_"+Integer.toString(ij));
							
								tt=NU_TOT_TRAN;
								ser=(Float)nu_al.get(0);
								tw=(Float)nu_al.get(1);
							
								peer_TT[indx]=tt;
								peer_SER[indx]=ser;
								peer_TW[indx]=tw;
								indx++;
							}
										
							for(int ij=0;ij<Index.adj_peer_count;ij++)
							{
								if(peer_TT[ij]==0.0)
								{
										peer_TR[ij]=10;	
								}
								else
								{
									float service_ratio=peer_SER[ij];
									float trust_ratio=peer_TW[ij];
												
									service_ratio=service_ratio/5f;
									trust_ratio=trust_ratio/10f;
											
									peer_TR[ij]=(service_ratio+trust_ratio)/ peer_TT[ij];
								}
							
								if(max_peer_value<peer_TR[ij])
								{
									max_peer_value=peer_TR[ij];
									max_peer_index=ij;
								}
							}
							float[] f=new float[Index.adj_peer_count];
							for(int jj=0;jj<Index.adj_peer_count;jj++)
							{
								f[jj]=(peer_TR[jj]/max_peer_value)*100f;
								jlper[jj].setText(f[jj]+"%");
							}
							
							System.out.println("============================================");
						}
						
						
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
					
					
				}
				else if(req.equals("STATUS"))
				{
					int peer=(Integer)ois.readObject();
					String status = (String)ois.readObject();
					System.out.println("STATUS UPDATE FROM "+peer+" STATUS "+status);
					
					int ij=peer-1;
					if(status.equals("ON"))
					{
						jlper[ij].setForeground(new Color(0,100,254));
					}
					else
					{
						jlper[ij].setForeground(Color.red);
					}
					
					ArrayList al = adj_peer_inf.get("P_G_2_"+peer);
					ArrayList nu_al = new ArrayList();
					nu_al.add(0,(Float)al.get(0));
					nu_al.add(1,(Float)al.get(1));
					nu_al.add(2,status);
					adj_peer_inf.put("P_G_2_"+peer,nu_al);
					
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
	
	public int checkNxtPeer()
	{
		int max_peer_index=-1;
		float max_peer_value=0;
		int indx=0;
		float tt=0,ser=0,tw=0;
		String status = "ON";
		
		try
		{
			peer_TT=new float[Index.adj_peer_count];
			peer_SER=new float[Index.adj_peer_count];
			peer_TW=new float[Index.adj_peer_count];
			peer_TR=new float[Index.adj_peer_count];
			max_peer_value=0;
			max_peer_index=-1;
			
			Map peer_info = ServerListener.adj_peer_inf;
			int TOT_TRAN = ServerListener.TOT_TRANS;
			
			for(int i=0;i<peer_info.size();i++)
			{
				int ii=i+1;
				
				ArrayList al = (ArrayList)peer_info.get("P_G_2_"+Integer.toString(ii));
				tt=TOT_TRAN;
				ser=(Float)al.get(0);
				tw=(Float)al.get(1);
				status=(String)al.get(2);
				if(status.equals("ON"))
				{
					peer_TT[indx]=tt;
					peer_SER[indx]=ser;
					peer_TW[indx]=tw;
					indx++;
				}
				else
				{
					peer_TT[indx]=-1;
					indx++;
				}	
			}
			
			for(int i=0;i<Index.adj_peer_count;i++)
			{
				if(peer_TT[i]==0.0)
				{
					peer_TR[i]=10;	
				}
				else if(peer_TT[i]==-1)
				{
					peer_TR[i]=-1;	
				}
				else
				{
					float service_ratio=peer_SER[i];
					float trust_ratio=peer_TW[i];
					
					service_ratio=service_ratio/5f;
					trust_ratio=trust_ratio/10f;
					
					peer_TR[i]=(service_ratio+trust_ratio)/peer_TT[i];
				}
				int ii=i+1;
				System.out.println("Trust Ratio "+ii+" "+peer_TR[i]);
				if(max_peer_value<peer_TR[i])
				{
					max_peer_value=peer_TR[i];
					max_peer_index=i;
				}
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	return max_peer_index;
	}
}
