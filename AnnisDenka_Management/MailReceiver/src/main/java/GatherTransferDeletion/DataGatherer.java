package GatherTransferDeletion;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import DataObjects.*;

public class DataGatherer {

	private List listOfSeparatedByMonths; // holds lists seperated by MonthToPay_Date from 'wholeDownloadedDataSet'
	private List<Expanse> wholeDownloadedDataSet; // holds data from file mailData_all

	private String home = System.getProperty("user.home");
	private String workspace = System.getProperty("user.dir");
	private String mailDataPath = "\\Desktop\\Annis Denka\\.temp"; // holds all DataSets
	private String mailDataFileName = "\\mailData_all.txt"; // fileName of all datasets

	public DataGatherer() {
		this.listOfSeparatedByMonths = new ArrayList<List>();
	}

	// reads in mailData_all.txt
	public void readFile() {
		BufferedReader readFile;

		if (wholeDownloadedDataSet == null) {
			this.wholeDownloadedDataSet = new ArrayList<Expanse>();
		}

		try {
			readFile = new BufferedReader(new FileReader(home + mailDataPath + mailDataFileName));
			String line = readFile.readLine();
			System.out.println("*** DataGatherer -> Read file and build Expanse Objects ***");
			while (line != null) {
				if (line.length() > 0) { // this gets rid of blank lines in file
					wholeDownloadedDataSet.add(buildExpanseFromFile(line));
				}
				line = readFile.readLine(); // reads next line
			}
			readFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// separates Line from file into expanse variables
	private Expanse buildExpanseFromFile(String line) {
		String date_driven_t = "", date_payment_t = "", amountToPay = "";
		String[] separatedData = splitData(line);

		date_driven_t = separatedData[0];
		date_payment_t = separatedData[1];
		amountToPay = separatedData[2];

		return new Expanse(date_driven_t, date_payment_t, amountToPay);
	}

	private String[] splitData(String line) {
		String[] splittedString = null;

		String deleteSpacesFromLine = line.trim();

		if (line.contains("\t")) {
			splittedString = deleteSpacesFromLine.split("\t");
		} else if (line.contains(";")) {
			splittedString = deleteSpacesFromLine.split(";");
		} else {
			System.out.println("*** DataGatherer -> Wrong value separator. '\t' or ';' are allowed. ***");
			System.out.println("*** DataGatherer -> Exit ***");
			System.exit(0);
		}
		return splittedString;
	}

	// TODO: separate content of wholeDownloadedDataSet by Months
	public List separateDataSetByDateToPay() throws ParseException, IOException {
		List monthList = null;
		List month = null;

		String currentMonth = "";
		Expanse temp = null;

		for (int i = 0; i < wholeDownloadedDataSet.size(); i++) {
			// System.out.println("*** DataGatherer -> " +
			// wholeDownloadedDataSet.get(i).getDate_payment());
			temp = wholeDownloadedDataSet.get(i);

			String dateToPay = temp.getDate_payment();
			Date monthAndYear = new SimpleDateFormat("dd.MM.yyyy").parse(dateToPay);
			Calendar calender = Calendar.getInstance();
			calender.setTime(monthAndYear);
			// System.out.println("*** DataGatherer -> " + (calender.get(Calendar.MONTH)+1)
			// + "." + calender.get(Calendar.YEAR));

			if ((calender.get(Calendar.MONTH) + 1) < 10) { // appends leading 0 if month lesser then 10
				currentMonth = "0" + (calender.get(Calendar.MONTH) + 1) + "." + calender.get(Calendar.YEAR);
			} else {
				currentMonth = (calender.get(Calendar.MONTH) + 1) + "." + calender.get(Calendar.YEAR);
			}
			System.out.println("*** DataGatherer -> Create file");
			createFileIfNotExistent(currentMonth);
		}
		System.out.println("*** DataGatherer -> Write to file");
		writeToFittingFile();

		return null;
	}

	// creates File for every non existing month in .temp directory
	private void createFileIfNotExistent(String currentMonth) throws IOException {
		File file = new File(home + mailDataPath + "\\" + currentMonth + ".txt");
		if (!file.exists()) {
			file.createNewFile();
			System.out.println("*** DataGatherer -> File created ***");
		} else {
			System.out.println("*** DataGatherer -> File already exists ***");
		}
	}

	//writes matching dateToPay to file with same name as date
	private void writeToFittingFile() throws IOException {
		String[] dirNames = getDirNames();
		String output = "";

		for (int i = 0; i < wholeDownloadedDataSet.size(); i++) {
			Expanse temp =  wholeDownloadedDataSet.get(i);
			for (int j = 0; j < dirNames.length; j++) {
				if (temp.getDate_payment().contains(dirNames[j].replace(".txt", ""))) {
					System.out.println("DataGatherer -> matching file found");
					File file = new File(home + mailDataPath + "\\" + dirNames[j]);
					FileWriter fr = new FileWriter(file, true);
					fr.write(temp.getDate_driven() + "\t" + temp.getDate_payment() + "\t" + temp.getAmountToPay() + System.lineSeparator());
					fr.close();
				}
			}
		}
	}

	//gets all files in dir .temp
	private String[] getDirNames() {
		File file = new File(home + mailDataPath);
		File[] directories = file.listFiles();
		String[] fileNames = new String[directories.length];
		
		for(int i = 0; i < directories.length; i++) {
			fileNames[i] = directories[i].getName();
		}
		return fileNames;
	}

	public void printWholeDownloadedDataSet() {
		for (Expanse e : wholeDownloadedDataSet) {
			System.out.println(e.toString());
		}
	}

}
