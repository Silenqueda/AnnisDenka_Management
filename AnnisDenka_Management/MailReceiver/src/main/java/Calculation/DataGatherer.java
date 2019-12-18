package Calculation;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import DataObjects.*;


public class DataGatherer {
	
	private List listOfSeparatedByMonths;	//holds lists seperated by MonthToPay_Date from 'wholeDownloadedDataSet'
	private List<Expanse> wholeDownloadedDataSet;    //holds data from file mailData_all
	
	private String home = System.getProperty("user.home");
	private String workspace = System.getProperty("user.dir");
	private String mailDataPath = "\\Desktop\\Annis Denka\\.temp\\mailData_all.txt";
	
	
	public DataGatherer() {
		this.listOfSeparatedByMonths = new ArrayList<List>();
	}
	
	// reads in mailData_all.txt
	public void readFile() {
		BufferedReader readFile;
		
		if(wholeDownloadedDataSet == null) {
			this.wholeDownloadedDataSet = new ArrayList<Expanse>();
		}
		
		try {
			readFile = new BufferedReader(new FileReader(home + mailDataPath));
			String line = readFile.readLine();
			while(line != null) {
				wholeDownloadedDataSet.add(buildExpanseFromFile(line));
				line = readFile.readLine();
			}
			readFile.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	//separates Line from file into expanse variables
	private Expanse buildExpanseFromFile(String line) {
		String date_driven_t = "", date_payment_t = "", amountToPay = "";		
		String[] separatedData = line.split("\t");
		
		date_driven_t  = separatedData[0];
		date_payment_t = separatedData[1];
		amountToPay    = separatedData[2];
		
		return new Expanse(date_driven_t, date_payment_t, amountToPay);
	}
	
	//TODO: separate content of wholeDownloadedDataSet by Months
	
	
	public void printWholeDownloadedDataSet() {
		for(Expanse e : wholeDownloadedDataSet) {
			System.out.println(e.toString());
		}
	}
	

}
