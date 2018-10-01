select * from fandeli_cumrule where 

--truncate table fandeli_abtcumvar

--truncate table fandeli_abtcum 

select * from fandeli_abtcum where sku ='NL012' and idcum = 3

select * from fandeli_abtcumvar where sku ='NL012' and idcum = 1

select * from fandeli_abtcumvar 

select * from fandeli_augmented

--Group Generic
select aa.sku, abs(1-sum(forecast)/sum(aa.realvalue)) errorsemanal
from (
select a.sku, a.idcum ,(b.cdl0 * a.vcdltarget)as realvalue, (b.cdl0 * a.vcdlforecast) as forecast, abs(1-(b.cdl0 * a.vcdlforecast)/(b.cdl0 * a.vcdltarget))*100 as errorporcentual
from fandeli_abtcumvar a inner join fandeli_abtcum b on (a.sku=b.sku and a.xvalue=b.xvalue and a.idcum=b.idcum)
where a.idcum = 2
 )aa
 group by aa.sku
 order by avg(aa.errorporcentual)  desc
--

--Error acumulado porcental
select aaa.sku,aaa.errsemporacum,bbb.errrmenporacum, ccc.errtrimporacum
from
	(
	select aa.sku,abs(1-sum(forecast)/sum(aa.realvalue)) errsemporacum
	from (
		select a.sku, a.idcum ,(b.cdl0 * a.vcdltarget)as realvalue, (b.cdl0 * a.vcdlforecast) as forecast, abs(1-(b.cdl0 * a.vcdlforecast)/(b.cdl0 * a.vcdltarget))*100 as errorporcentual
		from fandeli_abtcumvar a inner join fandeli_abtcum b on (a.sku=b.sku and a.xvalue=b.xvalue and a.idcum=b.idcum)
		where a.idcum = 2
		 )aa
	 group by aa.sku
	 )aaa inner join 
	(
	select aa.sku,abs(1-sum(forecast)/sum(aa.realvalue)) errrmenporacum
	from (
		select a.sku, a.idcum ,(b.cdl0 * a.vcdltarget)as realvalue, (b.cdl0 * a.vcdlforecast) as forecast, abs(1-(b.cdl0 * a.vcdlforecast)/(b.cdl0 * a.vcdltarget))*100 as errorporcentual
		from fandeli_abtcumvar a inner join fandeli_abtcum b on (a.sku=b.sku and a.xvalue=b.xvalue and a.idcum=b.idcum)
		where a.idcum = 1
		 )aa
	 group by aa.sku
	 )bbb on (aaa.sku = bbb.sku) inner join
	 (
	select aa.sku,abs(1-sum(forecast)/sum(aa.realvalue)) errtrimporacum
	from (
		select a.sku, a.idcum ,(b.cdl0 * a.vcdltarget)as realvalue, (b.cdl0 * a.vcdlforecast) as forecast, abs(1-(b.cdl0 * a.vcdlforecast)/(b.cdl0 * a.vcdltarget))*100 as errorporcentual
		from fandeli_abtcumvar a inner join fandeli_abtcum b on (a.sku=b.sku and a.xvalue=b.xvalue and a.idcum=b.idcum)
		where a.idcum = 3
		 )aa
	 group by aa.sku
	 )ccc on (ccc.sku = bbb.sku)
 order by ccc.errtrimporacum





--Promedio de errores acumulado porcental
select aaa.sku,aaa.erravgsemporacum,bbb.erravgmenporacum, ccc.erravgtrimporacum
from
	(
	select aa.sku,avg(errorporcentual) erravgsemporacum
	from (
		select a.sku, a.idcum ,(b.cdl0 * a.vcdltarget)as realvalue, (b.cdl0 * a.vcdlforecast) as forecast, abs(1-(b.cdl0 * a.vcdlforecast)/(b.cdl0 * a.vcdltarget)) as errorporcentual
		from fandeli_abtcumvar a inner join fandeli_abtcum b on (a.sku=b.sku and a.xvalue=b.xvalue and a.idcum=b.idcum)
		where a.idcum = 2
		 )aa
	 group by aa.sku
	 )aaa inner join 
	(
	select aa.sku,avg(errorporcentual) erravgmenporacum
	from (
		select a.sku, a.idcum ,(b.cdl0 * a.vcdltarget)as realvalue, (b.cdl0 * a.vcdlforecast) as forecast, abs(1-(b.cdl0 * a.vcdlforecast)/(b.cdl0 * a.vcdltarget)) as errorporcentual
		from fandeli_abtcumvar a inner join fandeli_abtcum b on (a.sku=b.sku and a.xvalue=b.xvalue and a.idcum=b.idcum)
		where a.idcum = 1
		 )aa
	 group by aa.sku
	 )bbb on (aaa.sku = bbb.sku) inner join
	 (
	select aa.sku,avg(errorporcentual) erravgtrimporacum
	from (
		select a.sku, a.idcum ,(b.cdl0 * a.vcdltarget)as realvalue, (b.cdl0 * a.vcdlforecast) as forecast, abs(1-(b.cdl0 * a.vcdlforecast)/(b.cdl0 * a.vcdltarget)) as errorporcentual
		from fandeli_abtcumvar a inner join fandeli_abtcum b on (a.sku=b.sku and a.xvalue=b.xvalue and a.idcum=b.idcum)
		where a.idcum = 3
		 )aa
	 group by aa.sku
	 )ccc on (ccc.sku = bbb.sku)
 order by ccc.erravgtrimporacum

