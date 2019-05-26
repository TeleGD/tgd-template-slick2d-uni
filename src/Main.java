import java.awt.DisplayMode;
import java.awt.GraphicsEnvironment;

import app.AppGame;

public final class Main {

	public static final void main(String [] arguments) {
		DisplayMode display = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode();
		String title = "Uni By TGD";
		int width = display.getWidth();
		int height = display.getHeight();
		boolean fullscreen = true;
		new AppGame(title, width, height, fullscreen) {

			@Override
			public void init() {
				this.addState(new pages.Welcome(AppGame.PAGES_WELCOME));
				this.addState(new pages.Menu(AppGame.PAGES_MENU));
				this.addState(new pages.Pause(AppGame.PAGES_PAUSE));
				this.addState(new games.test.World(AppGame.PAGES_GAME));
			}

		};
	}

}
