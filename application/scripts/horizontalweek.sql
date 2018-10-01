--Datos Reconstrucción Originales y Forecast
select aaaa.tipopronostico,aaaa.sku,aaaa.year,aaaa.month,sum(rebuildoriginal) as demand,sum(aaaa.rebuildforecast) as forecast
from
(
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
where aaa.tipopronostico = 'semanal' --AND aaa.sku = 'NL012'
)aaaa
where aaaa.currentx>54
group by aaaa.tipopronostico, aaaa.sku, aaaa.year, aaaa.month
order by aaaa.sku,aaaa.year,aaaa.month
