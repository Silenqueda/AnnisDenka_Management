package View;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import Handler.DataProvider;

public class QueryViewer extends JFrame{

	private static final long serialVersionUID = -74268140966252577L;
	private final String frameTitle = "query viewer";
	
	private int button_width = 10;
	private int button_height = 10;
	private int butto_height_offset = 60;
	
	private boolean visible = true;

	private DataProvider datatProvider;
	
	
	public QueryViewer(DataProvider dataProvider) {
		this.datatProvider = dataProvider;
		this.createMainFrame();
	}
	
	private void createMainFrame() {
		this.setTitle(frameTitle);
		this.setSize(600, 400);
		this.setResizable(false);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setLocationRelativeTo(null);
		
		JButton b1 = new JButton("test query");
		JButton b2 = new JButton("test query");
		b1.setBounds(button_width, button_height, 100, 20);
		b2.setBounds(button_width, button_height + butto_height_offset, 100, 20);
		
		JLabel b1_output = new JLabel("");
		b1_output.setBounds(110, 10, 400, 300);
		this.add(b1_output);
		
		b1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				b1_output.setText(datatProvider.read_sumOfExpansesOrderedByPayDate());
				
			}
		});
		
		b2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		
		
		this.add(b1);
		this.add(b2);
		
		this.setLayout(null);
		this.setVisible(visible);
	}
	
	
	
	
	
}
