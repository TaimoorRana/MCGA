package com.concordia.mcga.factories;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;

/**
 * Dummy {@link BitmapDescriptorFactory}, used to run unit tests on methods.
 */
@Implements(BitmapDescriptorFactory.class)
public class ShadowBitmapDescriptorFactory {
    @Implementation
    public static BitmapDescriptor fromResource(int i){
        return null;
    }
}
