@echo off

rmdir /s /q bin

mkdir bin

echo Please wait...
cd src

javac -d ../bin exceptions/*.java

javac -d ../bin -classpath "../bin;../lib/jgraph.jar;../lib/callgraph.jar;../lib/flowchart.jar" *.java

cd ..

pause
