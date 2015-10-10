import javax.swing.*;
import java.awt.*;
class Peer extends JPanel
{
	JLabel[] jl;
	static JLabel[] jlper;
	
	JLabel jl0=new JLabel("Peer Info");
	
	Font f=new Font("Dialog",Font.PLAIN,20);
	Font f1=new Font("Dialog",Font.BOLD,20);
	
	Peer(int peer_count)
	{
		super();
		this.setLayout(null);
		this.setBackground(Color.white);
		jl=new JLabel[peer_count+1];
		jlper=new JLabel[peer_count+1];
		
		this.add(jl0);
		jl0.setBounds(150,5,150,35);
		jl0.setFont(f);
		jl0.setForeground(Color.red);
		
		for(int i=1;i<=peer_count;i++)
		{
			jl[i]=new JLabel("P_G_1_"+i);
			this.add(jl[i]);
			jl[i].setBounds(100,50*i,100,30);
			jl[i].setFont(f);
			
			jlper[i]=new JLabel("100%");
			this.add(jlper[i]);
			jlper[i].setBounds(210,50*i,100,30);
			jlper[i].setFont(f1);
			jlper[i].setForeground(new Color(0,100,254));
		}
		
		
		this.setSize(400,600);
	}
	
	public static void setLbl(float[] f)
	{
		for(int j=0;j<f.length;j++)
		{
			jlper[j+1].setText(f[j]+"%");
		}
	}
}