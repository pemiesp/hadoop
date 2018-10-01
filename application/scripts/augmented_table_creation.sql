-- DROP TABLE public.aux_fandeli;
-- Stores the original observations
CREATE TABLE public.aux_fandeli
(
  sku character varying,
  year integer,
  month integer,
  demand integer
);

-- DROP TABLE aux_fandelicum;
-- Stores the original observations
CREATE TABLE aux_fandelicum
(
  sku character varying,
  idcum integer,
  year integer,
  month integer,
  xvalue integer,  
  cumdemand decimal
);

-- DROP TABLE fandeli_forecast;
-- Describes the type of forecast able
CREATE TABLE fandeli_forecast
(
  forecastid integer,
  wahead integer, -- Indicates the number of weeks ahead the target forecast will be
  weekthreshold integer, -- Indicates the week until which the accumulationexception will be executed
  exceptionweeks integer, -- Indicates the number of weeks that will be accumulated before the original starting week.
  description character varying
);
--Catalog population
INSERT INTO fandeli_forecast(forecastid, wahead, weekthreshold, exceptionweeks, description)    VALUES (0, 0, 4, 4,'Observation') ,  (1, 1, 4, 4,'WeekForecast') , (3, 4, 4, 4,'MonthForecast') , (4, 12, 4, 4,'TrimesterForecast');


-- DROP TABLE fandeli_cumrule;
-- Indicates the rule of how to accumulate
CREATE TABLE fandeli_cumrule
(
  idcum integer,
  description character varying,
  weeks integer, -- Indicates how many weeks will consider the accumulation
  lag integer, -- Indicates how many weeks before the current time will be taken into account  
  forecastid integer -- Links to this table to indicate the scope of the forecast
);
  
INSERT INTO fandeli_cumrule (idcum, description, weeks, lag, forecastid) VALUES (1, 'Month forecast considering this and three weeks before', 24, 3, 3);
INSERT INTO fandeli_cumrule (idcum, description, weeks, lag, forecastid) VALUES (2, 'Week forecast considering this and three weeks before', 24, 3, 1);
INSERT INTO fandeli_cumrule (idcum, description, weeks, lag, forecastid) VALUES (0, 'Means no rule, Indicates original values', 0, 0, 0);
INSERT INTO fandeli_cumrule (idcum, description, weeks, lag, forecastid) VALUES (3, 'Trimestral forecast considering this and three weeks before', 24, 3, 4);

-- DROP TABLE fandeli_augmented;
-- Stores the original, the splines and the predictions
CREATE TABLE fandeli_augmented
(
  sku character varying,
  idcum integer,
  year integer,
  month integer,
  xvalue integer,
  demand decimal,
  forecastid integer,
  spline boolean, --indicates if this value is a spline  
  hasoffset boolean -- indicates if the offse has been calculated
);
CREATE TABLE fandeli_offset
(
  sku character varying,
  theoffset decimal
 );
-- DROP TABLE fandeli_abtcum;
CREATE TABLE fandeli_abtcum
(
  sku character varying, --indicates the id of the item
  xvalue integer,--indicates the id of the observation over time. This x corresponds to cd
  idcum integer, --indicates the rules used to build this abt
  cdl3 decimal, -- The accumulated demand three week before now
  cdl2 decimal, -- The accumulated demand two week before now
  cdl1 decimal, -- The accumulated demand one week before now
  cdl0 decimal, -- Tinthe accumulated demand 
  cdltarget decimal -- The accumulated target to forecast, can be the next month, next trimester, etc. This is related to idcum.
);
-- DROP TABLE fandeli_abtcumvar;
CREATE TABLE fandeli_abtcumvar
(
  sku character varying, --indicates the id of the item
  xvalue integer,--indicates the id of the observation oveir time. This x corresponds to cd
  idcum integer, --indicates the rules used to build this abt
  vcdl32 decimal, -- The accumulated variation demand from the third to the second latest weeks
  vcdl21 decimal, -- The accumulated variation demand from the second to the first latest weeks
  vcdl10 decimal, -- The accumulated variation demand from the fist to the current latest weeks
  vcdltarget decimal, -- The accumulated variation demand from the current to the target weeks
  vcdlforecast decimal -- Data to store the prediction
);
--
--
-- DROP TABLE fandeli_abtcumfc;
CREATE TABLE fandeli_abtcumfc
(
  sku character varying, --indicates the id of the item
  xvalue integer,--indicates the id of the observation over time. This x corresponds to cd
  idcum integer, --indicates the rules used to build this abt
  cdl3 decimal, -- The accumulated demand three week before now
  cdl2 decimal, -- The accumulated demand two week before now
  cdl1 decimal, -- The accumulated demand one week before now
  cdl0 decimal -- Tinthe accumulated demand 
);
-- DROP TABLE fandeli_abtcumvarfc;
CREATE TABLE fandeli_abtcumvarfc
(
  sku character varying, --indicates the id of the item
  xvalue integer,--indicates the id of the observation oveir time. This x corresponds to cd
  idcum integer, --indicates the rules used to build this abt
  vcdl32 decimal, -- The accumulated variation demand from the third to the second latest weeks
  vcdl21 decimal, -- The accumulated variation demand from the second to the first latest weeks
  vcdl10 decimal, -- The accumulated variation demand from the fist to the current latest weeks
  vcdlforecast decimal -- Data to store the prediction
);