--Datos
select 'trimestral' tipopronostico,c.sku, d.year, d.month, c.xvalue as indice, e.demand doriginal,c.vcdltarget target, c.vcdlforecast forecast   
from 	fandeli_forecast a inner join
	fandeli_cumrule b on (a.forecastid = b.forecastid) inner join
	fandeli_abtcumvar c on (b.idcum = c.idcum) inner join
	fandeli_augmented d on (c.xvalue = d.xvalue+a.wahead and c.sku = d.sku ) inner join
	aux_fandeli e on (e.sku = d.sku and e.year=d.year and e.month=d.month)
where c.idcum = 2 and c.sku = 'PF011'
order by c.sku,c.xvalue

--Datos Reconstrucción
select case when c.idcum =3 then 'trimestral'
            when c.idcum =1 then 'mensual'
            when c.idcum = 2 then 'semanal' end as tipopronostico,c.sku, d.year, d.month, 
	case when (c.xvalue+1)%4 = 0 then 4
	     else (c.xvalue+1)%4 end as semana ,c.xvalue currentX,d.xvalue targetX, c.cdl0 cumValue, 
	e.demand doriginal,f.vcdltarget target, f.vcdlforecast forecast, g.theoffset,
	case when
	round(((c.cdl0*f.vcdltarget)-c.cdl0)-(a.wahead*g.theoffset)) <0 then 0
	else round(((c.cdl0*f.vcdltarget)-c.cdl0)-(a.wahead*g.theoffset)) end as rebuildOriginal,
	case when
	round(((c.cdl0*f.vcdlforecast)-c.cdl0)-(a.wahead*g.theoffset)) <0 then 0
	else round(((c.cdl0*f.vcdlforecast)-c.cdl0)-(a.wahead*g.theoffset)) end as rebuildForecast
from 	fandeli_forecast a inner join
	fandeli_cumrule b on (a.forecastid = b.forecastid) inner join
	fandeli_abtcumvar f on (b.idcum = f.idcum) inner join
	fandeli_abtcum c on (f.sku=c.sku and f.xvalue =c.xvalue and f.idcum = c.idcum) inner join
	fandeli_augmented d on (c.xvalue+a.wahead = d.xvalue and c.sku = d.sku ) inner join
	aux_fandeli e on (e.sku = d.sku and e.year=d.year and e.month=d.month) inner join
	fandeli_offset g on (e.sku = g.sku)
where c.idcum = 3 AND C.sku = 'NL012'
order by c.sku,c.xvalue


--Datos Reconstrucción Forecast
select case when c.idcum =3 then 'trimestral'
            when c.idcum =1 then 'mensual'
            when c.idcum = 2 then 'semanal' end as tipopronostico,c.sku, 
            round((floor(c.xvalue+a.wahead)/48)+2014) as year, 
            round(((c.xvalue+a.wahead)%48)/4)+1 as month, 
	case when (c.xvalue+a.wahead+1)%4 = 0 then 4
	     else (c.xvalue+a.wahead+1)%4 end as semana ,c.xvalue currentX,c.xvalue+a.wahead targetX, c.cdl0 cumValue, 
	NULL target, f.vcdlforecast forecast, g.theoffset,
	NULL as rebuildOriginal,
	case when
	round(((c.cdl0*f.vcdlforecast)-c.cdl0)-(a.wahead*g.theoffset)) <0 then 0
	else round(((c.cdl0*f.vcdlforecast)-c.cdl0)-(a.wahead*g.theoffset)) end as rebuildForecast
