package org.mockito.configuration;

/**
 * Added to clear Objenesis cache and prevent Robolectric crash
 */
public class MockitoConfiguration extends DefaultMockitoConfiguration {

    @Override
    public boolean enableClassCache() {
        return false;
    }
}