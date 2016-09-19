/**
 * Ajay Vijayakumaran Nair
 * A Yang
 * Nachiket Doke
 * InClass09
 */
package com.example.inclass09;

import java.io.IOException;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

	GoogleMap gMap;
	int textBoxId = 12345678;
	AlertDialog ad;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.showFoodPlaces) {

			// Use the Builder class for convenient dialog construction
			AlertDialog.Builder builder = new AlertDialog.Builder(this);

			final EditText input = new EditText(MainActivity.this);
			input.setId(textBoxId);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.MATCH_PARENT);
			input.setLayoutParams(lp);
			builder.setView(input);
			builder.setMessage("Enter a location").setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.dismiss();
				}
			}).setNegativeButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					String city = input.getText().toString();
					Log.d("ic", "city " + city);
					new GeoTask(MainActivity.this).execute(city);
				}
			});
			ad = builder.create();
			ad.show();

			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onMapReady(GoogleMap gMap) {
		this.gMap = gMap;
		gMap.setMyLocationEnabled(true);

	}

	class GeoTask extends AsyncTask<String, Void, List<Address>> {

		Context context;

		public GeoTask(Context context) {
			this.context = context;
		}

		@Override
		protected void onPostExecute(List<Address> result) {
			LatLng position;

			if (Geocoder.isPresent() && result != null) {
				Log.d("ic", "results returned : " + result.size());
				position = new LatLng(result.get(0).getLatitude(), result.get(0).getLongitude());

			} else {
				// geo coding is not supported in the device, hence hard coding
				// to charlotte, nc
				// for the time being
				position = new LatLng(35.2269444, -80.8433333);
			}
			FoodPlacesHelper helper = new FoodPlacesHelper(gMap);
			helper.placeMarkers(position);
			// super.onPostExecute(result);
		}

		@Override
		protected List<Address> doInBackground(String... params) {
			List<Address> address = null;
			Geocoder geoCoder = new Geocoder(context);
			try {
				address = geoCoder.getFromLocationName(params[0], 1);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return address;
		}

	}
}
