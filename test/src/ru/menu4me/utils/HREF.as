package ru.menu4me.utils {
	public class HREF {
		private static const TAG:String = "HREF";
		public static function convert(str:String):String {
			var result:String = "";
			var current:String = "";
			for (var i:int = 0; i < str.length; i++) {
				current = str.charAt(i);
				//Log.d(TAG, "result[iteration " + i + "] = " + result);
				
				switch(current) {
					case '/': {
						result += "%2F";
						break;
					}
					case ':': {
						result += "%3A";
						break;
					}
					default: {
						result += current;
						break;
					}
				}
			}
			//Log.d(TAG, "converted url: " + result);
			return result;
		}	
	}
}