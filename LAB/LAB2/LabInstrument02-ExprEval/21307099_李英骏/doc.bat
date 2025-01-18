@echo off
cd src
javadoc -private -author -version -d ..\doc -classpath ..\bin -encoding UTF-8 -charset UTF-8 parser\*.java scanner\*.java DFA\*.java token\*.java token\Function\*.java token\Value\*.java token\Operator\*.java token\Symbol\*.java arithmetic\*.java
cd ..
pause
@echo on
