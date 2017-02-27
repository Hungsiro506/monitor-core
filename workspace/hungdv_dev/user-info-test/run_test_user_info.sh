#!/bin/bash
C_PATH_ROOT = $1
T_PATH_ROOT = $2
echo "Remove old different-record between C and T"
rm diff_vectorHouly_2
rm diff_vectorDaily_2
rm diff_vectorDays_2
rm diff_vectorApp_2
echo " Create diffCount - counting number of diff-record "
touch diffCount.txt
echo " Finding incommon records between T and C "
grep -Fxvf $C_PATH_ROOT/vectorDaily.csv $T_PATH_ROOT/vectorDaily.csv >> diff_vectorDaily_2
wc -l ./diff_vectorDaily_2 >> diffCount.txt

grep -Fxvf $C_PATH_ROOT/vectorApp.csv $T_PATH_ROOT/vectorApp.csv >> diff_vectorApp_2
wc -l ./diff_vectorApp_2 >> diffCount.txt

grep -Fxvf $C_PATH_ROOT/vectorDays.csv $T_PATH_ROOT/vectorDays.csv >> diff_vectorDays_2
wc -l ./diff_vectorDays_2 >> diffCount.txt
echo " Calcualting diff - sum "
java -jar UserInfoTest2.jar ./diff_vectorDaily_2 $C_PATH_ROOT/vectorDaily.csv $T_PATH_ROOT/vectorDaily.csv ./compareDaily_2.txt 

java -jar UserInfoTest2.jar ./diff_vectorApp_2 $C_PATH_ROOT/vectorApp.csv $T_PATH_ROOT/vectorApp.csv ./compareApp_2.txt

java -jar UserInfoTest2.jar ./diff_vectorDays_2 $C_PATH_ROOT/vectorDays.csv $T_PATH_ROOT/vectorDays.csv ./compareDays_2.txt


