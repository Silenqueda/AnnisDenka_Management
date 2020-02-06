package Main;
import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.*;

import DataObjects.Expanse;
import GUI.GUI_popup;
import GatherTransferDeletion.ContentTransferer;
import GatherTransferDeletion.DataGatherer;

public class MailReceiver {

	private String lastWrittenDate;
	private static List<Expanse> listOfExpanses;

	public static void receiveMails(String userName, String password, GUI_popup getCredentials) throws IOException {

		/**
		 * 0 = Datum am Tag der Fahrt 1 = Kosten der Fahrt
		 */
		final String REGEX_CARSHARING_AMOUNT = "in Höhe von ([0-9]+,[0-9]{2}).EUR";
		final String REGEX_CARSHARING_CHARGE_DATE = "SEPA-Lastschrift zum.([0-9]{2}\\.[0-9]{2}\\.[0-9]{2,4})";

		String[] folderNamesToFetch = { "INBOX/Carsharing"// ,
				// "INBOX/Carsharing/*"
		};

		try {
			Properties properties = new Properties();
			properties.setProperty("mail.store.protocol", "imaps");

			Session emailSession = Session.getDefaultInstance(properties);
			Store emailStore = emailSession.getStore("imaps");
			System.out.println("*** MailReceiver -> Connecting to mail account ***");
			emailStore.connect("imap.gmx.net", userName, password); // ### MessagingExecption

			Folder emailFolder = emailStore.getFolder("Inbox");
			Folder[] emailFolderList = emailStore.getDefaultFolder().list("INBOX/Carsharing");

			Message[] messages;

			String mailContent = "";

			System.out.println("*** MailReceiver -> Reading mail content ***");
			for (Folder f : emailFolderList) {
				f.open(Folder.READ_ONLY);
				messages = f.getMessages();
				for (Message m : messages) {
					mailContent += getFilteredContentFromMessage(m, REGEX_CARSHARING_AMOUNT,
							REGEX_CARSHARING_CHARGE_DATE);					
					mailContent += System.lineSeparator();
					System.out.println(mailContent);
				}
			}
			
			
			mailContent += changeLastLine();

			createFileForMailData(mailContent);

		} catch (NoSuchProviderException e) {
			getCredentials.dialogFailedLoginAttemp();
		} catch (MessagingException e) {
			getCredentials.dialogFailedLoginAttemp();
		}
	}
	
	private static String changeLastLine() throws IOException {
		BufferedReader input = new BufferedReader(new FileReader("D:\\Private\\AnnisDenka_Management\\AnnisDenka_Management\\MailReceiver\\temp\\mailData_all.txt"));
		String last = "", line = "", last_replaced = "";
		
		while ((line = input.readLine()) != null) {
			if(line.endsWith(",")) {
				last = line;
			}
		}
		System.out.println(last);
		last_replaced = last.substring(0, last.length() -1) + ";";
		System.out.println(last_replaced);
		return last_replaced;		
	}

