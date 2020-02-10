package Handler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import Database.DBConnection;
import View.Overview;

public class DataProvider {

	private DBConnection dbc;
	private Overview view;
	private InputChecker inputChecker;

	private String sumOfExpansesOrderedByPayDate = "expanses summed up and ordered by month:" + System.lineSeparator();
	private String allExistingPayDates = "";

	public DataProvider() {
		dbc = new DBConnection();
		view = new Overview();
		//gatherInformation();
	}

	/**
	 * queries
	 * 
	 * @throws IOException
	 */
	private void gatherInformation() throws IOException {

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
		while (br.readLine() != null) {
			insertContent += br.readLine();
		}

		query += insertContent;
		// TODO: query the insert to table!

	}

	/**
	 * inserts dataset to given table
	 * @param table_name table to write to
	 * @param value_date date
	 * @param value_price price
	 * @param value_descriptionText text
	 */
	public void write_toDatabase(String table_name, String value_date, float value_price,
			String value_descriptionText) {
		// connect to database
		dbc = new DBConnection();
		dbc.connectToDB();
		Connection connection = dbc.getConnection();
		// build query
		String query = buildQuery(table_name, value_date, value_price, value_descriptionText);
		System.out.println(query);
		// send to database
		try {
			// create statement
			Statement statement = connection.createStatement();
			// insert data
			statement.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		dbc.closeConnection();

	}

	/**
	 * helper method.
	 * 
	 * build insert into query for generic table name
	 * 
	 * @param food_tablename
	 */
	private String buildQuery(String table_name, String value_date, float value_price, String value_descriptionText) {
		String query_start = "insert into ";
		String values = "values(";
		String query_end = ");";
		String comma = ",";
		String high = "'";

		ResultSetMetaData rsmd = getColumnNamesFromTable(table_name);

		try {
			return query_start + table_name + "(" + rsmd.getColumnName(2) + comma + rsmd.getColumnName(3) + comma
					+ rsmd.getColumnName(4) + ")" + values + high + value_date + high + comma + high + value_price
					+ high + comma + high + value_descriptionText + high + query_end;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * helper mehtod
	 * 
	 * gets all column names from given table
	 * @param table_name
	 * @return
	 */
	private ResultSetMetaData getColumnNamesFromTable(String table_name) {
		String query = "select * from " + table_name;
		Statement st;
		ResultSetMetaData rsmd = null;
		try {
			st = this.dbc.getConnection().createStatement();
			ResultSet rs = st.executeQuery(query);
			return rsmd = rs.getMetaData();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rsmd;
	}

	private void handInformationToView() {

	}

}
