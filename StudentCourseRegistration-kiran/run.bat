@echo off
echo Compiling project...
javac -cp ".;lib/lib/mysql-connector-j-9.6.0.jar" model/*.java service/*.java ui/*.java util/*.java
if %errorlevel% neq 0 (
    echo Compilation failed!
    pause
    exit /b %errorlevel%
)
echo Compilation successful.
echo Running application...
java -cp ".;lib/lib/mysql-connector-j-9.6.0.jar" ui.LoginUI
pause
