@echo off
cd src
javac -d ..\bin -classpath ..\bin parser\*.java scanner\*.java DFA\*.java token\*.java token\Function\*.java token\Value\*.java token\Operator\*.java token\Symbol\*.java arithmetic\*.java
cd ..
pause
@echo on
