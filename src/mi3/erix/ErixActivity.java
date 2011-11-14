package mi3.erix;

import java.util.ArrayList;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ErixActivity extends Activity implements android.view.GestureDetector.OnGestureListener {
    /** Called when the activity is first created. */

	LinearLayout layout; 
	LinearLayout gameOver;
	TextView flingDetector; // For debugging.
	DrawView drawView; // For the actual game.
	DisplayMetrics metrics = new DisplayMetrics();
	int screenWidth;
	int screenHeight;
	
	public boolean gameOverCheck = false;
	
	// Tileset using constructor v2.
	private TileSet tileset = new TileSet(screenWidth / 10, screenHeight / 10);

	private GestureDetector gestureScanner; // Detects flings/swipes.
	
	// For moving stuff on the canvas.
	protected boolean running = false;
	Thread drawThread = null;
	
	Handler drawHandler = new Handler (); // Handler to move our object every second.
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Hide the title and notification bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        final Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        // For the purpose of detecting flings.
        gestureScanner = new GestureDetector(this);
        
        // Set up the layout.
        layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        
        // START Game over screen
        gameOver = new LinearLayout(this);
        gameOver.setOrientation(LinearLayout.VERTICAL);
        
        TextView gameOverText = new TextView(this);
        gameOverText.setText("Game over!");
        gameOver.addView(gameOverText);
        
        final TextView restart = new TextView(this);
        restart.setText("Touch screen to restart.");
        gameOver.addView(restart);
        // END Game over screen
        
        // Get the screen size for later
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;
        
        flingDetector = new TextView(this);
        flingDetector.setText(screenWidth + ", " + screenHeight); // Debugging
        //layout.addView(flingDetector);
        
        drawView = new DrawView(this);
        drawView.setBackgroundColor(Color.WHITE);
        layout.addView(drawView);
        
        // Vi vil gerne vide hvor spilleren er fra starten.
        tileset.tileStatus.put(drawView.currentX + "x" + drawView.currentY,TileSet.PLAYER);
        
        startDrawing(); // Start the thread that takes care of moving our object.
        
        setContentView(layout); // We don't care about R.java
    }
	
	/*
	 * This method restarts the app. Should be called by the restart button's onClick method.
	 */
	public void reload () {
		Intent intent = getIntent();
		overridePendingTransition(0, 0);
	    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
	    
	    finish();
	    
	    overridePendingTransition(0, 0);
	    startActivity(intent);
	}
	
	/*
	 * This is the thread that moves the object.
	 * It sends messages, in the form of Runnables,
	 * to our Handler.
	 */
	public void startDrawing () {
		Runnable runner = new Runnable () {

			@Override
			public void run() {
				while(true) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						flingDetector.setText("Something went wrong with the... waiting.");
					}
					if(!gameOverCheck) { // Only keep drawing as long as we're still alive.
						drawHandler.post(new Runnable () {
	
							@Override
							public void run() {
								if(tileset.tileStatus.get(drawView.currentX / 10 + "x" + drawView.currentY / 10) == null || 
										tileset.tileStatus.get(drawView.currentX / 10 + "x" + drawView.currentY / 10) == TileSet.EMPTY ||
										tileset.tileStatus.get(drawView.currentX / 10 + "x" + drawView.currentY / 10) == TileSet.PLAYER) {
									// We passed through this tile, change it to be a line so it will be drawn.
									tileset.tileStatus.put(drawView.currentX / 10 + "x" + drawView.currentY / 10, TileSet.LINE);
								}
								
								// Clear out the list of what should be drawn.
								drawView.lineCoords = new ArrayList<String>();
								// Set the coords for tiles that need to be colored gray.
								for (Map.Entry<String, Integer> entry : tileset.tileStatus.entrySet()) {
									if(entry.getValue() == TileSet.LINE) { // If it's a line...
										// ...add the chord
										drawView.lineCoords.add(entry.getKey());
									}
								}
								// Game over - first make sure we're checking a valid tile (not null)
								if (tileset.tileStatus.get((drawView.currentX + drawView.speed[0]) / 10 + "x" + (drawView.currentY + drawView.speed[1]) / 10) != null) {
									// Then check if that tile is a line AND we're currently moving
									if(tileset.tileStatus.get((drawView.currentX + drawView.speed[0]) / 10 + "x"
											+ (drawView.currentY + drawView.speed[1]) / 10) == TileSet.LINE &&
											drawView.speed[0] != 0 || drawView.speed[1] != 0) {
										// If so, we die because we hit our own line.
										gameOverCheck = true;
									}
								} else {
									//flingDetector.setText((drawView.currentX + drawView.speed[0]) + "x" + (drawView.currentY + drawView.speed[1]));
								}
								
								// Keep us within the limits of the screen (don't move if that will take us outside the screen)
								if (drawView.currentX + drawView.speed[0] >= 0 && drawView.currentX + drawView.speed[0] <= screenWidth - 10) {
									drawView.currentX += drawView.speed[0];
								}
								if (drawView.currentY + drawView.speed[1] >= 0 && drawView.currentY + drawView.speed[1] <= screenHeight - 10) {
									drawView.currentY += drawView.speed[1];
								}
								// The current position of the player. 
								tileset.tileStatus.put(drawView.currentX / 10 + "x" + drawView.currentY / 10, TileSet.PLAYER);
								
								drawView.invalidate(); // Stuff changed, we should draw it all again.
							}
							
						});
					} else { // If gameOverCheck is true we want to not draw at all, but send a different message to the handler.
						drawHandler.post(new Runnable () {

							@Override
							public void run() {
								// Go to the gameOver screen.
								setContentView(gameOver);
							}
							
						});
					}
				}
			}
			
		};
		new Thread(runner).start(); // Start the thread
	}

	// Make sure to intercept all touch events so we can run our own code.
	@Override
	public boolean onTouchEvent(MotionEvent me) {
		return gestureScanner.onTouchEvent(me);
	}

	// onFling is called whenever the user swipes a finger over the screen.
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		// We copy the velocities so we can manipulate them without losing the originals.
		float xMov = velocityX;
		float yMov = velocityY;
		
		// Make sure that we only have positive values for figuring out what axis we're moving on.
		if(xMov < 0) {
			xMov *= -1;
		}
		if(yMov < 0) {
			yMov *= -1;
		}
		
		// Figure out what axis we're moving on.
		if(xMov >= yMov) { // x axis
			if(velocityX <= 0) { // Check the original velocity to get the direction
				drawView.speed[0] = -10;
				drawView.speed[1] = 0;
			} else { // Right
				drawView.speed[0] = 10;
				drawView.speed[1] = 0;
			}
		} else { // y axis
			if(velocityY <= 0) { // Moving upwards.
				drawView.speed[0] = 0;
				drawView.speed[1] = -10;
			} else { // Down.
				drawView.speed[0] = 0;
				drawView.speed[1] = 10;
			}
		}
		return true; // Tells the program that yes, we are using this action.
	}

	/*
	 * If the screen is touched while we're at the game over screen,
	 * reload the application.
	 */
	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		//flingDetector.setText("Down");
		
		if(gameOverCheck) {
			reload();
			return true;
		}
		
		return false;
	}
	
	/*
	 * En bunke ting som skal være med fordi
	 * vi implementerer OnGestureListener.
	 * All returns false to show that we're
	 * not using them for anything.
	 */
	
	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
}