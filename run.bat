@echo off
setlocal

set MVN_BIN=apache-maven-3.9.6\bin
set JAVA_HOME=%JAVA_HOME%

echo Checking for Maven...
if not exist "%MVN_BIN%" (
    echo Maven not found in %MVN_BIN%. Please ensure setup_run.ps1 has been run at least once to download dependencies, or manually install Maven.
    pause
    exit /b 1
)

set PATH=%~dp0%MVN_BIN%;%PATH%

echo Building Project...
call mvn clean package -DskipTests

if %ERRORLEVEL% NEQ 0 (
    echo Build Failed!
    pause
    exit /b %ERRORLEVEL%
)

echo Starting Application...
call mvn exec:java -Dexec.mainClass="com.kiloux.restopos.Main"

endlocal
