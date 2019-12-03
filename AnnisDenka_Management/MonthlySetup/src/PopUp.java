import java.util.Arrays;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class PopUp {
	
	private String grundkosten;
	private String kwh;
	
	//Constructor
		public PopUp() {
			this.popUpUserInput();
		}

		private void popUpUserInput() {
			JTextField grundkosten = new JTextField();
			JTextField kwh = new JTextField();

			Object[] message = { "Grundkosten in €", grundkosten, "Preis / kwh", kwh };

			JOptionPane pane = new JOptionPane(message, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
			
			pane.createDialog(null, "Enter electricity expanses").setVisible(true);
			
			if(grundkosten.getText().isEmpty()) {
				this.grundkosten = "0";
			} else if(kwh.getText().isEmpty()) {
				this.kwh = "0";
			} else {
				this.grundkosten = grundkosten.getText();
				this.kwh = kwh.getText();
			}
			
			
			
			
		}
		
		//Informs user if login attempt has failed
		public void dialogFailedLoginAttemp() {
			Object[] dialog_abort_message = {"Username and/or password wrong or empty"};
			JOptionPane pane_dialog_abort = new JOptionPane(dialog_abort_message, JOptionPane.OK_OPTION);
			pane_dialog_abort.createDialog(null, "Failed to Login").setVisible(true);
			System.exit(0);
		}
		
		public void dialogProgramFinished() {
			Object[] dialog_abort_message = {"Program finished."};
			JOptionPane pane_dialog_abort = new JOptionPane(dialog_abort_message, JOptionPane.OK_OPTION);
			pane_dialog_abort.createDialog(null, "Finished").setVisible(true);
			System.exit(0);
		}

		
		
		//Getter&Setter
		
		public String getGrundkosten() {
			return grundkosten;
		}

		public void setGrundkosten(String grundkosten) {
			this.grundkosten = grundkosten;
		}

		public String getKwh() {
			return kwh;
		}

		public void setKwh(String kwh) {
			this.kwh = kwh;
		}
		
		
		
}
