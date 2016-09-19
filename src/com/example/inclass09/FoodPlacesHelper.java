/**
 * Ajay Vijayakumaran Nair
 * A Yang
 * Nachiket Doke
 * InClass09
 */
package com.example.inclass09;

import java.util.List;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class FoodPlacesHelper {
	GoogleMap gMap;
	LatLngBounds.Builder builder = new LatLngBounds.Builder();
	LatLngBounds bound;

	public FoodPlacesHelper(GoogleMap gMap) {
		this.gMap = gMap;
	}

	public void placeMarkers(LatLng latlong) {
		gMap.clear();
		// CameraUpdateFactory
		// gMap.animateCamera(CameraUpdateFactory.zoomIn());
		ParseGeoPoint point = new ParseGeoPoint(latlong.latitude, latlong.longitude);
		ParseQuery<ParseObject> query = ParseQuery.getQuery("foodPlaces");
		// query.whereNear("location", point);
		query.whereWithinMiles("location", point, 50);
		query.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> arg0, ParseException arg1) {
				for (ParseObject obj : arg0) {
					ParseGeoPoint positionParse = obj.getParseGeoPoint("location");
					LatLng position = new LatLng(positionParse.getLatitude(), positionParse.getLongitude());
					Marker marker = gMap.addMarker(new MarkerOptions().position(position).title(obj.getString("name")));
					builder.include(position);
					marker.showInfoWindow();
				}
				if (arg0 != null && arg0.size() > 0) {
					gMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 25));
				}
			}
		});
	}
}
