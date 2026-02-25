@echo off
setlocal

set ANT_BIN=apache-ant-1.10.14\bin

echo Checking for Ant...
if exist "%ANT_BIN%" (
    set "ANT_CMD=%~dp0%ANT_BIN%\ant.bat"
) else (
    where ant >nul 2>nul
    if %ERRORLEVEL% NEQ 0 (
        echo Ant not found. Install Apache Ant and make sure command 'ant' is available in PATH.
        echo Download: https://ant.apache.org/bindownload.cgi
        pause
        exit /b 1
    )
    set "ANT_CMD=ant"
)

echo Building Project...
call "%ANT_CMD%" clean copy-resources

if %ERRORLEVEL% NEQ 0 (
    echo Build Failed!
    pause
    exit /b %ERRORLEVEL%
)

echo Starting Application...
call "%ANT_CMD%" run

endlocal
