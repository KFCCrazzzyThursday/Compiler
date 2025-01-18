@echo off
setlocal enabledelayedexpansion

rem 设置循环范围
set /A startNum=1
set /A endNum=2000

rem 循环测试每个用例
for /L %%i in (%startNum%, 1, %endNum%) do (
    set "num=%%i"
    set "tcNum=00!num!"
    set "tcNum=!tcNum:~-3!"

    rem 显示当前测试用例信息
    echo Running Testcase !tcNum!: a correct input from DBv2.
    echo ==============================================
    echo The input is:
    type testcases\tc-!tcNum!.infix
    echo ----------------------------------------------
    cd bin

    rem 运行测试用例
    java Postfix < ..\testcases\tc-!tcNum!.infix >> ..\time.txt

    rem 显示预期输出
    echo ----------------------------------------------
    echo The output should be:

    cd ..
    echo ==============================================

    echo.
)

echo on
