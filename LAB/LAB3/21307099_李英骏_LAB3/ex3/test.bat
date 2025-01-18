@echo off
cd bin

for %%f in (..\src\testcases\arithmetic.*) do (
    REM 获取文件扩展名
    set "ext=%%~xf"

    REM 检查文件扩展名是否为 .obr
    if /I not "%%ext%%"==".obr" (
        echo Running %%~nxf
        java Main %%f
    )
)

cd ..

pause