from 	fandeli_forecast a inner join
	fandeli_cumrule b on (a.forecastid = b.forecastid) inner join
	fandeli_abtcumvarfc f on (b.idcum = f.idcum) inner join
	fandeli_abtcumfc c on (f.sku=c.sku and f.xvalue =c.xvalue and f.idcum = c.idcum) inner join	
	fandeli_offset g on (c.sku = g.sku)
	
where c.idcum = 3 AND c.sku = 'NL012'
order by c.sku,c.xvalue
--
--
-- Unión de Resultados
--
Select tipopronostico,sku,year,month,semana,currentx,targetx,cumvalue,target,forecast,theoffset,rebuildoriginal,rebuildforecast 
FROM
(
--Datos Reconstrucción Originales y Forecast
select case when c.idcum =3 then 'trimestral'
            when c.idcum =1 then 'mensual'
            when c.idcum = 2 then 'semanal' end as tipopronostico,c.sku, d.year, d.month, 
	case when (c.xvalue+1)%4 = 0 then 4
	     else (c.xvalue+1)%4 end as semana ,c.xvalue currentX,d.xvalue targetX, c.cdl0 cumValue, 
	e.demand doriginal,f.vcdltarget target, f.vcdlforecast forecast, g.theoffset,
	case when
	round(((c.cdl0*f.vcdltarget)-c.cdl0)-(a.wahead*g.theoffset)) <0 then 0
	else round(((c.cdl0*f.vcdltarget)-c.cdl0)-(a.wahead*g.theoffset)) end as rebuildOriginal,
	case when
	round(((c.cdl0*f.vcdlforecast)-c.cdl0)-(a.wahead*g.theoffset)) <0 then 0
	else round(((c.cdl0*f.vcdlforecast)-c.cdl0)-(a.wahead*g.theoffset)) end as rebuildForecast
from 	fandeli_forecast a inner join
	fandeli_cumrule b on (a.forecastid = b.forecastid) inner join
	fandeli_abtcumvar f on (b.idcum = f.idcum) inner join
	fandeli_abtcum c on (f.sku=c.sku and f.xvalue =c.xvalue and f.idcum = c.idcum) inner join
	fandeli_augmented d on (c.xvalue+a.wahead = d.xvalue and c.sku = d.sku ) inner join
	aux_fandeli e on (e.sku = d.sku and e.year=d.year and e.month=d.month) inner join
	fandeli_offset g on (e.sku = g.sku)
	) aa
