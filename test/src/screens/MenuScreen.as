package screens {
	import feathers.controls.Button;
	import feathers.controls.PanelScreen;
	import feathers.controls.WebView;
	import starling.core.Starling;
	import starling.events.Event;
	
	import flash.filesystem.File;
	import flash.filesystem.FileMode;
	
	import ru.menu4me.extensions.Notifier;
	import ru.menu4me.extensions.NotifierEvent;
	import ru.menu4me.utils.Log;
	
	public class MenuScreen extends PanelScreen {
		private static const TAG			: String = "Menu Screen";
		private var initNotifierButton		: Button = null;
		private var registerUpdatesButton	: Button = null;
		private var unregisterUpdatesButton : Button = null;
		private var notifier				: Notifier = null;
		private var clientInfo				: String = "{\"os\":\"Adobe Windows\",\"deviceId\":\"71e4889df5fd26a0d792597556c5e7523b5d288ec79971371ca5a5f1f0623a7f\",\"appVersion\":\"3.0.19\",\"osVersion\":\"Windows 10\",\"brand\":\"\",\"appId\":\"com.menu4me.richiepersonal.sushifood\",\"model\":\"\"}";
		
		
		public function MenuScreen() {
			Log.d(TAG, "MenuScreen()");
			super();
		}	
		
		override protected function initialize():void {
			Log.d(TAG, "initialize()");
			super.initialize();
			title = "Notificator";
			
			initNotifierButton = new Button();
			initNotifierButton.label = "Get test path";
			initNotifierButton.x = 5;
			initNotifierButton.y = 40;
			initNotifierButton.addEventListener(Event.TRIGGERED, initNotifierButtonTriggered);
			
			registerUpdatesButton = new Button();
			registerUpdatesButton.label = "Register Updates";
			registerUpdatesButton.x = 5;
			registerUpdatesButton.y = 80;
			registerUpdatesButton.addEventListener(Event.TRIGGERED, registerUpdatesButtonTriggered);
			
			unregisterUpdatesButton = new Button();
			unregisterUpdatesButton.label = "Unregister Updates";
			unregisterUpdatesButton.x = 5;
			unregisterUpdatesButton.y = 120;
			unregisterUpdatesButton.addEventListener(Event.TRIGGERED, unregisterUpdatesButtonTriggered);
			
			this.addChild(initNotifierButton);
			this.addChild(registerUpdatesButton);
			this.addChild(unregisterUpdatesButton);
			Startup.notifier.addEventListener(NotifierEvent.FROM_NOTIFICATION_EVENT, function(e:NotifierEvent):void {
				Log.d(TAG, "event: " + e + " & request: " + e.data);
			});
			Startup.notifier.addEventListener(NotifierEvent.FOREGROUND_NOTIFICATION_EVENT, function(e:NotifierEvent):void {
				Log.d(TAG, "event: " + e + " & request: " + e.data);
			});
			Startup.notifier.addEventListener(NotifierEvent.BACKGROUND_NOTIFICATION_EVENT, function(e:NotifierEvent):void {
				Log.d(TAG, "event: " + e + " & request: " + e.data);
			});
			
			Startup.notifier.fetchStarterNotification();
			Startup.notifier.initClientInfo(clientInfo);
			Startup.notifier.initLoginPass("login", "pass");
		}
		
		private function unregisterUpdatesButtonTriggered(e:Event):void {
			Log.d(TAG, "unregisterUpdatesButtonTriggered()");
			Startup.notifier.unregisterUpdates();
		}
		
		private function registerUpdatesButtonTriggered(e:Event):void {
			Log.d(TAG, "registerUpdatesButtonTriggered()");
			Startup.notifier.registerUpdates();
		}
		
		private function initNotifierButtonTriggered(e:Event):void {
			Log.d(TAG, "initNotifierButtonTriggered()");
			//var path:File = new File("file:///" + File.applicationDirectory.resolvePath("databases/m4m_orders").nativePath);
			//var dir:File = path.resolvePath("m4m_orders.db");
			//Log.d(TAG, "file: " + file);
			//Log.d(TAG, "nativePath: " + path.nativePath + " & url: " + path.url);
			//Startup.notifier.fetchStarterNotification();
			//notifier = Notifier.getInstance();
		}
	}
}