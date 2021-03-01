package com.digitalmatatus.ma3tycoon.utils;

import android.view.View;
import android.widget.Button;

import com.digitalmatatus.ma3tycoon.R;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow;

/**
 * Created by stephineosoro on 28/06/2017.
 */

public class CustomInfoWindow extends MarkerInfoWindow {
    /**
     * @param layoutResId layout that must contain these ids: bubble_title,bubble_description,
     *                    bubble_subdescription, bubble_image
     * @param mapView
     */
    /*public CustomInfoWindow(int layoutResId, MapView mapView) {
        super(layoutResId, mapView);
    }*/

    protected CustomMarker marker;

    public CustomInfoWindow(MapView mapView) {
        super(R.layout.bonuspack_bubble, mapView);
    }

    @Override
    public void onOpen(Object item) {
        marker = (CustomMarker) item;
        final Button btn = (Button) (mView.findViewById(R.id.bubble_moreinfo));
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (marker != null) {
                    if (marker.name != null && marker.desc != null) {
                       /* btn.setText(title);
                        btn.setDescription(desc);*/
                    }
                }
            }
        });
    }

}