UNION
Select tipopronostico,sku,year,month,semana,currentx,targetx,cumvalue,target,forecast,theoffset,rebuildoriginal,rebuildforecast 
from
--Datos Reconstrucción Forecast
(select case when c.idcum =3 then 'trimestral'
            when c.idcum =1 then 'mensual'
            when c.idcum = 2 then 'semanal' end as tipopronostico,c.sku, 
            round((floor(c.xvalue+a.wahead)/48)+2014) as year, 
            round(((c.xvalue+a.wahead)%48)/4)+1 as month, 
	case when (c.xvalue+a.wahead+select b.*,a.demand from aux_fandeli a inner join 
(
select c.sku,c.year,c.month,sum(c.demand) as demand
from fandeli_augmente
1)%4 = 0 then 4
	     else (c.xvalue+a.wahead+1)%4 end as semana ,c.xvalue currentX,c.xvalue+a.wahead targetX, c.cdl0 cumValue, 
	NULL target, f.vcdlforecast forecast, g.theoffset,
	NULL as rebuildOriginal,
	case when
	round(((c.cdl0*f.vcdlforecast)-c.cdl0)-(a.wahead*g.theoffset)) <0 then 0
	else round(((c.cdl0*f.vcdlforecast)-c.cdl0)-(a.wahead*g.theoffset)) end as rebuildForecast
from 	fandeli_forecast a inner join
	fandeli_cumrule b on (a.forecastid = b.forecastid) inner join
	fandeli_abtcumvarfc f on (b.idcum = f.idcum) inner join
	fandeli_abtcumfc c on (f.sku=c.sku and f.xvalue =c.xvalue and f.idcum = c.idcum) inner join	
	fandeli_offset g on (c.sku = g.sku)
) bb

	
where tipopronostico = 'trimestral' AND sku = 'NL012'
order by sku,targetx











-- Fix the negative problems with the offset
--Should add this code when generating the synthetic time series
	--Clean the table
truncate table fandeli_offset;
	--Insert the offsets
INSERT INTO fandeli_offset (sku,theoffset)
select sku, CASE   
          WHEN MIN(demand) < 0 THEN MIN(demand)*-1   
          ELSE 0 END as theoffset
from fandeli_augmented
where idcum = 0
group by sku
order by sku
	--Add the offset to the demand	-
with t as (
	select a.sku as sku2, a.xvalue as xvalue2, a.demand + b.theoffset as newdemand
	from fandeli_augmented as a inner join
	     fandeli_offset as b on (a.sku = b.sku)
	where a.idcum=0 
)
update fandeli_augmented
set demand = t.newdemand 
from t
where sku = t.sku2 and xvalue = t.xvalue2
----

select * from fandeli_offset

truncate fandeli_abtcum 
truncate fandeli_abtcumvar 

select * from fandeli_abtcum
select * from fandeli_abtcumvar where

select * from fandeli_augmented where sku = 'NL001' order by xvalue

select * from fandeli_cumrule 









--Datos Reconstrucción Originales y Forecast
select * from
(
Select tipopronostico,sku,year,month,semana,currentx,targetx,cumvalue,target,forecast,theoffset,rebuildoriginal,rebuildforecast 
FROM
(
--Datos Reconstrucción Originales
select case when c.idcum =3 then 'trimestral'
            when c.idcum =1 then 'mensual'
            when c.idcum = 2 then 'semanal' end as tipopronostico,c.sku, d.year, d.month, 
	case when (c.xvalue+1)%4 = 0 then 4
	     else (c.xvalue+1)%4 end as semana ,c.xvalue currentX,d.xvalue targetX, c.cdl0 cumValue, 
	e.demand doriginal,f.vcdltarget target, f.vcdlforecast forecast, g.theoffset,
	case when
	round(((c.cdl0*f.vcdltarget)-c.cdl0)-(a.wahead*g.theoffset)) <0 then 0
	else round(((c.cdl0*f.vcdltarget)-c.cdl0)-(a.wahead*g.theoffset)) end as rebuildOriginal,
	case when
	round(((c.cdl0*f.vcdlforecast)-c.cdl0)-(a.wahead*g.theoffset)) <0 then 0
	else round(((c.cdl0*f.vcdlforecast)-c.cdl0)-(a.wahead*g.theoffset)) end as rebuildForecast
from 	fandeli_forecast a inner join
	fandeli_cumrule b on (a.forecastid = b.forecastid) inner join
	fandeli_abtcumvar f on (b.idcum = f.idcum) inner join
	fandeli_abtcum c on (f.sku=c.sku and f.xvalue =c.xvalue and f.idcum = c.idcum) inner join
	fandeli_augmented d on (c.xvalue+a.wahead = d.xvalue and c.sku = d.sku ) inner join
	aux_fandeli e on (e.sku = d.sku and e.year=d.year and e.month=d.month) inner join
	fandeli_offset g on (e.sku = g.sku)
	) aa
UNION
Select tipopronostico,sku,year,month,semana,currentx,targetx,cumvalue,target,forecast,theoffset,rebuildoriginal,rebuildforecast 
from
--Datos Reconstrucción Forecast
(select case when c.idcum =3 then 'trimestral'
            when c.idcum =1 then 'mensual'
            when c.idcum = 2 then 'semanal' end as tipopronostico,c.sku, 
            round((floor(c.xvalue+a.wahead)/48)+2014) as year, 
            round(((c.xvalue+a.wahead)%48)/4)+1 as month, 
	case when (c.xvalue+a.wahead+1)%4 = 0 then 4
	     else (c.xvalue+a.wahead+1)%4 end as semana ,c.xvalue currentX,c.xvalue+a.wahead targetX, c.cdl0 cumValue, 
	NULL target, f.vcdlforecast forecast, g.theoffset,
	NULL as rebuildOriginal,
	case when
	round(((c.cdl0*f.vcdlforecast)-c.cdl0)-(a.wahead*g.theoffset)) <0 then 0
	else round(((c.cdl0*f.vcdlforecast)-c.cdl0)-(a.wahead*g.theoffset)) end as rebuildForecast
from 	fandeli_forecast a inner join
	fandeli_cumrule b on (a.forecastid = b.forecastid) inner join
	fandeli_abtcumvarfc f on (b.idcum = f.idcum) inner join
	fandeli_abtcumfc c on (f.sku=c.sku and f.xvalue =c.xvalue and f.idcum = c.idcum) inner join	
	fandeli_offset g on (c.sku = g.sku)
) bb
)aaa
where aaa.tipopronostico = 'trimestral' --AND aaa.sku = 'NL012'
order by aaa.sku,aaa.targetx




group by c.sku,c.year,c.month
) b on(a.sku=b.sku and 
a.year=b.year and a.month=b.month)

select c.sku,c.year,c.month,sum(c.demand) as demand
from fandeli_augmented c
group by c.sku,c.year,c.month








