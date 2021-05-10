# DBS_A2
Deadline 23/5

----------------------------------

mongoimport --db DBS_A2 --collection data --file ./output-mongo.json --jsonArray

db.data.createIndex( { Sensor_ID: 1 } )

db.data.getIndexes()

----------------------------------

java org.apache.derby.tools.ij

connect 'jdbc:derby:DBS_A2_Derby;create=true';

connect 'jdbc:derby:DBS_A2_Derby';

CALL SYSCS_UTIL.SYSCS_IMPORT_TABLE(null,'SENSOR','output-derby-sensor.csv',',',null,null,0);

CALL SYSCS_UTIL.SYSCS_IMPORT_TABLE(null,'DATETIME','output-derby-datetime.csv',',',null,null,0);

create table sensor(Sensor_ID int,Sensor_Name varchar(40));

create table datetime(Date_Time varchar(40), Date_Year int, Month varchar(20), MDate int, Day varchar(10), Time int, Sensor_ID int, Hourly_Counts int);

CREATE INDEX Sensor_Index on SENSOR (Sensor_ID ASC);

CREATE INDEX Sensor_Index on DATETIME (Sensor_ID ASC);

SHOW INDEXES FROM SENSOR;

DROP INDEX Sensor_Index;

===============================================

db.data.find({"Details": {$elemMatch: {"Hourly_Counts": {$gte: 300}, "MDate": 29, "Month": March, "Year": 2013, "Time": 17}}}, {_id: 0, Sensor_Name: 1}).pretty()

db.data.find({"Details": {$elemMatch: {"Sensor_Name": "Alfred Place", "Hourly_Counts": {$gte: 300}).pretty()

db.data.find({"Details": {$elemMatch: {"Sensor_ID": 34, "MDate": 29, "Month": March, "Year": 2013, "Time": 17}}}, {_id: 0, Hourly_Counts: 1}).pretty()

db.data.find({"Details": {$elemMatch: {"Sensor_ID": 34, "Hourly_Counts": {$gte: 300}, {_id: 0, Date_Time: 1}).pretty()

===============================================

select sensor.Sensor_Name from sensor where sensor.Sensor_ID in ( SELECT datetime.Sensor_ID FROM datetime WHERE datetime.Hourly_Counts > 300 AND datetime.Mdate = 29 AND datetime.Date_Year = 2013 AND datetime.Month = 'March' AND datetime.Time = 17);

select * from datetime where datetime.Hourly_Counts >= 300 and datetime.Sensor_ID in (SELECT sensor.Sensor_ID FROM sensor WHERE sensor.Sensor_name = "Alfred Place");

select datetime.Hourly_Counts from datetime where datetime.Sensor_ID = 34 and datetime.Mdate = 29 and datetime.Date_Year = 2013 and datetime.Month = 'March' and datetime.Time = 17;

select datetime.Date_Time from datetime where datetime.Sensor_ID = 34 and datetime.Hourly_Counts >= 300;
