@echo off

:: BatchGotAdmin
:-------------------------------------
REM  --> Check for permissions
echo Getting admin permissions...
>nul 2>&1 "%SYSTEMROOT%\system32\cacls.exe" "%SYSTEMROOT%\system32\config\system"

REM --> If error flag set, we do not have admin.
if '%errorlevel%' NEQ '0' (
    echo Requesting administrative privileges...
    goto UACPrompt
) else ( goto gotAdmin )

:UACPrompt
    echo Set UAC = CreateObject^("Shell.Application"^) > "%temp%\getadmin.vbs"
    echo UAC.ShellExecute "%~s0", "", "", "runas", 1 >> "%temp%\getadmin.vbs"

    "%temp%\getadmin.vbs"
    exit /B

:gotAdmin
    if exist "%temp%\getadmin.vbs" ( del "%temp%\getadmin.vbs" )
    pushd "%CD%"
    CD /D "%~dp0"
:--------------------------------------


REM  --> Install JDK 6
echo Installing Java...
call install\jdk-6u45-windows-x64.exe

REM  --> Install JDK 7
echo Installing Java...
call install\jdk-7u21-windows-x64.exe


REM --> Setting Java 6 to JAVA_HOME and PATH Environment variables
echo setting JAVA_HOME
setx -m JAVA_HOME "C:\Program Files\Java\jdk1.6.0_45"

echo Setting mavan and jdk in path 
set tmp=Program Files
setx -m PATH "%PATH%;C:\%tmp%\Java\jdk1.6.0_45\bin;C:\pt2014\Apache-maven-3.1.1\bin"


REM  --> Install Chrome
REM  --> echo Installing Chrome...
REM  --> call install\chrome.exe /silent /install

REM  --> Install Notepad++
echo Installing Notepad PP...
call install\notepadpp.exe


REM --> Setting PROXY environment variables
setx -m HTTP_PROXY_HOST proxy
setx -m HTTP_PROXY_PORT 8080
setx -m HTTPS_PROXY_HOST proxy
setx -m HTTPS_PROXY_PORT 8080
setx -m HTTP_NON_PROXY_HOSTS localhost


REM --> Setting environment variables required by extension application
setx -m NW_CLOUD_SDK_PATH   c:\pt2014\neo-sdk-javaweb-1.42.19
setx -m NW_CLOUD_SDK_VERSION   1.42.19
setx -m ECLIPSE_HOME   c:\pt2014\eclipse
setx -m SAP_UI5_VERSION   1.18.6