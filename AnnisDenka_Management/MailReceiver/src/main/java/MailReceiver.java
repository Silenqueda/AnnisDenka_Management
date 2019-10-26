import java.io.*;
import java.io.ObjectInputStream.GetField;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.*;
import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;

public class MailReceiver {

	private String lastWrittenDate;

	public static void receiveMails(String userName, String password) {

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
			emailStore.connect("imap.gmx.net", userName, password); // ### MessagingExecption

			Folder emailFolder = emailStore.getFolder("Inbox");
			Folder[] emailFolderList = emailStore.getDefaultFolder().list("INBOX/Carsharing");

			Message[] messages;

			String mailContent = "";

			for (Folder f : emailFolderList) {
				f.open(Folder.READ_ONLY);
				messages = f.getMessages();
				for (Message m : messages) {
					mailContent += getFilteredContentFromMessage(m, REGEX_CARSHARING_AMOUNT,
							REGEX_CARSHARING_CHARGE_DATE);
					mailContent += System.lineSeparator();
					// System.out.println(mailContent);
				}
			}

			createFileForMailData(mailContent);

		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Methodes provides date and expanse of mail content
	 * 
	 * @param message:         the mail
	 * @param regexCarsharing: regex to find xx,xx€ in text
	 * @return date & expanse
	 */
	public static String getFilteredContentFromMessage(Message message, String regexCarsharing_Amount,
			String regexCarsharing_ChargeDate) {
		try {
			Object content = message.getContent();
			if (content instanceof Multipart) {
				StringBuffer messageContent = new StringBuffer();
				Multipart multipart = (Multipart) content;
				for (int i = 0; i < multipart.getCount(); i++) {
					Part part = multipart.getBodyPart(i);
					if (part.isMimeType("text/plain") && filterMessageForChargeDate(part.getContent().toString(),
							regexCarsharing_ChargeDate, message) != "") {
						messageContent.append(getDateFromMessage(message));
						messageContent.append("\t");
						messageContent.append(filterMessageForChargeDate(part.getContent().toString(),
								regexCarsharing_ChargeDate, message));
						messageContent.append("\t");
						messageContent.append(
								filterMessageForAmount(part.getContent().toString(), regexCarsharing_Amount, message));
					}
				}
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
			filteredMessage += matcher_expanse.group(1) + "€";
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
		String workspace = "C:\\Users\\Dennis\\Tutorial\\GIT\\AnnisDenka_Management\\MailReceiver\\";
		String subPath = "temp\\";
		String fileName = "mailData";
		String absoluteFilePath = workspace + subPath + fileName;
		System.out.println(absoluteFilePath);
		writeToFile(absoluteFilePath, mailContent);
	}

	private static void writeToFile(String pathNameForFile, String content) {
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

	public static void main(String[] args) {

		receiveMails("d.b.htw@gmx.de", "dennis22.");
	}

}
