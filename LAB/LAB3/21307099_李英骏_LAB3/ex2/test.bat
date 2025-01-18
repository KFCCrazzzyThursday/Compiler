@echo off
cd bin

REM 循环处理所有 testcases 目录下的 arithmetic 文件
for %%f in (..\src\testcases\arithmetic.*) do (
    echo Running %%~nxf
    java Main %%f
)

cd ..

pause