@echo off
cd src
javadoc -private -author -version -d ..\doc  -classpath ..\bin *.java
cd ..
pause
@echo on