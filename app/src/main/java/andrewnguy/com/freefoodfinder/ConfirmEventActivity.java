package andrewnguy.com.freefoodfinder;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ConfirmEventActivity extends Activity implements View.OnClickListener {

    Button confirm, cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_event);

        confirm = (Button) findViewById(R.id.buttonConfirmPost); // confirm post button
        cancel = (Button) findViewById(R.id.buttonCancelPost);   // cancel post button

        confirm.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        if (v.getId() == R.id.buttonCancelPost) { // cancel the add
            finish();
        }
        else { // finish the add
            finish();
        }
    }
}
