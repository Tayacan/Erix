package mi3.erix;

import java.util.ArrayList;

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
	public ArrayList<String> lineCoords = new ArrayList<String>();
	
	public int[] speed = new int[2];

	public DrawView(Context context) {
		super(context);
		occupied.setColor(Color.BLACK);
		line.setColor(Color.GRAY);
		currentColor = line;
	}
	
	public void onDraw (Canvas canvas) {
		drawSquare(currentColor,canvas,currentX,currentY);
		drawLine(lineCoords, canvas);
	}
	
	private void drawSquare(Paint col, Canvas canvas, int x, int y) {
		canvas.drawRect(x, y, x+10, y+10, col);
	}
	
	/*
	 * Draws all the tiles which are part of
	 * the line behind the figure. 
	 * Sort of slow and laggy right now -
	 * no idea how we can optimize it, since
	 * it has to draw the entire array of tiles
	 * each time. Maybe do something about bigger
	 * rectangles.
	 */
	private void drawLine(ArrayList<String> lineCoords2, Canvas canvas) {
		for(String coord : lineCoords2) {
			String[] numbers = coord.split("x");
			int newCoords[] = new int[2];
			int j = 0;
			for (String i : numbers) {
				if(i != "x") {
					newCoords[j] = Integer.parseInt(i);
					j++;
				}
			}
			drawSquare(DrawView.line, canvas, newCoords[0]*10,newCoords[1]*10);
		}
	}

}
