package com.udacity.gradle.builditbigger;

import android.app.Activity;

import org.junit.Test;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by joeljohnson on 2/26/17.
 */
public class EndpointsAsyncTaskTest extends Activity {

    @Test
    public void doInBackground() throws Exception {
       EndpointsAsyncTask task =  new EndpointsAsyncTask();
               task.execute(this);
        String output = task.get();
        assertNotNull(output);
    }


}