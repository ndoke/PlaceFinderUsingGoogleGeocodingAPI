/**
 * Ajay Vijayakumaran Nair
 * A Yang
 * Nachiket Doke
 * InClass09
 */
package com.example.inclass09;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;

public class ParseApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();


    // Enable Local Datastore.
    Parse.enableLocalDatastore(this);

    // Add your initialization code here
    Parse.initialize(this, "I0dxsgF4EYNMfjvCdbqHiH3lIyqY8P1H7RIgHDML", "MbhUHCq0yiTXdO7iyPhkyobaFCMV16hHHUimW4ML");

    ParseUser.enableAutomaticUser();
    ParseACL defaultACL = new ParseACL();
    // Optionally enable public read access.
    // defaultACL.setPublicReadAccess(true);
    ParseUser.getCurrentUser().saveInBackground();
    ParseACL.setDefaultACL(defaultACL, true);
  }
}
