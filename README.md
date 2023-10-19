# Patient-Data-Analysis
The UC Irvine Machine Learning repository contains many datasets for conducting
computer science research. One of them is the Haberman’s Survival dataset, available
at http://archive.ics.uci.edu/ml/datasets/Haberman’s+Survival. The file “haberman.data”
contains survival data for breast cancer patients in comma-separated values (CSV)
format. The first field is the patient’s age at the time of surgery, the second field is the
year of the surgery, the third field is the number of positive axillary nodes detected, and
the fourth field is the survival status. The survival status is 1 if the patient survived 5
years or longer and 2 if the patient died within 5 years.


This program reads the CSV file and calculates:

    • the average age of patients who survived 5 years or longer
    
    • the average age of patients died within 5 years 

The program finds out:

    • which year has the highest patient survival ratio (5 years or longer)
    
    • which year has the lowest patient survival ratio (5 years or longer)

The program also computes: 

    • the average number of positive axillary nodes detected for patients survived 5
    years or longer
    
    • the average number of axillary nodes detected for patients died within 5 years.

The program saves ALL computed averages (in a readable format) in a
text file specified by the user.

The program handles similar input data files (that have a record per line
and each record has 4 entries, separated by a comma) of any size.

A sample run of the program:

    Please enter the name of a dataset in CSV format:
    
    Haberman.data
    
    Please enter the name of the output file that saves the result from the program:
    
    Averages
    
    Do you want to run the program again (y for yes and n for no):
    
    n
