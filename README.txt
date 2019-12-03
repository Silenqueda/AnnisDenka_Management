AnnisDenka_Management is a private application to manage the expanses in a highly customized and sort of hardcoded way.

// check if all .jars are existing in jars folder

### 01_Monthly Setup ###
This project generates an initial setup for the current month, depending on the current system time.
After generating the new setup 

### 02_MailReceiver ###
This project connects to my personal mail account, navigates to my carsharing expanses, filters and writes them to the .temp directory.
These dates and values are filtered and written to a file in the predefined directory "carsharing - " depending on the dateToPay.

### 03_CalculatePreviousMonthExpanses ###
The system tries to calculate all expanses of the previous month. It sums every single file, sums up their result and writes it to ".gesamt = ".




### TODOs ###
4) MonthlySetup - create dir for Stromzähler fixed expanse            values [4 <-> 5]
5) MonthlySetup - create file with fixed expanse values of Strom [5 <-> 4]

1) MailReceiver - write mailData_all to temp
2) MailReceiver - filter mailData_all by dateToPay (second date)
3) MailReceiver - write filtered datasets to their dedicated file 
   (dateToPay = 12.10.2019 -> 10.2019.txt)

1) Calculator - calculates only values with comma
2) Calculator - calculate carsharing expanses [depending on: MailReceiver 3)]
3) Calculator - calculate Stromzähler expanses
	=> read from file with needed entries (fixed price, price 	   per kwh)


### DONEs ###
1) MonthlySetup - initial setup throws error if prevMonthSetup does not exist.
	=> MonthlySetup's only purpose is to create directory tree
2) MonthlySetup - calculate carsharing expanses [depending on: MailReceiver 3)]
	=> job is handled by Calculator now
3) MonthlySetup - fix renaming of ".gesamt = .txt"

4) MailReceiver - executable Jar points to "\.temp\", eclipse run points to "\temp\"
