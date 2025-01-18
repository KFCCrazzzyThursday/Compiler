@echo off
cd src
javac -Xlint:deprecation -d ../bin/ -classpath ../bin  *.java
cd ..
pause
@echo on
