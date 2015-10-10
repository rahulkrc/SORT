import java.sql.*;
import java.net.*;
import java.io.*;
import java.util.*;
class ProcessData
{
	static float peer_TT[];
	static float peer_SER[];
	static float peer_TW[];
	static float peer_TR[];
	int peer_count=0;
	ProcessData(String dest,String msg)
	{
		String peer_IP="";
		int peer_PORT=2000;
		
		System.out.println("Selected Destination "+dest);
		
		int nxt_peer=checkNxtPeer();
		
		peer_PORT+=nxt_peer+1;
		
		
		
		try
		{
			peer_IP=ClientForm.peer_ip;
			
			System.out.println("PORT "+peer_PORT);
			
			Socket s=new Socket(peer_IP,peer_PORT);
			ObjectOutputStream oos=new ObjectOutputStream(s.getOutputStream());
			ObjectInputStream ois=new ObjectInputStream(s.getInputStream());
			long start=System.currentTimeMillis();
			oos.writeObject("DATA");
			oos.writeObject(ClientForm.usr_nme);
			oos.writeObject(dest);
			oos.writeObject(msg);
			
			
			
			String reply=(String)ois.readObject();
			long end=System.currentTimeMillis();
			oos.close();
			ois.close();
			s.close();
			
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
			System.out.println("SENDER KEY "+msg.hashCode());
			System.out.println("RECEIVER KEY "+reply.hashCode());
			
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
			System.out.println("Selected PEER "+"P_G_1_"+Integer.toString(nxt_peer+1));
			System.out.println("");
			
			int ii=nxt_peer+1;
			
			Map peer_info = ServerListener.adj_peer_inf;
			int TOT_TRAN = ServerListener.TOT_TRANS;
			
			ArrayList al = (ArrayList)peer_info.get("P_G_1_"+Integer.toString(ii));
			
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
			String peer="P_G_1_"+Integer.toString(ii);
			
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
							
				peer_TT=new float[ClientForm.peer_cnt];
				peer_SER=new float[ClientForm.peer_cnt];
				peer_TW=new float[ClientForm.peer_cnt];
				peer_TR=new float[ClientForm.peer_cnt];
				max_peer_value=0;
				max_peer_index=-1;
				
				Map nu_peer_info = ServerListener.adj_peer_inf;
				int NU_TOT_TRAN = ServerListener.TOT_TRANS;
			
			
				for(int i=0;i<nu_peer_info.size();i++)
				{
					int ij=i+1;
					ArrayList nu_al = (ArrayList)nu_peer_info.get("P_G_1_"+Integer.toString(ij));
				
					tt=NU_TOT_TRAN;
					ser=(Float)nu_al.get(0);
					tw=(Float)nu_al.get(1);
				
					peer_TT[indx]=tt;
					peer_SER[indx]=ser;
					peer_TW[indx]=tw;
					indx++;
				}
							
				for(int ij=0;ij<ClientForm.peer_cnt;ij++)
				{
					if(peer_TT[ij]==0.0)
					{
							peer_TR[ij]=10;	
					}
					else
					{
						float service_ratio=peer_SER[ij];
						float trust_ratio=peer_TW[ij];
						System.out.println("SR "+service_ratio+" TR "+trust_ratio);
						service_ratio=service_ratio/5f;
						trust_ratio=trust_ratio/10f;
								
						peer_TR[ij]=(service_ratio+trust_ratio)/ peer_TT[ij];
						System.out.println("TTWR "+peer_TR[ij]);
					}
				
					if(max_peer_value<peer_TR[ij])
					{
						max_peer_value=peer_TR[ij];
						max_peer_index=ij;
					}
				}
				float[] f=new float[ClientForm.peer_cnt];
				for(int jj=0;jj<ClientForm.peer_cnt;jj++)
				{
					f[jj]=(peer_TR[jj]/max_peer_value)*100f;
					
				}
				Peer.setLbl(f);
				System.out.println("============================================");
			}
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
			peer_TT=new float[ClientForm.peer_cnt];
			peer_SER=new float[ClientForm.peer_cnt];
			peer_TW=new float[ClientForm.peer_cnt];
			peer_TR=new float[ClientForm.peer_cnt];
			max_peer_value=0;
			max_peer_index=-1;
			
			Map peer_info = ServerListener.adj_peer_inf;
			int TOT_TRAN = ServerListener.TOT_TRANS;
			
			for(int i=0;i<peer_info.size();i++)
			{
				int ii=i+1;
				
				ArrayList al = (ArrayList)peer_info.get("P_G_1_"+Integer.toString(ii));
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
			
			for(int i=0;i<ClientForm.peer_cnt;i++)
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