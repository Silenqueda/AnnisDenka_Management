import java.io.*;
import java.util.Properties;
import javax.mail.*;

public class MailReceiver {

	public static void receiveMails(String userName, String password) {

		try {
			Properties properties = new Properties();
			properties.setProperty("mail.store.protocol", "imaps");

			Session emailSession = Session.getDefaultInstance(properties);
			Store emailStore = emailSession.getStore("imaps");
			emailStore.connect("imap.gmx.net", userName, password); // ### MessagingExecption
			
			Folder emailFolder = emailStore.getFolder("Inbox");
			System.out.println("main folder: " + emailFolder);
			
			Folder[] emailFolderList = emailStore.getDefaultFolder().list("INBOX/*");
			for(Folder f : emailFolderList) {
				System.out.println("subfolder: " + f);
			}
			
		
		
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		receiveMails("d.b.htw@gmx.de", "dennis22.");
	}

}
