#We create the table of the demand
drop table if exists retailforecasting.skudemandtbl;
create external table skudemandtbl
(sku string,
 yearmonth string,
 demand string)
 row format delimited
 fields terminated by ','
 stored as textfile
 location '/user/hduser/skudemandtbl/';

#Now we create the table for the accumulative demand
drop table if exists retailforecasting.skudemandcumtbl;
create external table retailforecasting.skudemandcumtbl
(sku string,
 yearmonth string,
 demand string,
 cumdemand string)
 row format delimited
 fields terminated by ','
 stored as textfile
 location '/user/hduser/skudemandcumtbl/';

