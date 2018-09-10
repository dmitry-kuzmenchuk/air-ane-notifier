package ru.menu4me.utils {
	
	import Date;
	
	public class Log {
		
		private static var date:Date;
		
		public static function d(tag:String, text:String):void {
			date = new Date();
			trace("[" + date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds() + "::" + date.getMilliseconds() + "] " + "(" + tag + ") " + text);
		}
		
		public static function w(tag:String, text:String):void {
			date = new Date();
			trace("[" + date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds() + "::" + date.getMilliseconds() + "] " + "(" + tag + ") " + "[WARNING] " + text);
		}
		
		public static function e(tag:String, text:String):void {
			date = new Date();
			trace("[" + date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds() + "::" + date.getMilliseconds() + "] " + "(" + tag + ") " + "[ERROR] " + text);	
		}
	}
}