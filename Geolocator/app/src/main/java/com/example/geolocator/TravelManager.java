package com.example.geolocator;

import android.location.Location;

public class TravelManager {
    public static float DEFAULT_SPEED = 60.0f; // km/h
    public static int METERS_PER_KM = 1000;

    private Location destination;

    public void setDestination( Location newDestination ) {
        destination = newDestination;
    }

    // returns distance from current to destination in meters
    public float distanceToDestination( Location current ) {
        if( current!= null && destination != null )
            return current.distanceTo( destination );
        else
            return -1.0f;
    }

    // returns distance from current to destination in km
    public String kmToDestination( Location current ) {
        int distance =
                ( int ) ( distanceToDestination( current ) / METERS_PER_KM );
        if( distance > 1 )
            return distance + " km";
        else
            return "0 km";
    }

    // returns time from current to destination in hours and minutes
    public String timeToDestination( Location current ) {
        // # of meters from current location to destination
        float metersToGo = distanceToDestination( current );
        // hours from current location to destination at default speed
        float timeToGo = metersToGo / ( DEFAULT_SPEED * METERS_PER_KM );

        String result = "";
        int hours = ( int ) timeToGo;
        if( hours == 1 )
            result += "1 hour ";
        else if( hours > 1 )
            result += hours + " hours ";
        int minutes = ( int ) ( ( timeToGo - hours ) * 60 );
        if( minutes == 1 )
            result += "1 minute ";
        else if( minutes > 1 )
            result += minutes + " minutes";
        if( hours <= 0 && minutes <= 0 )
            result = "less than a minute left";
        return result;
    }
}
