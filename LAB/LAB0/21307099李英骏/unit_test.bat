@echo off
setlocal

set TEST_PATH=.\Test
set CLASS_PATH=.\ClassFile


java -cp %CLASS_PATH% Main < %TEST_PATH%\input.txt > %TEST_PATH%\test_output.txt

fc %TEST_PATH%\test_output.txt %TEST_PATH%\output.txt > nul

if %ERRORLEVEL% == 0 (
    echo Test Passed: Output matches expected output.
) else (
    echo Test Failed: Output does not match expected output.
)
pause