package mi3.erix;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class DrawView extends View {
	public static Paint occupied = new Paint();
	public static Paint line = new Paint();
	public static Paint currentColor;
	
	public int currentX = 0;
	public int currentY = 0;
	//public int[][] lineCoords;
	
	public int[] speed = new int[2];

	public DrawView(Context context) {
		super(context);
		occupied.setColor(Color.BLACK);
		line.setColor(Color.GRAY);
		currentColor = line;
	}
	
	public void onDraw (Canvas canvas) {
		drawSquare(currentColor,canvas,currentX,currentY);
	}
	
	private void drawSquare(Paint col, Canvas canvas, int x, int y) {
		canvas.drawRect(x, y, x+10, y+10, col);
	}

}
