package GatherTransferDeletion;

import java.io.File;

// transfers separated datasets to Ausgaben_yyyy\carsharing
public class ContentTransferer {

	private String home = System.getProperty("user.home");
	private String workspace = System.getProperty("user.dir");
	private String mailDataPath = "\\Desktop\\Annis Denka\\.temp"; // holds all DataSets

	private String ausgabenDirPath = "\\Desktop\\Annis Denka\\";
	private String pathToCarsharingFile = "Ausgaben\\";
	private String ausgaben = "Ausgaben "; //name for filtering
	
	//home + ausgabenDirPath + pathToCarsharingFile

	private String[] existingAusgabenDirs; // holds all found 'Ausgaben yyyy' folder names
	
	private String[] allExistingfolder;

	public ContentTransferer() {
		existingAusgabenDirs = filterAusgabenDirs();
	}

	
	//filters directory Annis Denka for Ausgaben folder
	private String[] filterAusgabenDirs() {
		File file = new File(home + ausgabenDirPath);
		File[] directories = file.listFiles();
		
		int fileNamesLength = calcFileNamesLength(directories);
		String[] fileNames = new String[fileNamesLength];

		int arrayPlacement = 0;
		
		for (int i = 0; i < directories.length; i++) {
			if (directories[i].getName().contains(ausgaben)) {
				fileNames[arrayPlacement] = directories[i].getName();
				arrayPlacement++;
			}
		}
		return fileNames;
	}

	//calculates length for array which holds all found Ausgaben directories
	private int calcFileNamesLength(File[] directories) {
		int temp = 0;
		for (int i = 0; i < directories.length; i++) {
			if (directories[i].getName().contains(ausgaben)) {
				temp++;
			}
		}
		return temp;
	}

	//gets all existing directories in Ausgaben yyyy
	private String[] filterForAllFolderInExistingAusgabeDir() {
		String[] temp = null;
	
		return temp;
	}
	
	public void copyFileToMatchingDirectory() {

	}

	private void renameFile() {

	}

}
