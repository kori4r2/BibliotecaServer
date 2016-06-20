import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class Interface extends JFrame implements ActionListener{
	private JFrame frame;
	private JButton but;
	
	private Container pane;
	private JPanel botao;
	
	private JTextField output;

	private JPanel userPanel;
	private JPanel senhaPanel;
	private JPanel userPass;
	private JPanel welcome;
	
	private JTextField usuario2;
	private JTextField usuario;
	
	private JTextField senha;
	private JTextField senha2;
	
	public Interface(){
		super("Teste");
		
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(20, 20);
		
		pane = this.getContentPane();
		pane.setLayout(new BorderLayout(30, 30));
		pane.setSize(200, 200);
		
		
		pane.add(getWelcomeLayout(), BorderLayout.NORTH);
		
		pane.add(getUserPassLayout(), BorderLayout.CENTER);
		pane.add(this.getButtonPanel(), BorderLayout.SOUTH);
		
		//JScrollPane scroll = new JScrollPane(output);
		//scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		//scroll.setPreferredSize(new Dimension(600, 400));
		
		//input = new JTextField(25);
		//input.addActionListener(this);
		//pane.add(input, BorderLayout.CENTER);
		pack();
	}
	
	private JComponent getWelcomeLayout(){
		output = new JTextField("Digite Usuario e Senha");
		output.setEditable(false);
		
		welcome = new JPanel();
		welcome.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		welcome.add(output);
		
		return welcome;
	}
	
	private JComponent getUserPassLayout(){
		userPass = new JPanel();
		userPass.setLayout(new GridLayout(1,2));
		userPass.setSize(80, 20);
		
		userPass.add(getUsuarioLayout());
		userPass.add(getSenhaLayout());
		
		return userPass;
	}
	
	private JComponent getUsuarioLayout(){
		usuario2 = new JTextField("Usuário:");
		usuario2.setEditable(false);
		usuario2.setSize(20, 10);
		
		usuario = new JTextField();
		usuario.setEditable(true);
		usuario.setSize(20, 10);
		
		userPanel = new JPanel();
		userPanel.setLayout(new GridLayout(1,2));
		
		userPanel.add(usuario2);
		userPanel.add(usuario);
		
		return userPanel;
	}
	
	private JComponent getSenhaLayout(){
		senha2 = new JTextField("Senha:");
		senha2.setEditable(false);
		senha2.setSize(20, 10);
		
		senha = new JTextField();
		senha.setEditable(true);
		senha.setSize(20,10);
		
		senhaPanel = new JPanel();
		senhaPanel.setLayout(new GridLayout(1,2));
		
		senhaPanel.add(senha2);
		senhaPanel.add(senha);
		
		return senhaPanel;
	}
	
	protected JComponent getButtonPanel(){
		botao = new JPanel();
		botao.setLayout(new GridLayout(1,1));
		but = new JButton("LOGIN");
		botao.add(but);
		but.addActionListener(this);
		return botao;
	}
	
	public static void main(String[] args) throws Exception{
		Interface oi = new Interface();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		pane.remove(welcome);
		pane.remove(userPass);
		pane.remove(botao);
		pane.setVisible(false);
		pane.add(botao);
		pane.setVisible(true);
		
	}

}
