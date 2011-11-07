package mi3.erix;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ErixActivity extends Activity implements android.view.GestureDetector.OnGestureListener {
    /** Called when the activity is first created. */

	LinearLayout layout; 
	TextView flingDetector; // For debugging.
	DrawView drawView; // For the actual game.

	private GestureDetector gestureScanner; // Detects flings/swipes.
	
	// For moving stuff on the canvas.
	protected boolean running = false;
	Thread drawThread = null;
	
	Handler drawHandler = new Handler (); // Handler to move our object every second.
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // For the purpose of detecting flings.
        gestureScanner = new GestureDetector(this);
        
        // Set up the layout.
        layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        
        flingDetector = new TextView(this);
        flingDetector.setText("No flings yet"); // Debugging
        layout.addView(flingDetector);
        
        drawView = new DrawView(this);
        drawView.setBackgroundColor(Color.WHITE);
        layout.addView(drawView);
        
        startDrawing(); // Start the thread that takes care of moving our object.
        
        setContentView(layout); // We don't care about R.java
    }
	
	/*
	 * This is the thread that moves the object.
	 * It should be updated to check for direction,
	 * which again involves changes in the DrawView
	 * class. Right now it's just an example.
	 */
	public void startDrawing () {
		Runnable runner = new Runnable () {

			@Override
			public void run() {
				while(true) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						flingDetector.setText("Something went wrong with the... waiting.");
					}
					drawHandler.post(new Runnable () {

						@Override
						public void run() {
							drawView.currentX += 10;
							drawView.invalidate();
						}
						
					});
				}
			}
			
		};
		new Thread(runner).start();
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
				flingDetector.setText("Left!");
				if(drawView.currentX >= 10) { // Stop at the left edge
					drawView.currentX -= 10; // Give the onDraw function a new x.
					drawView.invalidate(); // Refresh the screen.
				}
			} else { // Right
				flingDetector.setText("Right!");
				if(drawView.currentX <= 100) { // Stop after 100 pixel - change later.
					drawView.currentX += 10; // New x value.
					drawView.invalidate(); // Refresh screen.
				}
			}
		} else { // y axis
			if(velocityY <= 0) { // Moving upwards.
				flingDetector.setText("Up!");
				if(drawView.currentY >= 10) { // Stop at the top of the screen.
					drawView.currentY -= 10; // New y value.
					drawView.invalidate(); // Refresh screen.
				}
			} else { // Down.
				flingDetector.setText("Down!");
				if(drawView.currentY <= 100) { // Stop after 100 pixel - change later.
					drawView.currentY += 10; // New y value.
					drawView.invalidate(); // Refresh screen.
				}
			}
		}
		return true; // Tells the program that yes, we are using this action.
	}
	
	/*
	 * En bunke ting som skal være med fordi
	 * vi implementerer OnGestureListener.
	 * All returns false to show that we're
	 * not using them for anything.
	 */

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		//flingDetector.setText("Down");
		return false;
	}
	
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