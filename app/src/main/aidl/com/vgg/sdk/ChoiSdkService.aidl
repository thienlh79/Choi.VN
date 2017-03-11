// IChoiSdkService.aidl
package com.vgg.sdk;

// Declare any non-default types here with import statements

interface ChoiSdkService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);

    void onSdkAction(String result);
}
