copy ..\..\as3\lib\Notifier.swc ..\temp\Notifier.swc
copy ..\..\java\app\build\outputs\aar\app-debug.aar ..\temp\libNotifier.aar


7z.exe x ..\temp\Notifier.swc -o..\temp\swc
7z.exe x ..\temp\libNotifier.aar -o..\temp\jar


move ..\temp\jar\classes.jar ..\Android-ARM\libNotifier.jar
move ..\temp\swc\library.swf ..\Android-ARM\library.swf

rd ..\temp\jar /s /q
rd ..\temp\swc /s /q