package ru.menu4me.extensions {
	import flash.events.Event;
	
	public class NotifierEvent extends Event {
		
		public static const FROM_NOTIFICATION_EVENT : String = "fromNotificationEvent";
		public static const FOREGROUND_NOTIFICATION_EVENT : String = "foregroundNotificationEvent";
		
		public var data:String;
		
		public function NotifierEvent(type:String, bubbles:Boolean=false, cancelable:Boolean=false) {
			super(type, bubbles, cancelable);
		}
	}
}