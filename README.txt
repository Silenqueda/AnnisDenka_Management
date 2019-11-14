AnnisDenka_Management is a private application to manage the expanses in a highly customized and sort of hardcoded way.

### Monthly Setup ###
This project generates an initial setup for the current month, depending on the current system time.
After generating the new setup the system tries to calculate all expanses of the previous month. It sums every single file, sums up their result and writes it to ".gesamt = ".

### MailReceiver ###
This project connects to my personal mail account, navigates to my carsharing expanses, filters and writes them to the .temp directory.
These dates and values are filtered and written to a file in the predefined directory "carsharing - " depending on the date.


### TODO's ###
1) MonthlySetup - initial setup throws error if prevMonthSetup does not exist.
2) MonthlySetup - calculatecarsharing expanses [depending on: MailReceiver 3)]

1) MailReceiver - write mailData_all to temp
2) MailReceiver - filter mailData_all by dateToPay (second date)
3) MailReceiver - write filtered datasets to their dedicated file 
   (dateToPay = 12.10.2019 -> 10.2019.txt)