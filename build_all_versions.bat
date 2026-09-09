@echo off
echo Building universal BridgingAnalyzer jar...
call mvn clean package -DskipTests
if %ERRORLEVEL% NEQ 0 (
    echo Build failed.
    exit /b %ERRORLEVEL%
)
echo.
echo Build complete: target\BridgingAnalyzer-2.3.3.jar
echo This single jar supports Minecraft 1.8.8 - 26.x+
