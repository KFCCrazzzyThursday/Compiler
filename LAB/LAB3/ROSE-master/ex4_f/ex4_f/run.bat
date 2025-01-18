@echo off

cd bin

java -classpath ".;../lib/jgraph.jar;../lib/flowchart.jar" OberonMain ../src/calculate.obr

cd ..

pause
