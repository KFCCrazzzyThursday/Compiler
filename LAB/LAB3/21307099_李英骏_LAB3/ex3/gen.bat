@echo off
cd javacup
java -jar ..\src\jflex-1.9.1\lib\jflex-full-1.9.1.jar ..\src\oberon.flex
cd ..\src
java -jar ..\javacup\java-cup-11b.jar -parser Parser -symbols Symbol -nonterms ..\src\oberon.cup
cd ..
pause