	/**
	 * Methodes provides date and expanse of mail content
	 * 
	 * @param message: the mail
	 * @param regexCarsharing: regex to find xx,xx€ in text
	 * @return date & expanse
	 */
	public static String getFilteredContentFromMessage(Message message, String regexCarsharing_Amount,
			String regexCarsharing_ChargeDate) {

		String date_driven = "";
		String date_payment = "";
		String amountToPay = "";

		Expanse expanse;

		try {
			Object content = message.getContent();
			if (content instanceof Multipart) {
				StringBuffer messageContent = new StringBuffer();
				Multipart multipart = (Multipart) content;
				for (int i = 0; i < multipart.getCount(); i++) {
					Part part = multipart.getBodyPart(i);
					if (part.isMimeType("text/plain") && filterMessageForChargeDate(part.getContent().toString(),
							regexCarsharing_ChargeDate, message) != "") {
						
						date_driven = getDateFromMessage(message);
						messageContent.append("('"+date_driven+"'");
						messageContent.append(",");

						date_payment = filterMessageForChargeDate(part.getContent().toString(),
								regexCarsharing_ChargeDate, message);
						messageContent.append("'"+date_payment+"'");
						messageContent.append(",");

						amountToPay = filterMessageForAmount(part.getContent().toString(), regexCarsharing_Amount,
								message);
						messageContent.append(amountToPay+")");
						messageContent.append(",");
					}
				}
				
				//build object expanse
				addExpanseToList(date_driven, date_payment, amountToPay);
				
				return messageContent.toString();
			}
			// return content.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return "";
	}

	private static String filterMessageForAmount(String messageToFilter, String regexCarsharing_Amount,
			Message message) {
		String filteredMessage = "";
		Pattern pattern_expanse = Pattern.compile(regexCarsharing_Amount);
		Matcher matcher_expanse = pattern_expanse.matcher(messageToFilter);
		if (matcher_expanse.find()) {
			filteredMessage += matcher_expanse.group(1).replace(",", ".");
		}
		return filteredMessage;
	}

	private static String filterMessageForChargeDate(String messageToFilter, String regexCarsharing_ChargeDate,
			Message message) {
		String filteredMessage = "";
		Pattern pattern_expanse = Pattern.compile(regexCarsharing_ChargeDate);
		Matcher matcher_expanse = pattern_expanse.matcher(messageToFilter);
		if (matcher_expanse.find()) {
			filteredMessage += matcher_expanse.group(1);
		}
		return filteredMessage;
	}

	private static String getDateFromMessage(Message message) throws MessagingException {
		Date date = message.getReceivedDate();
		Calendar calender = Calendar.getInstance();
		calender.setTime(date);
		int day = calender.get(Calendar.DAY_OF_MONTH);
		int month = calender.get(Calendar.MONTH);
		int year = calender.get(Calendar.YEAR);
		return day + "." + (month + 1) + "." + year;
	}

	private static boolean createFile_LastWrittenDate() {
		// TODO: Location!
		return false;
	}

	private String getLastWrittenDateFromFile() {
		if (lastWrittenDate == null) {
			createFile_LastWrittenDate();
			return "";
		} else {
			// TODO
			return "TODO: LAST WRITTEN DATE!";
		}
	}

	private static void createFileForMailData(String mailContent) {
		String workspace_eclipse = System.getProperty("user.dir") + "\\";
		String workspace_execJar = System.getProperty("user.dir") + "\\..\\";

		String subPath_eclipse = "temp\\";
		String subPath_execJar = ".temp\\";

		String fileName = "mailData_all.txt";

		String absoluteFilePath;

		if (new File("..\\" + subPath_execJar).exists()) {
			absoluteFilePath = "..\\" + subPath_execJar + fileName;
		} else {
			absoluteFilePath = workspace_eclipse + subPath_eclipse + fileName;
		}
		System.out.println("*** MailReceiver -> File created at " + absoluteFilePath + " ***");
		writeToFile_mailData_all(absoluteFilePath, mailContent);
	}

	private static void writeToFile_mailData_all(String pathNameForFile, String content) {
		Writer writer = null;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(pathNameForFile), "utf-8"));
			writer.write(content);
		} catch (IOException ex) {
		} finally {
			try {
				writer.close();
			} catch (Exception ex) {
				/* ignore */}
		}
	}

	private void getDateToPay() {

	}

	private static void createSubFilesFromMailData_all() {
		
		for(int i = 0; i < listOfExpanses.size(); i++) {
			String date_payment = listOfExpanses.get(i).getDate_payment();
			if(!new File("C:\\Users\\dbr\\Desktop\\Annis Denka\\.temp\\" + date_payment).exists()) {
				File f = new File("C:\\Users\\dbr\\Desktop\\Annis Denka\\.temp\\" + date_payment);
			}
		}
		
	}
	
//	private static String parseDate_payment(String date_payment) {
//		
//		String 
//		
//		return parsed;
//	}
	
	private static void writeToSubFiles() {

	}

	private static void addExpanseToList(String date_driven, String date_payment, String amountToPay) {
		if (listOfExpanses == null) {
			listOfExpanses = new ArrayList<Expanse>();
		}
		listOfExpanses.add(new Expanse(date_driven, date_payment, amountToPay));
	}

	private static void exitProgram(GUI_popup getCredentials) {
		getCredentials.dialogProgramFinished();
	}
	
	
	public static String tempToString() {
		String temp = "";
		
		for(int i = 0; i < listOfExpanses.size(); i++) {
			temp += listOfExpanses.get(i).toString() + System.lineSeparator();
		}
		return temp;
	}
	

	public static void main(String[] args) throws ParseException, IOException {

		
		// get login credentials
		GUI_popup getCredendtials = new GUI_popup();
		// login to provided mail account
		receiveMails(getCredendtials.getUserName(), getCredendtials.getPassword(), getCredendtials);
		
		//testing of DataGatherer
		DataGatherer dg = new DataGatherer();
		dg.readFile();
		dg.separateDataSetByDateToPay();
		
		//testing of ContentTransferer
		ContentTransferer ct = new ContentTransferer();
		
		// finishes program
		exitProgram(getCredendtials);
	}

}
