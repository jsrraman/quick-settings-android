package com.rajaraman.qsnotificationbar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    final Utils utils = Utils.getInstance();
    utils.setContext(this);

    Button button_enable = (Button) findViewById(R.id.button_enable);
    button_enable.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        utils.showPersistentNotification(Utils.NOTIFICATION_DEFAULT);
      }
    });

    Button button_disable = (Button) findViewById(R.id.button_disable);
    button_disable.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        utils.cancelPersistentNotification();

        // TODO Also need to stop the service
      }
    });

  }

  @Override
  protected void onResume() {
    // TODO Auto-generated method stub
    super.onResume();

    Log.d(Constants.TAG, "onResume");
  }

  @Override
  protected void onPause() {
    // TODO Auto-generated method stub
    super.onPause();

    Log.d(Constants.TAG, "onPause");
  }

  @Override
  protected void onDestroy() {
    // TODO Auto-generated method stub
    super.onDestroy();

    // TODO Stop the service and cancel the persistent notification
    // stopService(name);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }
}
