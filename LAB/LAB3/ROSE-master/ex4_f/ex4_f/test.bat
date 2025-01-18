@echo off

cd bin

java -classpath ".;../lib/jgraph.jar;../lib/flowchart.jar" OberonMain ../src/testcases/calculate.001
java -classpath ".;../lib/jgraph.jar;../lib/flowchart.jar" OberonMain ../src/testcases/calculate.002
java -classpath ".;../lib/jgraph.jar;../lib/flowchart.jar" OberonMain ../src/testcases/calculate.003
java -classpath ".;../lib/jgraph.jar;../lib/flowchart.jar" OberonMain ../src/testcases/calculate.004
java -classpath ".;../lib/jgraph.jar;../lib/flowchart.jar" OberonMain ../src/testcases/calculate.005
java -classpath ".;../lib/jgraph.jar;../lib/flowchart.jar" OberonMain ../src/testcases/calculate.006
java -classpath ".;../lib/jgraph.jar;../lib/flowchart.jar" OberonMain ../src/testcases/calculate.007
java -classpath ".;../lib/jgraph.jar;../lib/flowchart.jar" OberonMain ../src/testcases/calculate.008
java -classpath ".;../lib/jgraph.jar;../lib/flowchart.jar" OberonMain ../src/testcases/calculate.009

cd ..

pause
