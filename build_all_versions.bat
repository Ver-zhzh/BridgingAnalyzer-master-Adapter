@echo off
echo Building all versions of BridgingAnalyzer v2.1.3...

echo.
echo Building 1.16 version...
call mvn clean package -P legacy-1.16 -q
copy "target\BridgingAnalyzer-2.1.2-1.16.jar" "release\"

echo.
echo Building 1.15 version...
call mvn clean package -P legacy-1.15 -q
copy "target\BridgingAnalyzer-2.1.2-1.15.jar" "release\"

echo.
echo Building 1.14 version...
call mvn clean package -P legacy-1.14 -q
copy "target\BridgingAnalyzer-2.1.2-1.14.jar" "release\"

echo.
echo Building 1.13 version...
call mvn clean package -P legacy-1.13 -q
copy "target\BridgingAnalyzer-2.1.2-1.13.jar" "release\"

echo.
echo All versions built successfully!
echo Files are available in the release directory.