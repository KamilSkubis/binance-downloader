# Status
Development. Not working.

# Project goal
Download all data with "USDT" (excluding also tickers containing UP or DOWN) from binance and save it to database.

# Requirements
There are two main requirements:
- app is using MySQL by default so if you want to change database please edit src/resources/hibernate.cfg.xml. Please create schema and new database user if you need to.  
- settings.properties file is required to run this app. Example file is provided in root folder. Put this file in folder where is jar file located or /target folder if running from IDE.

# Usage 
Everytime app is running it will check for data already saved in database and download only missing data entries.






  






