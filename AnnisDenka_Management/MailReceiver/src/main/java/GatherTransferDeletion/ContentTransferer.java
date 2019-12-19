package GatherTransferDeletion;

import java.io.File;

// transfers separated datasets to Ausgaben_yyyy\carsharing
public class ContentTransferer {

	private String home = System.getProperty("user.home");
	private String workspace = System.getProperty("user.dir");
	private String mailDataPath = "\\Desktop\\Annis Denka\\.temp"; // holds all DataSets

	private String ausgabenDirPath = "\\Desktop\\Annis Denka\\";
	private String ausgaben = "Ausgaben ";

	private String[] existingAusgabenDirs;

	public ContentTransferer() {

	}

	
	//filters directory Annis Denka for Ausgaben folder
	public String[] filterAusgabenDirs() {
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

	private int calcFileNamesLength(File[] directories) {
		int temp = 0;
		for (int i = 0; i < directories.length; i++) {
			if (directories[i].getName().contains(ausgaben)) {
				temp++;
			}
		}
		return temp;
	}

	public void copyFileToMatchingDirectory() {

	}

	private void renameFile() {

	}

}
