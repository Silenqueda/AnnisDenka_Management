package Handler;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Database.DBConnection;
import View.Overview;

public class DataProvider {

	private DBConnection dbc;
	private Overview view;

	private String sumOfExpansesOrderedByPayDate = "expanses summed up and ordered by month:" + System.lineSeparator();
	private String allExistingPayDates = "";

	public DataProvider() throws IOException {
		dbc = new DBConnection();
		view = new Overview();
		this.gatherInformation();
	}

	/**
	 * queries 
	 * @throws IOException 
	 */
	private void gatherInformation() throws IOException  {

		// sum of carsharing expanses ordered by pay_date
		this.sumOfExpansesOrderedByPayDate += this.read_sumOfExpansesOrderedByPayDate();

		// all existing pay_dates in table -> dropdownmenu
		this.allExistingPayDates = this.read_existingPayDate();
		
		this.write_gatheredMailDataToDatabase();

	}

	/**
	 * provides total expanses ordered by month
	 * 
	 * @return
	 */
	private String read_sumOfExpansesOrderedByPayDate() {
		String query = "select substring(pay_date,4) as month_Year, sum(price) as sum  from expanse_carsharing group by month_Year;";
		String result = "";

		dbc.connectToDB();

		Connection connection = dbc.getConnection();
		Statement statement;
		try {
			statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			while (resultSet.next()) {
				result += resultSet.getString("month_Year") + " " + resultSet.getString("sum") + System.lineSeparator();
			}
		} catch (SQLException e) {
			System.out.println("Failed to connect");
			e.printStackTrace();
		}
		dbc.closeConnection();
		return result;
	}

	/**
	 * provides all existing month where customer drove for future work. used as
	 * dropDownMenuOptions
	 * 
	 * @return
	 */
	private String read_existingPayDate() {
		String result = "";
		String query = "select distinct (to_date(substring(pay_date,4), 'MM-YYYY')) as mmYYYY from expanse_carsharing order by mmYYYY desc;";

		dbc.connectToDB();

		Connection connection = dbc.getConnection();
		Statement statement;
		try {
			statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			while (resultSet.next()) {
				result += resultSet.getString("mmYYYY") + System.lineSeparator();
			}
		} catch (SQLException e) {
			System.out.println("Failed to connect");
			e.printStackTrace();
		}
		dbc.closeConnection();

		return result;
	}

	public void write_gatheredMailDataToDatabase() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(
				"D:\\Private\\AnnisDenka_Management\\AnnisDenka_Management\\MailReceiver\\temp\\mailData_all.txt"));
				
		String query = "";
		
		query += "insert into expanse_carsharing (" + System.lineSeparator();
		query += "drive_date," + System.lineSeparator();
		query += "pay_date," + System.lineSeparator();
		query += "price)" + System.lineSeparator();
		query += "values" + System.lineSeparator();
		
		String insertContent = "";
		while(br.readLine() != null) {
			insertContent += br.readLine();
		}

		query += insertContent;
	 //TODO: query the insert to table!
		
		
	}

	private void handInformationToView() {

	}

}
