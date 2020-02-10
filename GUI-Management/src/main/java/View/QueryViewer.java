package View;

import javax.swing.JFrame;

public class QueryViewer extends JFrame{

	private static final long serialVersionUID = -74268140966252577L;
	private final String frameTitle = "query viewer";
	
	private boolean visible = true;

	public QueryViewer() {
		this.createMainFrame();
	}
	
	private void createMainFrame() {
		this.setTitle(frameTitle);
		this.setSize(600, 400);
		this.setResizable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		
		this.setVisible(visible);
	}
	
	
	
}
