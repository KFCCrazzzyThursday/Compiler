@echo off

cd src
echo Compiling Node.java and Symbol.java...
javac -d ..\bin -classpath ..\bin -encoding UTF-8 Node.java Symbol.java

cd ..
cd bin
echo Compiling other source files...
javac -d ..\bin -classpath ..\bin;..\bin\java_cup.runtime;..\lib\jgraph.jar;..\lib\callgraph.jar;..\lib\flowchart.jar ..\src\Main.java ..\src\OberonScanner.java ..\src\Parser.java

cd ..
pause
@echo on
