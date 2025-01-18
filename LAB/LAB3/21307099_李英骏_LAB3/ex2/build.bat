@echo off
javac -d bin -classpath ..\bin -encoding UTF-8 src\Symbol.java
cd bin
javac -d . ..\src\OberonScanner.java
javac -d . ..\src\Main.java
cd ..
pause