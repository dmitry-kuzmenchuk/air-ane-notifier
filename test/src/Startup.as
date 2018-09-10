package {
	import flash.events.Event;
	import flash.display.Sprite;
	import flash.display.StageAlign;
	import flash.display.StageScaleMode;
	
	import starling.core.Starling;
	import feathers.utils.ScreenDensityScaleFactorManager;
	import flash.display.LoaderInfo;
	import ru.menu4me.utils.Log;
	import ru.menu4me.extensions.Notifier;
	
	public class Startup extends Sprite {
		private static const TAG	: String = "Startup";
		public var starling 		: Starling;
		public var scaler 			: ScreenDensityScaleFactorManager;
		public static var notifier	: Notifier;
		
		public function Startup() {
			Log.d(TAG, "Startup()");
			
			stage.scaleMode = StageScaleMode.NO_SCALE;
			stage.align = StageAlign.TOP_LEFT;
			
			loaderInfo.addEventListener(Event.COMPLETE, onComplete);
		}

		protected function onComplete(e : Event) : void {
			Log.d(TAG, "onComplete()");
			
			starling = new Starling(Main, stage);
			starling.supportHighResolutions = true;
			starling.skipUnchangedFrames = true;
			starling.start();
			
			notifier = new Notifier();
			notifier.initLoginPass("design@menu4me.com", "design");
			notifier.setIsInForeground(true);
			scaler = new ScreenDensityScaleFactorManager(starling);
			stage.addEventListener(Event.DEACTIVATE, onDeactivate);
		}
		
		private function onDeactivate(e : Event) : void {
			Log.d(TAG, "onDeactivate()");
			notifier.setIsInForeground(false);
			starling.stop(true);
			stage.addEventListener(Event.ACTIVATE, onActivate);
		}
		
		private function onActivate(e : Event) : void {
			Log.d(TAG, "onActivate");
			notifier.setIsInForeground(true);
			stage.removeEventListener(Event.ACTIVATE, onActivate);
			starling.start();
		}
	}
}