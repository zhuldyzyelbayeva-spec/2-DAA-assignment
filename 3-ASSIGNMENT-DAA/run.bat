@echo off
setlocal

echo =====================================
echo Assignment 2
echo =====================================

if not exist out mkdir out
if not exist results\tables mkdir results\tables
if not exist results\plots mkdir results\plots

echo.
echo Compiling Java source files...
javac -d out src\*.java

if errorlevel 1 (
    echo.
    echo COMPILATION FAILED
    pause
    exit /b 1
)

echo.
echo Running tests...
java -ea -cp out Tests

if errorlevel 1 (
    echo.
    echo TESTS FAILED
    pause
    exit /b 1
)

echo.
echo Running benchmark...
java -ea -cp out Benchmark

if errorlevel 1 (
    echo.
    echo BENCHMARK FAILED
    pause
    exit /b 1
)

echo.
echo Generating plots...
python plot_results.py

if errorlevel 1 (
    echo.
    echo PLOT GENERATION FAILED
    pause
    exit /b 1
)

echo.
echo =====================================
echo ASSIGNMENT COMPLETED SUCCESSFULLY
echo =====================================
echo.
echo Results:
echo   Tables: results\tables
echo   Plots:  results\plots
echo.
pause

endlocal