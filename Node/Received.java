import javax.swing.*;
import java.awt.*;
class Received extends JPanel
{
	static JTextArea jta=new JTextArea();
	JLabel jl=new JLabel("Received");
	
	Font f=new Font("Dialog",Font.PLAIN,25);
	
	Received()
	{
		super();
		this.setLayout(null);
		
		this.add(jl);
		this.add(jta);
		
		jl.setFont(f);
		
		jl.setBounds(125,5,150,35);
		jta.setBounds(50,50,282,250);
	}
}