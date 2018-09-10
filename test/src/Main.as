package {
	import feathers.controls.Screen;
	import feathers.controls.Button;
	import feathers.controls.Header;
	import feathers.controls.List;
	import feathers.controls.renderers.DefaultListItemRenderer;
	import feathers.controls.renderers.IListItemRenderer;
	
	import feathers.data.ListCollection;
	
	import feathers.layout.AnchorLayout;
	import feathers.layout.AnchorLayoutData;
	
	import feathers.motion.Slide;
	import feathers.motion.Fade;
	
	import feathers.controls.StackScreenNavigator;
	import feathers.controls.StackScreenNavigatorItem;
	
	import feathers.themes.MetalWorksMobileTheme;
	
	import starling.events.Event;
	import starling.display.DisplayObject;
	
	import flash.filesystem.File;
	import flash.filesystem.FileMode;
	import flash.filesystem.FileStream;
	
	import flash.net.URLLoader;
	import flash.net.URLLoaderDataFormat;
	import flash.net.URLRequest;
	
	import flash.events.IOErrorEvent;
	import flash.events.ProgressEvent;
	
	import ru.menu4me.utils.Log;
	
	import screens.*;

	public class Main extends Screen
	{
		private static const TAG 	: String = "Main";
		private var ssNavigator		: StackScreenNavigator;
		public const MENU_SCREEN	: String = "menuScreen";
		
		public function Main() 
		{
			Log.d(TAG, "Main()");
			new MetalWorksMobileTheme();
			super();
		}
		
		override protected function initialize():void
		{
			Log.d(TAG, "initialize()");
			super.initialize();
			
			ssNavigator = new StackScreenNavigator();
			
			var menuScreen:StackScreenNavigatorItem = new StackScreenNavigatorItem(MenuScreen);
			ssNavigator.addScreen(MENU_SCREEN, menuScreen);
			
			ssNavigator.rootScreenID = MENU_SCREEN;
			ssNavigator.pushTransition = Slide.createSlideLeftTransition();
			ssNavigator.popTransition = Slide.createSlideRightTransition();
			
			layout = new AnchorLayout();
			addChild(ssNavigator);
		}
	}
}