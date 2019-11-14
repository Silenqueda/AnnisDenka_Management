package GUI;

import java.awt.Window;
import java.util.Arrays;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class GUI_popup {

	private String userName;
	private String password;

	//Constructor
	public GUI_popup() {
		this.popUpUserInput();
	}

	private void popUpUserInput() {
		JTextField userName = new JTextField();
		JPasswordField password = new JPasswordField();

		Object[] message = { "username/mail address", userName, "password", password };

		JOptionPane pane = new JOptionPane(message, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
		
		pane.createDialog(null, "Login to mail account").setVisible(true);
		
		if(!userName.getText().isEmpty() && !Arrays.toString(password.getPassword()).isEmpty()) {
			this.userName = userName.getText();
			this.password = Arrays.toString(password.getPassword()).replace(",", "").replace(" ", "").replace("[", "").replace("]", ""); //### verbesserungswürdig...^^
		} else {
			this.dialogFailedLoginAttemp();
		}
		
	}
	
	//Informs user if login attempt has failed
	public void dialogFailedLoginAttemp() {
		JDialog dialog_abort = new JDialog();
		Object[] dialog_abort_message = {"Username and/or password wrong or empty"};
		JOptionPane pane_dialog_abort = new JOptionPane(dialog_abort_message, JOptionPane.OK_OPTION);
		pane_dialog_abort.createDialog(null, "Failed to Login").setVisible(true);
		System.exit(0);
	}
	
	
	//Getter and Setter
	public String getUserName() {
		return userName;
	}

	public String getPassword() {
		return password;
	}

}
