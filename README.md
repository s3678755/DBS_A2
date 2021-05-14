# DBS_A2
Deadline 23/5

----------------------------------

mongoimport --db DBS_A2 --collection data --file ./output-mongo.json --jsonArray

db.data.createIndex( { Sensor_ID: 1 } )

db.data.getIndexes()

java dbqueryA1 3411-01 4096

java -Xmx6g dbqueryA2 3411-01 4096

----------------------------------

java org.apache.derby.tools.ij

connect 'jdbc:derby:DBS_A2_Derby;create=true';

connect 'jdbc:derby:DBS_A2_Derby';

CALL SYSCS_UTIL.SYSCS_IMPORT_TABLE(null,'SENSOR','output-derby-sensor.csv',',',null,null,0);

CALL SYSCS_UTIL.SYSCS_IMPORT_TABLE(null,'DATETIME','output-derby-datetime.csv',',',null,null,0);

create table sensor(Sensor_ID int,Sensor_Name varchar(40));

create table datetime(Date_Time varchar(40), Date_Year int, Month varchar(20), MDate int, Day varchar(10), Time int, Sensor_ID int, Hourly_Counts int);

CREATE INDEX Sensor_Index on SENSOR (Sensor_ID ASC);

CREATE INDEX DateTime_Index on DATETIME (Sensor_ID ASC);

SHOW INDEXES FROM SENSOR;

SHOW INDEXES FROM DATETIME;

DROP INDEX Sensor_Index;

===============================================

.explain("executionStats")

db.data.find({"Details": {$elemMatch: {"Hourly_Counts": {$gte: 300}, "Mdate": 29, "Month": "March", "Year": 2011, "Time": 17}}}, {_id: 0, Sensor_Name: 1}).pretty().explain("executionStats")

// db.data.find({"Details.Hourly_Counts": {$gte: 300}, "Details.Mdate": 29, "Details.Month": "March", "Details.Year": 2011, "Details.Time": 17}, {_id: 0, Sensor_Name: 1}).pretty().explain("executionStats")

db.data.find({"Sensor_Name": "Alfred Place", "Details": {$elemMatch: {"Hourly_Counts": {$gte: 300}}}}, {_id: 0, "Details.Date_Time": 1}).pretty().explain("executionStats")

db.data.find({"Sensor_ID": 34, "Details": {$elemMatch: {"Mdate": 1, "Month": "November", "Year": 2019, "Time": 17}}}, {_id: 0, "Details.Hourly_Counts": 1}).pretty().explain("executionStats")

db.data.find({"Sensor_ID": 34, "Details": {$elemMatch: {"Hourly_Counts": {$gte: 300}}}}, {_id: 0, "Details.Date_Time": 1}).pretty().explain("executionStats")

===============================================

java org.apache.derby.tools.ij

connect 'jdbc:derby:DBS_A2_Derby;create=true';

connect 'jdbc:derby:DBS_A2_Derby';

connect 'jdbc:derby:DBS_A1_Derby';

MaximumDisplayWidth 5000;
CALL SYSCS_UTIL.SYSCS_SET_RUNTIMESTATISTICS(1);
CALL SYSCS_UTIL.SYSCS_SET_STATISTICS_TIMING(1);

VALUES SYSCS_UTIL.SYSCS_GET_RUNTIMESTATISTICS();

CALL SYSCS_UTIL.SYSCS_SET_RUNTIMESTATISTICS(0);

select sensor.Sensor_Name from sensor where sensor.Sensor_ID in ( SELECT datetime.Sensor_ID FROM datetime WHERE datetime.Hourly_Counts > 300 AND datetime.Mdate = 29 AND datetime.Date_Year = 2011 AND datetime.Month = 'March' AND datetime.Time = 17);

select datetime.Date_Time from datetime where datetime.Hourly_Counts >= 300 and datetime.Sensor_ID in (SELECT sensor.Sensor_ID FROM sensor WHERE sensor.Sensor_Name = 'Alfred Place');

select datetime.Hourly_Counts from datetime where datetime.Sensor_ID = 34 and datetime.Mdate = 1 and datetime.Date_Year = 2019 and datetime.Month = 'November' and datetime.Time = 17;

select datetime.Date_Time from datetime where datetime.Sensor_ID = 34 and datetime.Hourly_Counts >= 300;
