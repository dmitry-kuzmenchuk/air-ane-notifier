package ru.menu4me.extensions {
	import flash.external.ExtensionContext;
	import flash.events.EventDispatcher;
	import flash.events.Event;
	import flash.events.StatusEvent;
	import ru.menu4me.extensions.NotifierEvent;
	
	import ru.menu4me.utils.Log;
	
	public class Notifier extends EventDispatcher {
		private static const TAG	: String = "Notifier";
		private static var extensionContext: ExtensionContext = null;
		private static var instance: Notifier = null;
		
		public function Notifier() {
			Log.d(TAG, "Notifier()");
			if (!instance) {
				if (!extensionContext) {
				extensionContext = ExtensionContext.createExtensionContext("ru.menu4me.extensions.Notifier", "");
				}
				
				if (!extensionContext) {
					Log.e(TAG, "Unable to create Extension Context");
				} else {
					Log.d(TAG, "Extension Context was created successfully");
					extensionContext.addEventListener(StatusEvent.STATUS, onStatusEvent);
				}
			} else {
				Log.w(TAG, "Do not use constructor directly, it's a singleton. Use getInstance() instead");
			}
		}
		
		public function registerForPushNotifications(projectId:String):void {
			Log.d(TAG, "registerForPushNotifications()");
			if (extensionContext) {
				extensionContext.call("registerForPushNotifications");
			} else {
				Log.d(TAG, "Unable to register for push notifications, Extension Context is not initialized");
			}
		}
		
		public static function getInstance():Notifier {
			Log.d(TAG, "getInstance()");
			return instance ? instance : instance = new Notifier();
		}
		
		public function unregisterUpdates():void {
			Log.d(TAG, "unregisterUpdates()");
			
			if (!extensionContext) {
				Log.e(TAG, "Unable to unregister updates, Extension Context is not initialized");
			} else {
				Log.d(TAG, "Unregistering updates...");
				extensionContext.call("unregisterUpdates");
			}
		}
		
		public function registerUpdates():void {
			Log.d(TAG, "registerUpdates()");
			if (!extensionContext) {
				Log.e(TAG, "Unable to register updates, Extension Context is not initialized");
			} else {
				Log.d(TAG, "Registering updates...");
				extensionContext.call("registerUpdates");
			}
		}
		
		public function fetchStarterNotification():void {
			Log.d(TAG, "fetchStarterNotification()");
			if (!extensionContext) {
				Log.e(TAG, "Unable to fetch starter notification, Extension Context is not initialized");
			} else {
				Log.d(TAG, "fetching starter notification...");
				extensionContext.call("fetchStarterNotification");
			}
		}
		
		public function setIsInForeground(value:Boolean):void {
			Log.d(TAG, "setIsInForeground()");
			if (extensionContext != null) {
				extensionContext.call("setIsInForeground", value);
			}
		}
		
		public function initData(login:String, password:String, clientInfo:String, ownerId:String):void {
			Log.d(TAG, "initData()");
			extensionContext.call("initData", login, password, clientInfo, ownerId);
		}
		
		public function forceUpdate():void {
			Log.d(TAG, "forceUpdate()");
			extensionContext.call("forceUpdate");
		}
		
		public function purgeData():void {
			Log.d(TAG, "purgeData()");
			extensionContext.call("purgeData");
		}
		
		private function onStatusEvent(e:StatusEvent):void {
			Log.d(TAG, "onStatusEvent()");
			var event : NotifierEvent;
			var data : String = e.level;
			
			switch (e.code) {
				case NotifierEvent.FROM_NOTIFICATION_EVENT: {
					Log.d(TAG, "e.code: " + e.code);
					Log.d(TAG, "e.level: " + e.level);
					event = new NotifierEvent(NotifierEvent.FROM_NOTIFICATION_EVENT);
					if (data != null) {
						event.data = data;
					}
					break;
				}
				case NotifierEvent.FOREGROUND_NOTIFICATION_EVENT: {
					Log.d(TAG, "e.code: " + e.code);
					Log.d(TAG, "e.level: " + e.level);
					event = new NotifierEvent(NotifierEvent.FOREGROUND_NOTIFICATION_EVENT);
					if (data != null) {
						event.data = data;
					}
					break;
				}
			}
			
			if (event != null) {
				dispatchEvent(event);
			}
		}
	}
}