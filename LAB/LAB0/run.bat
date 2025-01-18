@echo off
setlocal

:: 设置Java文件的位置
set JAVA_FILES_PATH=.\Code

:: 编译源代码
echo Compiling Java source files...
javac %JAVA_FILES_PATH%\*.java
if %ERRORLEVEL% neq 0 goto error

:: 运行程序
echo Running program...
java -cp %JAVA_FILES_PATH% Main
if %ERRORLEVEL% neq 0 goto error

:: 运行回归测试
echo.
echo Running regression tests...
call :runTest 10000 "应缴税额: 45.00元"
call :runTest 30000 "应缴税额: 1695.00元"
call :runTest 60000 "应缴税额: 7745.00元"
call :runTest 85000 "应缴税额: 13495.00元"
goto end

:error
echo There was an error during the operation.
goto end

:runTest
set INCOME=%1
set EXPECTED_OUTPUT=%2
java -cp %JAVA_FILES_PATH% Main %INCOME% | findstr /C:"%EXPECTED_OUTPUT%"
if %ERRORLEVEL% neq 0 (
    echo Test failed for income: %INCOME%, expected output: %EXPECTED_OUTPUT%
) else (
    echo Test passed for income: %INCOME%
)
goto :eof

:end
endlocal
