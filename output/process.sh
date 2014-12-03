#!/bin/bash
predefine="http://cs.hbg.psu.edu"
first_rec=1
forward_factor=1
while read -r line || [[ -n $line ]]
do
log=$line
ip=${log%%;*}
#    echo $ip
cut_front=${log#*;}
req_time=${cut_front%;*}
#echo $req_time
#    month_name=${req_time#*/}
#    month_name=${month_name%/*}
#    month=1;
#    case $month_name in
#	Jan) month=1
#	    ;;
#	Feb) month=2
#	    ;;
#	Mar) month=3
#	    ;;
#	Apr) month=4
#	    ;;
#	May) month=5
#	    ;;
#	Jun) month=6
#	    ;;
#	Jul) month=7
#	    ;;
#	Aug) month=8
#	    ;;
#	Sep) month=9
#	    ;;
#	Oct) month=10
#	    ;;
#	Nov) month=11
#	    ;;
#	Dec) month=12
#	    ;;
#    esac
#    echo $month

day=${req_time%%/*}
day=${day#[}
#echo $day
hour=${req_time#*:}
hour=${hour%%:*}
#echo $hour
min=${req_time%:*}
min=${min##*:}
#echo $min
sec=${req_time##*:}
sec=${sec%% *}
#echo $sec

if [ $first_rec -eq 1 ]
 then
sleeptime=0
first_rec=0
elif [ $prev_day -ne $day ]
 then
prevtime=$(($((10#$prev_hour))*60+$((10#$prev_min))))
prevtime=$(($((10#$prevtime))*60+$((10#$prev_sec))))
curtime=$((24*60))
curtime=$(($((10#$hour))*60+$((10#$min))+$((10#$curtime))))
curtime=$(($((10#$curtime))*60+$((10#$sec))))
sleeptime=$(($((10#$curtime))-$((10#$prevtime))))
else
prevtime=$(($((10#$prev_hour))*60+$((10#$prev_min))))
prevtime=$(($((10#$prevtime))*60+$((10#$prev_sec))))
curtime=$(($((10#$hour))*60+$((10#$min))))
curtime=$(($((10#$curtime))*60+$((10#$sec))))
sleeptime=$(($((10#$curtime))-$((10#$prevtime))))
fi
echo "wait time is : "$(($sleeptime/$forward_factor))
prev_day=$day
prev_hour=$hour
prev_min=$min
prev_sec=$sec
    
path=${log##*;}
echo "SIZE "$path
done < $1