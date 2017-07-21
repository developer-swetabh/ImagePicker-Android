package com.swetabh.imagepicker.multipleimageselection.presenterandview;

import android.support.v4.app.Fragment;

/**
 * Created by "swets" on 07/20/2017.
 */

public interface BaseActivityCommunicator {

    void updateTitle(String title);

    Fragment getCurrentFragment();
}
