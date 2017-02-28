package com.kajak.findafeast;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;
import com.yelp.clientlib.connection.YelpAPI;
import com.yelp.clientlib.connection.YelpAPIFactory;

import static org.junit.Assert.*;

/**
 * Created by James on 2/26/2017.
 *
 */

public class YelpAPITest {

    private YelpAPI yelp_api;

    @Before
    public void setup_yelp_api() {
        Context appContext = InstrumentationRegistry.getTargetContext();

        YelpAPIFactory yelp_api_factory = new YelpAPIFactory(
                appContext.getString(R.string.consumerKey),
                appContext.getString(R.string.consumerSecret),
                appContext.getString(R.string.token),
                appContext.getString(R.string.tokenSecret));

        yelp_api = yelp_api_factory.createAPI();
    }

    // Test for non-null YelpAPI object
    @Test
    public void is_valid_yelp_api() {
        assertNotNull(yelp_api);
    }

}
