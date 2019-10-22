import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
//import java.net.URI;
import java.net.URISyntaxException;
//import java.nio.file.Files;
//import java.nio.file.Paths;
import java.text.DateFormatSymbols;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;


public class Main {

	private String targetDirEnding = "\\Desktop\\Annis Denka\\Ausgaben";
	private String envUserProfile;	
	private String targetDir;
	
	private String[] subDirNames = { "Einkäufe", "Stromzähler" };

	private String[] fileNames_Einkauefe = { ".gesamt = ", " - andere Käufe", " - Haushaltsartikel & Kosmetik",
			" - Kleidung", " - Nahrungsmittel", " - Carsharing"};

	private String[] fileNames_Stromzaehler = { " - Stromzähler = " };

	private String curDate;
	//private String prevDate;
	
	
	//private final String REGEX_CALC_EUR = "[0-9]+,[0-9]{2}";

	// --------------------------------------------------//

	public static void main(String[] args) throws IOException, URISyntaxException {

		Main main = new Main();
		main.curDate = main.getCurrentDate();
		//main.prevDate = main.getPreviousDate();
		
		main.setEnvUserprofile();
		main.setTargetDir();
		
		main.createDir();
		main.createSubDir();
		main.createFiles();
		
		//main.calculateExpanses();
		

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
	
//	private String getPreviousDate() {
//		String tempDate;
//		String curMonth = this.getCurrentMonth();
//		String curYear = this.getCurrentYear();
//		
//		if(curMonth.contentEquals("01")) {			
//			return tempDate = "01_" + (Integer.parseInt(curYear.toString()) -1);			
//		} else {
//			if((Integer.parseInt(curMonth) -1) < 10) {
//				return tempDate = "0" + (Integer.parseInt(curMonth) -1) + "_" + Integer.parseInt(curYear.toString());
//			} else {
//				return tempDate = (Integer.parseInt(curMonth) -1) + "_" + Integer.parseInt(curYear.toString());
//			}
//		}
//	}
	
	private String[] splitDate() {
		return curDate.split("_");
	}


	/**
	 * creates root dir at specified location
	 */
	private void createDir() {
		new File(buildRootDirString()).mkdirs();
	}

	
	private String buildRootDirString() {
		return targetDir + "/" + this.curDate;
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
	
//	private String[] getPrevMonthDirNames() {
//		File file = new File(buildPrevMonthFilesDir());
//		File[] filesList = file.listFiles();
//		
//		String files[] = new String[filesList.length];
//		
//		for(int i = 0; i < filesList.length; i++) {
//			files[i] = filesList[i].getName();
//		}
//		
//		return files;
//	}

	/**
	 * creates files in dir Einkäufe
	 * 
	 * @throws IOException
	 */
	private void createFiles() throws IOException  {
		String[] dirNames = getDirNames();
		for (String ds : dirNames) {
			if (ds.contentEquals(subDirNames[0])) {
				createTargetFile(subDirNames[0]);
			}
			if (ds.contentEquals(subDirNames[1])) {
				createTargetFile(subDirNames[1]);
			}
		}
	}
	
	private void createTargetFile(String pathNameForFile) throws IOException {		
		String content = "";		
		if(pathNameForFile == subDirNames[0]) {			
			content = defaultContentOfFile();			
			for (String fs : fileNames_Einkauefe) {
				if (fs.startsWith(".")) {
					writeToFile(fs, pathNameForFile, content);
				} else {
					writeToFile(curDate + " " + fs, pathNameForFile, content);
				}
			}
		}
		if(pathNameForFile == subDirNames[1]) {			
			content = defaultContentOfFile_Strom();			
			for (String fs : fileNames_Stromzaehler) {
				if (fs.startsWith(".")) {
					writeToFile(curDate + " " + fs, pathNameForFile, content);
				} else {
					writeToFile(curDate + " " + fs, pathNameForFile, content);
				}
			}
		}
	}
	
	private void writeToFile(String fileName, String pathNameForFile, String content) {
		Writer writer = null;
		try {
		    writer = new BufferedWriter(new OutputStreamWriter(
		    		new FileOutputStream(buildRootDirString() + "/" + pathNameForFile + "/"+ fileName + ".txt"), "utf-8"));
		    writer.write(content);
		} catch (IOException ex) {
		} finally {
			try {
				writer.close();
			} catch (Exception ex) {/*ignore*/}	
		}
	}
	
	private String defaultContentOfFile() {
		String fileContent = convertIntToMonth(splitDate()[0]) + " " + splitDate()[1] + System.lineSeparator();
		fileContent += "-------------------------------" + System.lineSeparator();
		fileContent += "					  " + System.lineSeparator();
		fileContent += "===============================";		
		return fileContent;
	}
	
	private String defaultContentOfFile_Strom() {
		String fileContent = convertIntToMonth(splitDate()[0]) + " " + splitDate()[1] + System.lineSeparator();
		fileContent += "Beginn des Monats:" + System.lineSeparator();
		fileContent += "Ende des Monats:" + System.lineSeparator();
		fileContent += "Verbrauch: kwh";
	
		return fileContent;
	}
	
	private String convertIntToMonth(String numberOfMonth) {
		DateFormatSymbols dfs = new DateFormatSymbols();
	    String[] months = dfs.getMonths();	    
	    String convertedMonth = months[Integer.parseInt(numberOfMonth)-1];	    
		return convertedMonth;
	}

//	private void calculateExpanses() throws IOException, URISyntaxException {
//		List<List<String>> expansesList = new ArrayList<List<String>>();
//		String[] fileList = this.getPrevMonthDirNames();
//		float sum = 0;
//		float[] sumOfFile;
//		float sumTotal = 0;
//		
//		for(String s : fileList) {
//			 expansesList.add(readExpansesFromFile(s));			 
//		}
//		
//		sumOfFile = new float[expansesList.size()];
//
//		List<Float> expansesOfType = new ArrayList<Float>();
//		
//		for(int i = 0; i < expansesList.size(); i++) {
//			for(int j = 0; j < expansesList.get(i).size(); j++) {
//				expansesOfType.add(Float.parseFloat(expansesList.get(i).get(j).replaceFirst(",", ".")));
//				sum += Float.parseFloat(expansesList.get(i).get(j).replaceFirst(",", "."));
//			}
//			sumOfFile[i] = sum;
//			sum = 0;
//			//System.out.println(sumOfFile[i]);
//		}
//		sumTotal = calcTotalExpanses(sumOfFile);
//		//System.out.println("Total: " + sumTotal);
//		this.addExpansesToFileNames(sumOfFile, sumTotal, fileList);
//	}
	
//	private float calcTotalExpanses(float[] sumOfFile) {
//		int tempSum = 0;
//		for(Float f :  sumOfFile) {
//			tempSum += f;
//		}
//		return tempSum;
//	}

//	private void addExpansesToFileNames(float[] sums, float total, String[] filelist) {
//		String value = "";
//		File oldFile;
//		File newFile;
//		
//		for(int i = 0; i < filelist.length; i++) {
//			oldFile = new File(buildPrevMonthFilesDir() + "/" + filelist[i]);
//			
//			if(i == 0 && filelist[i].startsWith(".")) {
//				value += (total+"").replace(".", ",") + "€";
//				
//				newFile = new File(buildPrevMonthFilesDir() + "/" + filelist[i] + " " + value + ".txt");
//				oldFile.renameTo(newFile);
//			} else {
//				value += (sums[i]+"").replace(".", "," + "€");
//				newFile = new File(buildPrevMonthFilesDir() + "/" + filelist[i] + " " + value + ".txt");
//				oldFile.renameTo(newFile);
//			}
//			value = "";
//		}
//	}
	
//	private List<String> readExpansesFromFile(String fileName) throws IOException, URISyntaxException {
//		String data = ""; 
//		File f = new File(this.targetDir + "\\" + getPreviousDate() +  "\\" + subDirNames[0] + "\\" + fileName);		
//	    data = new String(Files.readAllBytes(Paths.get(f.toURI()))); 	    
//	    List<String> extractedExpanses = getRegExMatches(data);
//	    
//		return extractedExpanses;
//	}
	
//	private List<String> getRegExMatches(String data) {
//		List<String> regexMatches = new ArrayList<String>();
//		Pattern pattern = Pattern.compile(REGEX_CALC_EUR);
//		Matcher matcher = pattern.matcher(data);
//		int hits = 0;
//		while (matcher.find())
//		{
//		    regexMatches.add(matcher.group(0));
//		    hits++;
//		}
//		return regexMatches;
//	}

//	private String buildPrevMonthFilesDir() {
//		return targetDir + "/" + this.prevDate + "/" + this.subDirNames[0];
//	}
}
