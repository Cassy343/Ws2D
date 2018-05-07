@echo off
if %OS% == "Windows_NT" setlocal

set /p CP=<classpath.txt
set SOURCE_DIR=.\src
set BUILD_DIR=.\build
set DIST_DIR=.\run
if not exist %BUILD_DIR% mkdir %BUILD_DIR%

xcopy %SOURCE_DIR%\*.* %BUILD_DIR% /s /y
cd %BUILD_DIR%
for /R %%f in (*.java) do (
    javac -cp %CP% %%f
    del %%f
)

cd ..\

if not exist %DIST_DIR% mkdir %DIST_DIR%
jar cfm %DIST_DIR%\game.jar META-INF\manifest.mf -C %BUILD_DIR% .

if %OS% == "Windows_NT" endlocal
pause