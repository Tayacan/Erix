package mi3.erix;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

public class ErixActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        LinearLayout layout = new LinearLayout(this);
        
        setContentView(layout);
    }
}