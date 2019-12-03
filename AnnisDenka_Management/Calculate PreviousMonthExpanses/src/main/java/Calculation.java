import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Use of this class is to calculate the previous month
 * @author DBR *
 */

public class Calculation {
	private String envUserProfile;
	private String targetDirEnding = "\\Desktop\\Annis Denka\\Ausgaben";
	private String prevTargetDir;
	private String prevDate;
	
	private final String REGEX_CALC_EUR = "[0-9]+,[0-9]{2}";
	private String[] subDirNames = { "Ausgaben", "Stromzähler" };

	public Calculation() {
		
	}
	
	private void setEnvUserprofile() {
		this.envUserProfile = System.getProperty("user.home");
	}
	
	private void setPrevTargetDir() {
		this.prevTargetDir = envUserProfile + targetDirEnding + " " + splitPrevDate()[1];
	}
	
	private void setPreviousDate() {
		String tempDate;
		String curMonth = this.getCurrentMonth();
		String curYear = this.getCurrentYear();

		if (curMonth.contentEquals("01")) {
			tempDate = "12_" + (Integer.parseInt(curYear.toString()) - 1);
		} else {
			if ((Integer.parseInt(curMonth) - 1) < 10) {
				tempDate = "0" + (Integer.parseInt(curMonth) - 1) + "_" + Integer.parseInt(curYear.toString());
			} else {
				tempDate = (Integer.parseInt(curMonth) - 1) + "_" + Integer.parseInt(curYear.toString());
			}
		}
		this.prevDate = tempDate;
	}
	
	private String[] getPrevMonthDirNames_Ausgaben() {
		File file = new File(buildPrevMonthFilesDir_Ausgaben());
		File[] filesList = file.listFiles();
		String files[] = new String[filesList.length];
		for (int i = 0; i < filesList.length; i++) {
			files[i] = filesList[i].getName();
		}
		return files;
	}
	
	private String[] getPrevMonthDirNames_Strom() {
		File file = new File(buildPrevMonthFilesDir_Ausgaben());
		File[] filesList = file.listFiles();
		String files[] = new String[filesList.length];
		for (int i = 0; i < filesList.length; i++) {
			files[i] = filesList[i].getName();
		}
		return files;
	}
	
	public String getPreviousDate() {
		return this.prevDate;
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
	
	private String[] splitPrevDate() {
		return prevDate.split("_");
	}
	
	// builds Ausgaben path
	private String buildPrevMonthFilesDir_Ausgaben() {
		if (prevDate.contains("12_")) {
			return prevTargetDir.substring(0, prevTargetDir.length() - 4) + prevDate.substring(prevDate.length() - 4) + "/"
					+ this.prevDate + "/" + this.subDirNames[0];
		} else {
			return prevTargetDir + "/" + this.prevDate + "/" + this.subDirNames[0];
		}
	}
	
	// builds Stromzähler path
	private String buildPrevMonthFilesDir_Strom() {
		if (prevDate.contains("12_")) {
			return prevTargetDir.substring(0, prevTargetDir.length() - 4) + prevDate.substring(prevDate.length() - 4) + "/"
					+ this.prevDate + "/" + this.subDirNames[1];
		} else {
			return prevTargetDir + "/" + this.prevDate + "/" + this.subDirNames[1];
		}
	}
	
	private void calculateExpanses() throws IOException, URISyntaxException {
		List<List<String>> expansesList = new ArrayList<List<String>>();
		String[] fileList = this.getPrevMonthDirNames_Ausgaben();
		float sum = 0;
		float[] sumOfFile;
		float sumTotal = 0;

		for (String s : fileList) {
			expansesList.add(readExpansesFromFile(s));
		}

		sumOfFile = new float[expansesList.size()];

		List<Float> expansesOfType = new ArrayList<Float>();

		for (int i = 0; i < expansesList.size(); i++) {
			for (int j = 0; j < expansesList.get(i).size(); j++) {
				expansesOfType.add(Float.parseFloat(expansesList.get(i).get(j).replaceFirst(",", ".")));
				sum += Float.parseFloat(expansesList.get(i).get(j).replaceFirst(",", "."));
			}
			sumOfFile[i] = sum;
			sum = 0;
		}
		sumTotal = calcTotalExpanses(sumOfFile);
		this.addExpansesToFileNames(sumOfFile, sumTotal, fileList);
	}
	
	private float calcTotalExpanses(float[] sumOfFile) {
		int tempSum = 0;
		for (Float f : sumOfFile) {
			tempSum += f;
		}
		return tempSum;
	}
	
	private void addExpansesToFileNames(float[] sums, float total, String[] filelist) {
		String value = "";
		File oldFile;
		File newFile;

		for (int i = 0; i < filelist.length; i++) {
			oldFile = new File(buildPrevMonthFilesDir_Ausgaben() + "/" + filelist[i]);

			if (i == 0 && filelist[i].startsWith(".")) {
				value += (total + "").replace(",", ".");
				newFile = new File(buildPrevMonthFilesDir_Ausgaben() + "/" + filelist[i] + " " + value + "€.txt");
				File temp = new File(newFile.getAbsoluteFile().toString().replaceAll("[0-9]*.[0-9]*€.txt ", ""));
				oldFile.renameTo(temp);
			} else {
				if (oldFile.getAbsoluteFile().toString().contains("€")) {
					value += (sums[i] + "");
					newFile = new File(buildPrevMonthFilesDir_Ausgaben() + "/" + filelist[i] + " " + value + "€.txt");
					File temp = new File(newFile.getAbsoluteFile().toString().replaceAll("[0-9]*.[0-9]*€.txt ", ""));
					oldFile.renameTo(temp);
				} else {
					value += (sums[i] + "");
					newFile = new File(buildPrevMonthFilesDir_Ausgaben() + "/" + filelist[i] + " " + value + "€.txt");
					oldFile.renameTo(newFile);
				}
			}
			value = "";
		}
	}

	private List<String> readExpansesFromFile(String fileName) throws IOException, URISyntaxException {
		String data = "";
		if (this.getPreviousDate().contains("12_")) {
			File f = new File(this.buildPrevMonthFilesDir_Ausgaben() + "\\" + fileName);
			data = new String(Files.readAllBytes(Paths.get(f.toURI())));
		} else {
			File f = new File(this.prevTargetDir + "\\" + this.getPreviousDate() + "\\" + subDirNames[0] + "\\" + fileName);
			data = new String(Files.readAllBytes(Paths.get(f.toURI())));
		}
		List<String> extractedExpanses = getRegExMatches(data);

		return extractedExpanses;
	}
	
	private List<String> getRegExMatches(String data) {
		List<String> regexMatches = new ArrayList<String>();
		Pattern pattern = Pattern.compile(REGEX_CALC_EUR);
		Matcher matcher = pattern.matcher(data);
		while (matcher.find()) {
			regexMatches.add(matcher.group(0));
		}
		return regexMatches;
	}

	public static void main(String[] args) throws IOException, URISyntaxException {
		
		Calculation cal = new Calculation();
		
		cal.setEnvUserprofile();
		cal.setPreviousDate();
		cal.setPrevTargetDir();
		
		cal.calculateExpanses();
		
		
	}

}
