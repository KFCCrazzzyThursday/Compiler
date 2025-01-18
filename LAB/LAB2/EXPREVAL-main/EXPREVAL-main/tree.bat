@echo off
cd your_directory_path

dir /A-D /B

echo .
echo .\src:
dir .\src /A-D /B

echo .
echo .\testcases:
dir .\testcases /A-D /B

pause
@echo on
