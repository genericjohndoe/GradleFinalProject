package com.udacity.gradle.builditbigger;

import org.junit.Test;

import static junit.framework.Assert.assertNotNull;



import static android.support.test.InstrumentationRegistry.getContext;

/**
 * Created by joeljohnson on 2/26/17.
 */
public class EndpointsAsyncTaskTest {

    static public String OUTPUT;
    @Test
    public void doInBackground() throws Exception {
       EndpointsAsyncTask task =  new EndpointsAsyncTask();
               task.execute(new Pair(getContext(),"jay"));
        task.get();
        assertNotNull(OUTPUT);
    }


}