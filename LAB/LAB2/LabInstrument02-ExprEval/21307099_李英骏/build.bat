@echo off
cd src
javac -d ..\bin -classpath ..\bin parser\*.java parser\scanner\*.java parser\DFA\*.java parser\token\*.java parser\token\Function\*.java parser\token\Value\*.java parser\token\Operator\*.java parser\token\Symbol\*.java parser\arithmetic\*.java
cd ..
pause
@echo on
