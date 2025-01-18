@echo off
echo Processing expressions from ../testcases/tc-005.infix:
for /F "tokens=*" %%a in (../testcases/tc-005.infix) do (
    echo Expression: %%a
    echo %%a | java Postfix
)
pause