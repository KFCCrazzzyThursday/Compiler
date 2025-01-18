@echo off
setlocal

set SRC_PATH=.\Code
set CLASS_PATH=.\ClassFile

javac -encoding UTF-8 -d %CLASS_PATH% %SRC_PATH%\*.java

java -cp %CLASS_PATH% Main

pause