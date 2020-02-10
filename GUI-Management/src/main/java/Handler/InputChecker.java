package Handler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputChecker {

	private static boolean noErrors;
	private static String regEx_date = "([0-3]{1}[0-9]{1})\\.([0-1]{1}[0-9]{1})\\.([0-9]{4})";

	public static String checkInputValues(String date, String price, String section) {
		String inputInfo = "<html>" + section;
		String inputInfoEnd = "</html>";
		noErrors = false;

		// Date input checks
		inputInfo += checkDate_validCharacters(date);		
		inputInfo += checkDate_Month(date);
		inputInfo += checkDate_monthLength(date);
		inputInfo += checkDate_Day(date);
		inputInfo += checkDate_dayLength(date);
		inputInfo += checkDate_Year(date);
		inputInfo += checkDate_YearLengthTooLong(date);
		
		// Price input checks
		inputInfo += checkPrice_empty(price);
		inputInfo += checkPrice_priceContainsOnlyZeros(price);
		inputInfo += checkPrice_invalidCharacters(price);
		inputInfo += checkPrice_centSectionLength(price);

		System.out.println(inputInfo + inputInfoEnd);

		if ((inputInfo + inputInfoEnd).contentEquals("<html>" + inputInfoEnd)) {
			noErrors = true;
		}
		return inputInfo + inputInfoEnd;
	}

	public static boolean isNoErrors() {
		return noErrors;
	}

	
	/*
	 * Date input checks
	 */
	
	private static String checkDate_validCharacters(String date) {
		String info = "";

		Pattern pattern = Pattern.compile(regEx_date);
		Matcher matcher = pattern.matcher(date);

		if (!matcher.find()) {
			info += "Date: date section empty/invalid characters.<br/>";
		}
		return info;
	}

	private static String checkDate_Day(String date) {
		String info = "";

		Pattern pattern = Pattern.compile(regEx_date);
		Matcher matcher = pattern.matcher(date);

		if (matcher.find()) {
			if (matcher.group(1).contains("00")) {
				info += "Date: day cant be 00.<br/>";
			}

			if (Integer.parseInt(matcher.group(1)) > 31) {
				info += "Date: day over 31.<br/>";
			}
		}
		return info;
	}

	private static String checkDate_dayLength(String date) {
		String info = "";
		String regEx_tooManyCharacters = "([a-zA-Z0-9]{3,}\\.)";

		Pattern pattern = Pattern.compile(regEx_tooManyCharacters);
		Matcher matcher = pattern.matcher(date);

		if (matcher.find()) {
			if (matcher.group(1).length() > 3) {
				info += "Date: day has to many characters.<br/>";
			}
		}
		return info;
	}

	private static String checkDate_Month(String date) {
		String info = "";

		Pattern pattern = Pattern.compile(regEx_date);
		Matcher matcher = pattern.matcher(date);

		if (matcher.find()) {
			if (matcher.group(2).contains("00")) {
				info += "Date: month cant be 00.<br/>";
			}

			if (Integer.parseInt(matcher.group(2)) > 12) {
				info += "Date: month cant be over 12.<br/>";
			}
		}
		return info;
	}

	private static String checkDate_monthLength(String date) {
		String info = "";
		String regEx_tooManyCharacters = "(\\.[a-zA-Z0-9]{3,}\\.)";

		Pattern pattern = Pattern.compile(regEx_tooManyCharacters);
		Matcher matcher = pattern.matcher(date);

		if (matcher.find()) {
			if (matcher.group(1).length() > 3) {
				info += "Date: month has to many characters.<br/>";
			}
		}
		return info;
	}

	private static String checkDate_Year(String date) {
		String info = "";
		int curr_Year;

		// current year
		DateFormat dateFormat = new SimpleDateFormat("yyyy");
		Date currdate = new Date();
		curr_Year = Integer.parseInt(dateFormat.format(currdate));

		Pattern pattern = Pattern.compile(regEx_date);
		Matcher matcher = pattern.matcher(date);

		if (matcher.find()) {
			if (Integer.parseInt(matcher.group(3)) > curr_Year) {
				info += "Date: provided year is in future.<br/>";
			}

			if (Integer.parseInt(matcher.group(3)) < curr_Year - 2) {
				info += "Date: provided year is too far in past.<br/>";
			}
		}

		return info;
	}

	private static String checkDate_YearLengthTooLong(String date) {
		String info = "";
		String regEx_yearLength = "(\\.[a-zA-Z0-9]{5,})";

		Pattern pattern = Pattern.compile(regEx_yearLength);
		Matcher matcher = pattern.matcher(date);

		if (matcher.find()) {
			info += "Date: provided year has too many digets.<br/>";
		}
		return info;
	}
	
	

	/*
	 * Price input checks
	 */
	
	private static String checkPrice_empty(String price) {
		String info = "";
		String regEx = "[0-9]*\\.[0-9]{2}";
		
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(price);
		
		if(!matcher.find()) {
			info += "Price: price section is empty.<br/>";
		}
		return info;
	}
	
	private static String checkPrice_priceContainsOnlyZeros(String price) {
		String info = "";
		String regEx = "([0]*)\\.([0]{2})";
		
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(price);
		
		if(matcher.find()) {
			if(matcher.group(1).matches("[0]*") && matcher.group(2).matches("[0]{2}")) {
				info += "Price: price contains only zeros.<br/>";
			}
		}
		return info;
	}
	
	private static String checkPrice_invalidCharacters(String price) {
		String info = "";
		String regEx = "[^0-9.]";
		
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(price);
		
		if(price.startsWith("-")) {
			info += "Price: negativ values not permitted.<br/>";
		}
		
		if(price.endsWith(".")) {
			info += "Price: cent section not provided.<br/>";
		}
		
		if(matcher.find()) {
			info += "Price: caracters not permitted.<br/>";
		}
		return info;
	}
	
	
	private static String checkPrice_centSectionLength(String price) {
		String info = "";
		String regEx = "(\\.[0-9]{3,})";
		
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(price);
		
		if(matcher.find()) {
			info += "Price: cent section to long.<br/>";
		}
		return info;
	}
	
}
