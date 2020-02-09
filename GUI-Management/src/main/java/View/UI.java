package View;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import Database.DBConnection;
import Handler.DataProvider;

public class UI extends JFrame {
	
	DataProvider dataProvider = new DataProvider();
	DBConnection dbc;

	// Food input
	private final String food_tablename = "expanse_food";
	private String food_date;
	private float food_price;
	private String food_description;

	// Clothes input
	private final String clothes_tablename = "expanse_clothes";
	private String clothes_date;
	private float clothes_price;
	private String clothes_description;

	// Cosmetics input
	private final String cosmetics_tablename = "expanse_cosmetics";
	private String cosmetics_date;
	private float cosmetics_price;
	private String cosmetics_description;

	// Other input
	private final String other_tablename = "expanse_other";
	private String other_date;
	private float other_price;
	private String other_description;

	// Constructor
	public UI() {
		this.createMainFrame();
	}

	private void createMainFrame() {
		this.setName("");
		this.setSize(800, 600);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		/**
		 * ######################################################### 
		 * Food section
		 * #########################################################
		 */

		// Food Label - StatusText
		final JLabel label_food_statusText = new JLabel();
		label_food_statusText.setBounds(620, 12, 200, 20);
		this.add(label_food_statusText);

		// Food - Label - food
		JLabel label_food_section = new JLabel("Food section");
		label_food_section.setBounds(10, 13, 200, 20);
		this.add(label_food_section);

		// Food - Textfield - Date
		final JTextField textfield_food_date = new JTextField();
		textfield_food_date.replaceSelection("dd.mm.yyyy");
		textfield_food_date.setBounds(110, 13, 100, 20);
		this.add(textfield_food_date);

		// Food - Textfield - Price
		final JTextField textfield_food_price = new JTextField();
		textfield_food_price.replaceSelection("0.00");
		textfield_food_price.setBounds(220, 13, 60, 20);
		this.add(textfield_food_price);

		// Food - Label - money
		JLabel label_food_money = new JLabel("€");
		label_food_money.setBounds(282, 12, 20, 20);
		this.add(label_food_money);

		// Food - Textfield - Description
		final JTextField textfield_food_descriptionText = new JTextField();
		textfield_food_descriptionText.replaceSelection("description");
		textfield_food_descriptionText.setBounds(300, 13, 200, 20);
		this.add(textfield_food_descriptionText);

		// Food - Button - Send & Check
		JButton button_food_check = new JButton("check");
		final JButton button_food_send = new JButton("send");

		// Food - Button - Bounds
		button_food_check.setBounds(510, 12, 100, 20);
		button_food_send.setBounds(510, 35, 100, 20);

		// Food Button action - check
		button_food_send.setEnabled(false);
		button_food_check.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				boolean passed = false;

				if (textfieldPrice_checkForWrongCharacters(textfield_food_price.getText())) {
					food_date = textfield_food_date.getText();
					food_price = checkTextFieldPriceInput(textfield_food_price.getText());
					food_description = textfield_food_descriptionText.getText();
					label_food_statusText.setText("input check passed");
					passed = true;
					button_food_send.setEnabled(true);
					
				} else {
					label_food_statusText.setText("input check failed");
				}
			}

		});
		this.add(button_food_check);

		// Food Button action - send
		button_food_send.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				// Send data to database
				dataProvider.write_toDatabase(food_tablename, food_date, food_price, food_description);
				
				button_food_send.setEnabled(false);
				label_food_statusText.setText("sent to database");
			}
		});
		this.add(button_food_send);

		/**
		 * ######################################################### 
		 * Clothes section
		 * #########################################################
		 */

		// clothes Label - StatusText
		final JLabel label_clothes_statusText = new JLabel();
		label_clothes_statusText.setBounds(620, 72, 200, 20);
		this.add(label_clothes_statusText);

		// clothes - Label - clothes
		JLabel label_clothes_section = new JLabel("Clothes section");
		label_clothes_section.setBounds(10, 72, 200, 20);
		this.add(label_clothes_section);

		// clothes - Textfield - Date
		final JTextField textfield_clothes_date = new JTextField();
		textfield_clothes_date.replaceSelection("dd.mm.yyyy");
		textfield_clothes_date.setBounds(110, 72, 100, 20);
		this.add(textfield_clothes_date);

		// clothes - Textfield - Price
		final JTextField textfield_clothes_price = new JTextField();
		textfield_clothes_price.replaceSelection("0.00");
		textfield_clothes_price.setBounds(220, 72, 60, 20);
		this.add(textfield_clothes_price);

		// clothes - Label - money
		JLabel label_clothes_money = new JLabel("€");
		label_clothes_money.setBounds(282, 72, 20, 20);
		this.add(label_clothes_money);

		// clothes - Textfield - Description
		final JTextField textfield_clothes_descriptionText = new JTextField();
		textfield_clothes_descriptionText.replaceSelection("description");
		textfield_clothes_descriptionText.setBounds(300, 73, 200, 20);
		this.add(textfield_clothes_descriptionText);

		// clothes - Button - Send & Check
		JButton button_clothes_check = new JButton("check");
		final JButton button_clothes_send = new JButton("send");

		// clothes - Button - Bounds
		button_clothes_check.setBounds(510, 72, 100, 20);
		button_clothes_send.setBounds(510, 95, 100, 20);

		// clothes Button action - check
		button_clothes_send.setEnabled(false);
		button_clothes_check.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				boolean passed = false;

				if (textfieldPrice_checkForWrongCharacters(textfield_clothes_price.getText())) {
					clothes_date = textfield_clothes_date.getText();
					clothes_price = checkTextFieldPriceInput(textfield_clothes_price.getText());
					clothes_description = textfield_clothes_descriptionText.getText();
					label_clothes_statusText.setText("input check passed");
					passed = true;
					button_clothes_send.setEnabled(true);
				} else {
					label_clothes_statusText.setText("input check failed");
				}
			}
		});
		this.add(button_clothes_check);

		// clothes Button action - send
		button_clothes_send.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// Send data to database
				dataProvider.write_toDatabase(clothes_tablename, clothes_date, clothes_price, clothes_description);
				
				button_clothes_send.setEnabled(false);
				label_clothes_statusText.setText("sent to database");
			}
		});
		this.add(button_clothes_send);

		/**
		 * ######################################################### 
		 * Cosmetics section
		 * #########################################################
		 */

		// cosmetics Label - StatusText
		final JLabel label_cosmetics_statusText = new JLabel();
		label_cosmetics_statusText.setBounds(620, 132, 200, 20);
		this.add(label_cosmetics_statusText);

		// cosmetics - Label - cosmetics
		JLabel label_cosmetics_section = new JLabel("Cosmetics section");
		label_cosmetics_section.setBounds(10, 132, 200, 20);
		this.add(label_cosmetics_section);

		// cosmetics - Textfield - Date
		final JTextField textfield_cosmetics_date = new JTextField();
		textfield_cosmetics_date.replaceSelection("dd.mm.yyyy");
		textfield_cosmetics_date.setBounds(110, 132, 100, 20);
		this.add(textfield_cosmetics_date);

		// cosmetics - Textfield - Price
		final JTextField textfield_cosmetics_price = new JTextField();
		textfield_cosmetics_price.replaceSelection("0.00");
		textfield_cosmetics_price.setBounds(220, 132, 60, 20);
		this.add(textfield_cosmetics_price);

		// cosmetics - Label - money
		JLabel label_cosmetics_money = new JLabel("€");
		label_cosmetics_money.setBounds(282, 132, 20, 20);
		this.add(label_cosmetics_money);

		// cosmetics - Textfield - Description
		final JTextField textfield_cosmetics_descriptionText = new JTextField();
		textfield_cosmetics_descriptionText.replaceSelection("description");
		textfield_cosmetics_descriptionText.setBounds(300, 133, 200, 20);
		this.add(textfield_cosmetics_descriptionText);

		// cosmetics - Button - Send & Check
		JButton button_cosmetics_check = new JButton("check");
		final JButton button_cosmetics_send = new JButton("send");

		// cosmetics - Button - Bounds
		button_cosmetics_check.setBounds(510, 132, 100, 20);
		button_cosmetics_send.setBounds(510, 155, 100, 20);

		// cosmetics Button action - check
		button_cosmetics_send.setEnabled(false);
		button_cosmetics_check.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				boolean passed = false;

				if (textfieldPrice_checkForWrongCharacters(textfield_cosmetics_price.getText())) {
					cosmetics_date = textfield_cosmetics_date.getText();
					cosmetics_price = checkTextFieldPriceInput(textfield_cosmetics_price.getText());
					cosmetics_description = textfield_cosmetics_descriptionText.getText();
					label_cosmetics_statusText.setText("input check passed");
					passed = true;
					button_cosmetics_send.setEnabled(true);
				} else {
					label_cosmetics_statusText.setText("input check failed");
				}
			}
		});
		this.add(button_cosmetics_check);

		// cosmetics Button action - send
		button_cosmetics_send.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// Send data to database
				dataProvider.write_toDatabase(cosmetics_tablename, cosmetics_date, cosmetics_price, cosmetics_description);
				
				button_cosmetics_send.setEnabled(false);
				label_cosmetics_statusText.setText("sent to database");
			}
		});
		this.add(button_cosmetics_send);

		/**
		 * ######################################################### 
		 * Other section
		 * #########################################################
		 */

		// other Label - StatusText
		final JLabel label_other_statusText = new JLabel();
		label_other_statusText.setBounds(620, 192, 200, 20);
		this.add(label_other_statusText);

		// other - Label - other
		JLabel label_other_section = new JLabel("Other section");
		label_other_section.setBounds(10, 192, 200, 20);
		this.add(label_other_section);

		// other - Textfield - Date
		final JTextField textfield_other_date = new JTextField();
		textfield_other_date.replaceSelection("dd.mm.yyyy");
		textfield_other_date.setBounds(110, 192, 100, 20);
		this.add(textfield_other_date);

		// other - Textfield - Price
		final JTextField textfield_other_price = new JTextField();
		textfield_other_price.replaceSelection("0.00");
		textfield_other_price.setBounds(220, 192, 60, 20);
		this.add(textfield_other_price);

		// other - Label - money
		JLabel label_other_money = new JLabel("€");
		label_other_money.setBounds(282, 192, 20, 20);
		this.add(label_other_money);

		// other - Textfield - Description
		final JTextField textfield_other_descriptionText = new JTextField();
		textfield_other_descriptionText.replaceSelection("description");
		textfield_other_descriptionText.setBounds(300, 193, 200, 20);
		this.add(textfield_other_descriptionText);

		// other - Button - Send & Check
		JButton button_other_check = new JButton("check");
		final JButton button_other_send = new JButton("send");

		// other - Button - Bounds
		button_other_check.setBounds(510, 192, 100, 20);
		button_other_send.setBounds(510, 218, 100, 20);

		// other Button action - check
		button_other_send.setEnabled(false);
		button_other_check.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				boolean passed = false;

				if (textfieldPrice_checkForWrongCharacters(textfield_other_price.getText())) {
					other_date = textfield_other_date.getText();
					other_price = checkTextFieldPriceInput(textfield_other_price.getText());
					other_description = textfield_other_descriptionText.getText();
					label_other_statusText.setText("input check passed");
					passed = true;
					button_other_send.setEnabled(true);
				} else {
					label_other_statusText.setText("input check failed");
				}
			}
		});
		this.add(button_other_check);

		// other Button action - send
		button_other_send.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// Send data to database
				dataProvider.write_toDatabase(other_tablename, other_date, other_price, other_description);
				
				button_other_send.setEnabled(false);
				label_other_statusText.setText("sent to database");
			}
		});
		this.add(button_other_send);

		this.setLayout(null);
		this.setVisible(true);
	}

	/**
	 * Error correction
	 * 
	 * checks textField price input: xx,xx -> xx.xx
	 * 
	 * @param content
	 * @return changed text in float format
	 */
	private float checkTextFieldPriceInput(String content) {
		if (content.contains(",")) {
			content = content.replace(",", ".");
		}
		return Float.parseFloat(content);
	}

	/**
	 * Error check
	 * 
	 * checks if input contains any characters only numbers are allowed
	 * 
	 * @param content
	 * @return true if passed
	 */
	private boolean textfieldPrice_checkForWrongCharacters(String content) {
		String regEx = "[a-zA-Z]";
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(content);

		if (matcher.find()) {
			return false;
		}
		return true;
	}
	
	

}
