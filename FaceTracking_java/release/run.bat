@ECHO OFF

SET LAB_ID="ADSL"

IF NOT EXIST "%JAVA_HOME%" (
	echo "Please set the JAVA_HOME environment variable"
) ELSE (
    
	"%JAVA_HOME%\bin\java" -Djava.library.path=. -jar facetrack.jar %LAB_ID%
)

