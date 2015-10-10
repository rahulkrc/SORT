import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
class Send extends JPanel implements ActionListener
{
	
	JLabel jl=new JLabel("Dest Node");
	JLabel jl1=new JLabel("Message");
	JComboBox jcb=new JComboBox();
	JTextArea jta=new JTextArea();
	JButton jb=new JButton("Send");
	JButton jb1=new JButton("Clear");
	
	Send(String U_Name,String[] neigh_nodes)
	{
		super();
		this.setLayout(null);
		this.setBackground(new Color(0,200,254));
		
		for(int i=0;i<neigh_nodes.length;i++)
		{
			if(!neigh_nodes[i].equals(U_Name))
			jcb.addItem(neigh_nodes[i]);	
		}
		
		this.add(jl);
		this.add(jl1);
		this.add(jcb);
		this.add(jta);
		this.add(jb);
		this.add(jb1);
		
		jl.setBounds(50,25,150,35);
		jl1.setBounds(50,75,150,35);
		jcb.setBounds(175,25,150,35);
		jta.setBounds(175,75,200,200);
		jb.setBounds(50,300,100,35);
		jb1.setBounds(200,300,100,35);
		
		this.setSize(400,400);
		
		jb.addActionListener(this);
		jb1.addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent ae)
	{
		if(ae.getSource()==jb)
		{
			String dest=(String)jcb.getSelectedItem();
			String msg=jta.getText().trim();
			if(!msg.equals(""))
			{
				new ProcessData(dest,msg);	
			}
			else
			{
				JOptionPane.showMessageDialog(null,"Eneter Message");
			}
			
		}	
		else
		{
			jta.setText("");
		}
	}
}