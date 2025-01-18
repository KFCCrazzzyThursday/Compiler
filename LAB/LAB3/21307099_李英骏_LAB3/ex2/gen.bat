@echo off
cd jflex\jflex-1.9.1\lib
java -jar jflex-full-1.9.1.jar ..\..\..\src\oberon.flex
cd ..
pause