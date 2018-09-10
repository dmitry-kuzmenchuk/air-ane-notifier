:: Android Support Library
set SUPPORT_DIR=C:\android-sdk\extras\android\m2repository\com\android\support
set SUPPORT_DEP=support-compat
set SUPPORT_VERSION=25.1.0

copy %SUPPORT_DIR%\%SUPPORT_DEP%\%SUPPORT_VERSION%\%SUPPORT_DEP%-%SUPPORT_VERSION%.aar ..\temp\%SUPPORT_DEP%-%SUPPORT_VERSION%.aar
7z.exe x ..\temp\%SUPPORT_DEP%-%SUPPORT_VERSION%.aar -o..\temp\%SUPPORT_DEP%-%SUPPORT_VERSION%
7z.exe x ..\temp\%SUPPORT_DEP%-%SUPPORT_VERSION%\classes.jar -o..\deps\
rd ..\deps\META-INF /s /q

:: OkHttp library
set OKHTTP_DIR=C:\android-sdk\extras
set OKHTTP_DEP=okhttp
set OKHTTP_VERSION=3.5.0

copy %OKHTTP_DIR%\%OKHTTP_DEP%-%OKHTTP_VERSION%.jar ..\temp\%OKHTTP_DEP%-%OKHTTP_VERSION%.jar
7z.exe x ..\temp\%OKHTTP_DEP%-%OKHTTP_VERSION%.jar -o..\deps\
rd ..\deps\META-INF /s /q

:: Okio dependency for OkHttp
set OKIO_DIR=C:\android-sdk\extras
set OKIO_DEP=okio
set OKIO_VER=1.11.0

copy %OKIO_DIR%\%OKIO_DEP%-%OKIO_VER%.jar ..\temp\%OKIO_DEP%-%OKIO_VER%.jar
7z.exe x ..\temp\%OKIO_DEP%-%OKIO_VER%.jar -o..\deps\
rd ..\deps\META-INF /s /q

:: Google Json library
set GSON_DIR=c:\android-sdk\extras
set GSON_DEP=gson
set GSON_VERSION=2.8.0

copy %GSON_DIR%\%GSON_DEP%-%GSON_VERSION%.jar ..\temp\%GSON_DEP%-%GSON_VERSION%.jar
7z.exe x ..\temp\%GSON_DEP%-%GSON_VERSION%.jar -o..\deps\
rd ..\deps\META-INF /s /q