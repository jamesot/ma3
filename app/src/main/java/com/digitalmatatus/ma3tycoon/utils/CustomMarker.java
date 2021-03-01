package com.digitalmatatus.ma3tycoon.utils;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

/**
 * Created by stephineosoro on 28/06/2017.
 */

public class CustomMarker extends Marker
{

    /*public CustomMarker(MapView mapView, ResourceProxy resourceProxy) {
        super(mapView, resourceProxy);
    }*/

    public CustomMarker(MapView mapView) {
        super(mapView);
    }

    public String name;
    public String desc;
}


/*
*
* package com.digitalmatatus.ma3tycoon.utils;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.OverlayItem;

/**
 * Created by stephineosoro on 28/06/2017.
 */

/*
public class CustomMarker extends OverlayItem
{

    */
/*public CustomMarker(MapView mapView, ResourceProxy resourceProxy) {
        super(mapView, resourceProxy);
    }*//*


   */
/* public CustomMarker(MapView mapView) {
//        super(mapView);
    }*//*


    public String name;
    public String desc;

    public CustomMarker(String aTitle, String aSnippet, IGeoPoint aGeoPoint) {
        super(aTitle, aSnippet, aGeoPoint);
    }
}
*/
