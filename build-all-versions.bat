@echo off
chcp 65001 >nul
echo ====================================
echo BridgingAnalyzer Multi-Version Build
echo ====================================
echo.

if not exist releases mkdir releases

echo [1/8] Building 2.1.0 version (default)...
call mvn clean package -DskipTests -q
if %errorlevel% neq 0 (
    echo ERROR: 2.1.0 version build failed
    pause
    exit /b 1
)
copy target\BridgingAnalyzer-2.1.0.jar releases\ >nul
echo ✅ 2.1.0 version completed

echo.
echo [2/8] Building 1.20 version...
call mvn -P legacy-1.20 package -DskipTests -q
if %errorlevel% neq 0 (
    echo ERROR: 1.20 version build failed
    pause
    exit /b 1
)
copy target\BridgingAnalyzer-2.1.0-1.20.jar releases\ >nul
echo ✅ 1.20 version completed

echo.
echo [3/8] Building 1.19 version...
call mvn -P legacy-1.19 package -DskipTests -q
if %errorlevel% neq 0 (
    echo ERROR: 1.19 version build failed
    pause
    exit /b 1
)
copy target\BridgingAnalyzer-2.1.0-1.19.jar releases\ >nul
echo ✅ 1.19 version completed

echo.
echo [4/8] Building 1.18 version...
call mvn -P legacy-1.18 package -DskipTests -q
if %errorlevel% neq 0 (
    echo ERROR: 1.18 version build failed
    pause
    exit /b 1
)
copy target\BridgingAnalyzer-2.1.0-1.18.jar releases\ >nul
echo ✅ 1.18 version completed

echo.
echo [5/8] Building 1.16 version...
call mvn -P legacy-1.16 package -DskipTests -q
if %errorlevel% neq 0 (
    echo ERROR: 1.16 version build failed
    pause
    exit /b 1
)
copy target\BridgingAnalyzer-2.1.0-1.16.jar releases\ >nul
echo ✅ 1.16 version completed

echo.
echo [6/8] Building 1.15 version...
call mvn -P legacy-1.15 package -DskipTests -q
if %errorlevel% neq 0 (
    echo ERROR: 1.15 version build failed
    pause
    exit /b 1
)
copy target\BridgingAnalyzer-2.1.0-1.15.jar releases\ >nul
echo ✅ 1.15 version completed

echo.
echo [7/8] Building 1.14 version...
call mvn -P legacy-1.14 package -DskipTests -q
if %errorlevel% neq 0 (
    echo ERROR: 1.14 version build failed
    pause
    exit /b 1
)
copy target\BridgingAnalyzer-2.1.0-1.14.jar releases\ >nul
echo ✅ 1.14 version completed

echo.
echo [8/8] Building 1.13 version...
call mvn -P legacy-1.13 package -DskipTests -q
if %errorlevel% neq 0 (
    echo ERROR: 1.13 version build failed
    pause
    exit /b 1
)
copy target\BridgingAnalyzer-2.1.0-1.13.jar releases\ >nul
echo ✅ 1.13 version completed

echo.
echo ====================================
echo Build completed! Generated files:
echo ====================================
dir releases\BridgingAnalyzer-*.jar /b
echo.
echo Usage:
echo - BridgingAnalyzer-2.1.0.jar        → 1.21+ servers
echo - BridgingAnalyzer-2.1.0-1.20.jar   → 1.20.4 servers
echo - BridgingAnalyzer-2.1.0-1.19.jar   → 1.19.2 servers
echo - BridgingAnalyzer-2.1.0-1.18.jar   → 1.18.1 servers
echo - BridgingAnalyzer-2.1.0-1.16.jar   → 1.16.5 servers
echo - BridgingAnalyzer-2.1.0-1.15.jar   → 1.15.2 servers
echo - BridgingAnalyzer-2.1.0-1.14.jar   → 1.14.4 servers  
echo - BridgingAnalyzer-2.1.0-1.13.jar   → 1.13.2 servers
echo.
echo All files are in the 'releases' directory.
echo.
pause
