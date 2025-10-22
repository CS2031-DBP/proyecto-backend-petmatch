package org.example.petmatch.GoogleApi;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GoogleMapsService {

    @Value("${google.maps.api.key}")
    private String apiKey;

    private GeoApiContext context;

    private GeoApiContext getContext() {
        if (context == null) {
            context = new GeoApiContext.Builder().apiKey(apiKey).build();
        }
        return context;
    }

    /** ✅ Validate address using Geocoding API */
    public boolean isValidAddress(String address) {
        try {
            GeocodingResult[] results = GeocodingApi.geocode(getContext(), address).await();
            return results != null && results.length > 0;
        } catch (Exception e) {
            return false;
        }
    }

    /** ✅ Get latitude & longitude for an address */
    public LatLng getCoordinates(String address) throws Exception {
        GeocodingResult[] results = GeocodingApi.geocode(getContext(), address).await();
        if (results.length == 0) throw new Exception("Address not found");
        return results[0].geometry.location;
    }

    /** ✅ Compute distance between two coordinates (Haversine formula) */
    public double distanceKm(LatLng a, LatLng b) {
        double R = 6371;
        double dLat = Math.toRadians(b.lat - a.lat);
        double dLng = Math.toRadians(b.lng - a.lng);
        double rLat1 = Math.toRadians(a.lat);
        double rLat2 = Math.toRadians(b.lat);
        double x = Math.sin(dLat/2)*Math.sin(dLat/2) +
                Math.sin(dLng/2)*Math.sin(dLng/2)*Math.cos(rLat1)*Math.cos(rLat2);
        return 2 * R * Math.atan2(Math.sqrt(x), Math.sqrt(1 - x));
    }
}
