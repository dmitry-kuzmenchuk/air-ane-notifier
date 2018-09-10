set OUT_ANE=..\out\Notifier.ane
set EXT_XML=..\xmls\extension.xml
set SWC=..\temp\Notifier.swc

"C:\flex-sdk\bin\adt.bat" -package -target ane %OUT_ANE% %EXT_XML% -swc %SWC% -platform Android-ARM -C ..\Android-ARM .