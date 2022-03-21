# Project goal
Console software to download data from Binance. Working can be automated by windows or linux automation tool.

# Milestones
* Can download data by using default parameters for all USDT currencies.
* Can download data by using special parameters (start date, end date).
* Downloaded data can be saved to database - using Hibernate
* System will check if what data is missing and will start from there. This will ensure that system will have always up to date data. This check will be helpful in case automated job fail.
* Downloaded data can be saved to file: csv


# Program algorithm
This program is intended use in daily intervals. In because of that, first thing to check is list of currency pairs.


1. List of currency pairs.
2. Check database for last entry
3. Download data + some more.
4. Put downloaded data to queue and check with data in database.
5. Put missed data to database.