package janyelson.starwarslist.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import janyelson.starwarslist.R;

/**
 * Created by MEU PC on 04/07/2017.
 */

public class Start extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_INTERNET= 103;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        /*
        if (Build.VERSION.SDK_INT >= 23) {
            //int count = 0;
            verifyPermission();
            while (true) {
                if ((ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED)) {
                    break;
                }
                if(auxPermission == -1) {
                    break;
                }
            }
        }

        if(auxPermission == -1) {
            return;
        }
        */

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
               switchSreen();
            }
        }, 2000);
    }

    private void switchSreen() {
        Intent intent = new Intent(Start.this, Home.class);
        startActivity(intent);
        finish();
    }

}
