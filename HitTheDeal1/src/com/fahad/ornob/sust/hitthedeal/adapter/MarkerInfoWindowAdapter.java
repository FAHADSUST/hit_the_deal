package com.fahad.ornob.sust.hitthedeal.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.fahad.ornob.sust.hitthedeal.R;
import com.fahad.ornob.sust.hitthedeal.app.AppController;
import com.fahad.ornob.sust.hitthedeal.contants.Constants;
import com.fahad.ornob.sust.hitthedeal.item.CombinedEventAndUser;
import com.fahad.ornob.sust.hitthedeal.item.Event;
import com.fahad.ornob.sust.hitthedeal.item.UserItem;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;

public class MarkerInfoWindowAdapter implements GoogleMap.InfoWindowAdapter
{
	private LayoutInflater inflater;
	Context context;
	HashMap<Marker , CombinedEventAndUser> mMarkersHashMap;
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();
	
    public MarkerInfoWindowAdapter(Context context, HashMap<Marker , CombinedEventAndUser> mMarkersHashMap )
    {
    	this.context=context;
    	this.mMarkersHashMap=mMarkersHashMap;
    	//this.myMarkers=myMarkers;
    }

    @Override
    public View getInfoWindow(Marker marker)
    {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker)
    {
    	View convertView=null;
    	if (inflater == null)
			inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null)
			convertView = inflater.inflate(R.layout.infowindow_layout, null);

		if (imageLoader == null)
			imageLoader = AppController.getInstance().getImageLoader();
    	
        
        //MyMarker myMarker = myMarkers.get(marker);
        CombinedEventAndUser combinedEventAndUser = mMarkersHashMap.get(marker);
        
        
        UserItem userItem = combinedEventAndUser.getUserItem();
        Event event = combinedEventAndUser.getEvent();

        ImageView markerIcon = (ImageView) convertView.findViewById(R.id.marker_icon);
        
        TextView markerLabel = (TextView)convertView.findViewById(R.id.marker_label);

        TextView anotherLabel = (TextView)convertView.findViewById(R.id.another_label);

       // markerIcon.setImageResource(manageMarkerIcon(myMarker.getmIcon()));

        markerLabel.setText(event.getEvent_name());
        anotherLabel.setText(userItem.getUser_name());
        markerIcon.setImageResource(Constants.iconMarkerShowInfoType[userItem.getCreator_type_id()-1]);

        return convertView;
    }
}