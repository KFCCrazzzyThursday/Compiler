@echo off
cd src
call jflex --nobak -q oberon.flex
call java -jar ../javacup/java-cup-11b.jar -parser Parser -symbols Symbol -nonterms oberon.cup 
cd ..
pause
@echo on