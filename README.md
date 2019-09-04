
# Summary:
Maven project that process a csv file. Write the row with 10 column to an sqlite database name <input-filename>.db and the row with less to a csv file name <input-filename>-bad.csv. The number of total process row, row with 10 column and the number of row with less  is written to a log file

# How to run the program:
    Run this command from the root of the project:

        mvn -e exec:java -Dexec.mainClass=com.mycompany.cvToDb.App -f cv_to_db/pom.xml -Dexec.args="<path to csv file>"

    Note: All data in write to an output folder in the project directory

# Design Choice:

    - A container class for data: Store the data read from the file and a sentinel      to indicate data processing is completed

    - A class to process file: 
        Read data from csv file and add it to two queue based on correctness

    - A class to process good data:
        Connect to a data based and write data that match column cout to it

    - A class to process bad data:
        Write data that do not match column count to another csv file

    - A wrapper class for database connection
        Allow creation and insertion of data into a database


#H1Assumption made base on requirement:
    - Every row in the project will have 10 column
    - The name of the column in the db is not important
    - Name of the database is <input-filename>.db
    - Name of the table in Database is <input-filename>
    - if any column is missing in a row that row is not added to the database, but written to <input-filename>-bad.csv
    - The path to the csv file is valid