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

	public static void receiveMails(String userName, String password) {
		
		/**
		 * 0 = Datum am Tag der Fahrt
		 * 1 = Kosten der Fahrt 
		 */	
		final String REGEX_CARSHARING = "in Höhe von ([0-9]+,[0-9]{2}).EUR";
		
		String[] folderNamesToFetch = {
			"INBOX/Carsharing"//,
			//"INBOX/Carsharing/*"
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
			
			for(Folder f : emailFolderList) {
				f.open(Folder.READ_ONLY);
				messages = f.getMessages();
				for(Message m : messages) {
					System.out.println(getFilteredContentFromMessage(m, REGEX_CARSHARING));
				}
			}
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		} 
	}
	
	
	/**
	 * Methodes provides date and expanse of mail content
	 * @param message: the mail
	 * @param regexCarsharing: regex to find xx,xx€ in text
	 * @return date & expanse
	 */
	public static String getFilteredContentFromMessage(Message message, String regexCarsharing) {
		try {
			Object content = message.getContent();
			if(content instanceof Multipart) {
				StringBuffer messageContent = new StringBuffer();
				Multipart multipart = (Multipart) content;
				for(int i = 0; i < multipart.getCount(); i++) {
					Part part = multipart.getBodyPart(i);
					if(part.isMimeType("text/plain")) {
						messageContent.append(getDateFromMessage(message));
						messageContent.append("\t");
						messageContent.append(filterMessage(part.getContent().toString(), regexCarsharing, message));
					}
				}
				return messageContent.toString();
			}
			//return content.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		} 
		return "";
	}
	
	private static String filterMessage(String messageToFilter, String regexCarsharing, Message message) {
		String filteredMessage = "";
		
		Pattern pattern_expanse = Pattern.compile(regexCarsharing);
		Matcher matcher_expanse = pattern_expanse.matcher(messageToFilter);
		
		if(matcher_expanse.find()) {
			filteredMessage += matcher_expanse.group(1) + "€";
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
		
		return day + "." + (month+1) + "." + year;
	}
	
	
	public static void main(String[] args) {
		receiveMails("d.b.htw@gmx.de", "dennis22.");
	}

}
