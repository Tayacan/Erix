package mi3.erix;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ErixActivity extends Activity implements android.view.GestureDetector.OnGestureListener {
    /** Called when the activity is first created. */

	LinearLayout layout;
	TextView flingDetector;
	DrawView drawView; 

	private GestureDetector gestureScanner;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        gestureScanner = new GestureDetector(this);
        
        layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        
        flingDetector = new TextView(this);
        flingDetector.setText("No flings yet.");
        layout.addView(flingDetector);
        
        drawView = new DrawView(this);
        drawView.setBackgroundColor(Color.WHITE);
        layout.addView(drawView);
        
        setContentView(layout);
    }

	@Override
	public boolean onTouchEvent(MotionEvent me) {
		return gestureScanner.onTouchEvent(me);
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		float xMov = velocityX;
		float yMov = velocityY;
		int dir = 1; // Skal bruges senere.
		if(xMov < 0) {
			xMov *= -1;
		}
		if(yMov < 0) {
			yMov *= -1;
		}
		if(xMov >= yMov) {
			if(velocityX <= 0) {
				dir = -1; // Skal bruges senere.
				flingDetector.setText("Left!");
				if(drawView.currentX >= 10) {
					drawView.currentX -= 10;
					drawView.invalidate();
				}
			} else {
				flingDetector.setText("Right!");
				if(drawView.currentX <= 100) {
					drawView.currentX += 10;
					drawView.invalidate();
				}
			}
		} else {
			if(velocityY <= 0) {
				dir = -1; // Skal bruges senere.
				flingDetector.setText("Up!");
				if(drawView.currentY >= 10) {
					drawView.currentY -= 10;
					drawView.invalidate();
				}
			} else {
				flingDetector.setText("Down!");
				if(drawView.currentY <= 100) {
					drawView.currentY += 10;
					drawView.invalidate();
				}
			}
		}
		return true;
	}
	
	/*
	 * En bunke ting som skal være med fordi
	 * vi implementerer OnGestureListener.
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