import java.util.List;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URISyntaxException;
import java.text.DateFormatSymbols;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Main {

	private String targetDirEnding = "\\Desktop\\Annis Denka\\Ausgaben";
	private String targetDirJars = "\\Desktop\\Annis Denka\\Executable jars"; //### creates folder which holds exec jars
	private String targetDirTemp = "\\Desktop\\Annis Denka\\.temp"; // ### creates folder whoch holds carsharing expanses
	private String targetDirFixed = "\\Desktop\\Annis Denka\\.fixed"; //### creates folder which holds fixed values 
	
	private String envUserProfile;
	private String targetDir;

	private String[] subDirNames = { "Ausgaben", "Stromz�hler" };

	private String[] fileNames_Einkaeufe = { ".gesamt = .txt", " - andere K�ufe.txt",
			" - Haushaltsartikel & Kosmetik.txt", " - Kleidung.txt", " - Nahrungsmittel.txt", " - Carsharing.txt" };

	private String[] fileNames_Stromzaehler = { " - Stromz�hler = .txt" };
	
	private String[] fileNames_fixed = {"Stromkosten.txt"};

	private String curDate;


	private List allFilePaths;


	// --------------------------------------------------//

	public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException {

		Main main = new Main();
		main.curDate = main.getCurrentDate();

		main.setEnvUserprofile();
		main.setTargetDir();

		main.createDir();
		main.createSubDir();
		main.createFiles();
		
		System.exit(0);

	}

	private void setEnvUserprofile() {
		this.envUserProfile = System.getProperty("user.home");
	}

	private void setTargetDir() {
		this.targetDir = envUserProfile + targetDirEnding + " " + splitDate()[1];
	}

	private String getCurrentMonth() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM");
		LocalDateTime now = LocalDateTime.now();
		return dtf.format(now).toString();
	}

	private String getCurrentYear() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("YYYY");
		LocalDateTime now = LocalDateTime.now();
		return dtf.format(now).toString();
	}

	/**
	 * Method builds current date
	 * 
	 * @return current date in format MM_YYYY
	 */
	private String getCurrentDate() {
		return this.getCurrentMonth() + "_" + this.getCurrentYear();
	}


	private String[] splitDate() {
		return curDate.split("_");
	}

	/**
	 * creates root dir at specified location
	 */
	private void createDir() {
		new File(buildRootDirString()).mkdirs();
		new File(buildRootDirExecutables()).mkdirs();
		new File(buildRootDirTemp()).mkdirs();
		new File(buildRootDirFixed()).mkdirs();
	}

	private String buildRootDirString() {
		return targetDir + "/" + this.curDate;
	}
	
	private String buildRootDirExecutables() {
		return envUserProfile + this.targetDirJars;
	}
	
	private String buildRootDirTemp() {
		return envUserProfile + this.targetDirTemp;
	}
	
	private String buildRootDirFixed() {
		return envUserProfile + this.targetDirFixed;
	}

	private void createSubDir() {
		for (String s : subDirNames) {
			new File(buildRootDirString() + "/" + s).mkdirs();
		}
	}

	private String[] getDirNames() {
		File file = new File(buildRootDirString());
		String[] directories = file.list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return new File(dir, name).isDirectory();
			}
		});
		return directories;
	}

	/**
	 * creates files in dir Eink�ufe
	 * 
	 * @throws IOException
	 */
	private void createFiles() throws IOException {
		String[] dirNames = getDirNames();
		for (String ds : dirNames) {
			if (ds.contentEquals(subDirNames[0])) {
				createTargetFile(subDirNames[0]);
			}
			if (ds.contentEquals(subDirNames[1])) {
				createTargetFile(subDirNames[1]);
			}
		}
		creatFiles_Stromkosten();
	}
	
	// creates File Stromkosten and calls a popup
	private void creatFiles_Stromkosten() {
		if(!new File(targetDirFixed+"\\"+fileNames_fixed[0]).exists()) {
			String name = buildRootDirFixed()+"\\"+fileNames_fixed[0];
			File fixed = new File(name);
			enterStromkostenValues(fixed);
		}
	}
    
	// calls a poup and user enters values if file is not existing
	private void enterStromkostenValues(File fixed) {
		PopUp pop = new PopUp();
		writeToStromkosten(pop, fixed);
	}

	// writes values to created file Stromkosten
	private void writeToStromkosten(PopUp pop, File fixed) {
		String grundkosten = pop.getGrundkosten();
		String kwh = pop.getKwh();
		
		System.out.println(grundkosten + "   " + kwh);
		
		Writer writer = null;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(fixed)));
			writer.write(defaultContentofFile_fixed(grundkosten, kwh));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (Exception ex) {
				/* ignore */}
		}
	}

	private void createTargetFile(String pathNameForFile) throws IOException {
		String content = "";
		if (pathNameForFile == subDirNames[0]) {
			content = defaultContentOfFile();
			for (String fs : fileNames_Einkaeufe) {
				if (fs.startsWith(".")) {
					writeToFile(fs, pathNameForFile, content);
				} else {
					writeToFile(curDate + " " + fs, pathNameForFile, content);
				}
			}
		}
		if (pathNameForFile == subDirNames[1]) {
			content = defaultContentOfFile_Strom();
			for (String fs : fileNames_Stromzaehler) {
				if (fs.startsWith(".")) {
					writeToFile(curDate + " " + fs, pathNameForFile, content);
				} else {
					writeToFile(curDate + " " + fs, pathNameForFile, content);
				}
			}
		}

		setAllFilePahts(provideAllAbsolutFilePaths());
	}

	public List getAllFilePahts() {
		return allFilePaths;
	}

	public void setAllFilePahts(List allFilePahts) {
		this.allFilePaths = allFilePahts;
	}

	private void writeToFile(String fileName, String pathNameForFile, String content) {

		Writer writer = null;
		File temp = new File(buildRootDirString() + "/" + pathNameForFile + "/" + fileName);
		
		if(!temp.exists()) { 
			try {
				writer = new BufferedWriter(new OutputStreamWriter(
						new FileOutputStream(buildRootDirString() + "/" + pathNameForFile + "/" + fileName),
						"utf-8"));
				writer.write(content);
			} catch (IOException ex) {
			} finally {
				try {
					writer.close();
				} catch (Exception ex) {
					/* ignore */}
			}
		}
	}

	private String defaultContentOfFile() {
		String fileContent = convertIntToMonth(splitDate()[0]) + " " + splitDate()[1] + System.lineSeparator();
		fileContent += "-------------------------------" + System.lineSeparator();
		fileContent += "					  " + System.lineSeparator();
		fileContent += "===============================";
		return fileContent;
	}
	
	private String defaultContentofFile_fixed(String grundkosten, String kwh) {
		String fileContent = "Grundkosten: " + grundkosten + System.lineSeparator();
		fileContent += "Preis kwh: " + kwh + System.lineSeparator();
		return fileContent;
	}

	private String defaultContentOfFile_Strom() {
		String fileContent = convertIntToMonth(splitDate()[0]) + " " + splitDate()[1] + System.lineSeparator();
		fileContent += System.lineSeparator();
		fileContent += "##################################" + System.lineSeparator();
		fileContent += "# Grundpreis: 8,20 �/Monat" + System.lineSeparator();
		fileContent += "# Verbrauchspreis: 31,14 Cent/kWh" + System.lineSeparator();
		fileContent += "##################################" + System.lineSeparator();
		fileContent += System.lineSeparator();
		fileContent += "Beginn des Monats:" + System.lineSeparator();
		fileContent += "Ende des Monats:" + System.lineSeparator();
		fileContent += "Verbrauch: kwh";
		fileContent += System.lineSeparator();
		fileContent += "Verbrauch: Euro";

		return fileContent;
	}

	private String convertIntToMonth(String numberOfMonth) {
		DateFormatSymbols dfs = new DateFormatSymbols();
		String[] months = dfs.getMonths();
		String convertedMonth = months[Integer.parseInt(numberOfMonth) - 1];
		return convertedMonth;
	}

	private ArrayList<String> provideAllAbsolutFilePaths() {
		String[] fileNames = fileNames_Einkaeufe;
		ArrayList<String> allFilePaths = new ArrayList<String>();

		for (int i = 0; i < fileNames.length; i++) {
			allFilePaths.add(targetDir + "\\" + subDirNames[0] + "\\" + curDate + " " + fileNames[i]);
		}
		return allFilePaths;
	}

	private String provideAbsolutFilePaths_Carsharing() {
		List allFilePaths = getAllFilePaths();
		for (int i = 0; i < allFilePaths.size(); i++) {
			if (((String) allFilePaths.get(i)).matches("Carsharing")) {
				return targetDir + "\\" + subDirNames[0] + "\\" + curDate + " " + allFilePaths.get(i);
			}
		}
		return "";
	}

	private List getAllFilePaths() {
		return allFilePaths;
	}
}
