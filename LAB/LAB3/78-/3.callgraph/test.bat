@echo off
cd bin
java -classpath ../bin Main ../testcases/*.err
cd ..
pause
@echo on