@echo off
setlocal

REM Set paths
set "PROJECT_DIR=C:\Users\Admin\Documents\not-enough-donuts"
set "BUILD_JAR=%PROJECT_DIR%\build\libs\not-enough-donuts-1.0.0.jar"
set "MODS_DIR=C:\Users\Admin\AppData\Roaming\ModrinthApp\profiles\Meteor 1.21.5 (1)\mods"
set "DEST_JAR=%MODS_DIR%\not-enough-donuts-1.0.0.jar"

REM Navigate to project directory
cd /d "%PROJECT_DIR%"

REM Run Gradle clean and build
call gradlew clean build
if errorlevel 1 (
    echo Gradle build failed.
    pause
    exit /b
)

REM Wait for 1 second before moving the file


REM Check if the build was successful and the JAR exists
if exist "%BUILD_JAR%" (
    echo Build succeeded. Moving JAR to mods folder...
    move /Y "%BUILD_JAR%" "%DEST_JAR%"
    echo Move complete.
) else (
    echo Build failed or JAR not found.
)

pause
endlocal
