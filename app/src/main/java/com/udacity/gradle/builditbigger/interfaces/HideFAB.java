package com.udacity.gradle.builditbigger.interfaces;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

/**
 * INTERFACE for manipulating FAB
 */

public interface HideFAB {
    void showFAB();
    void hideFAB();
    FloatingActionButton getFAB();
    FloatingActionMenu getFAM();
}
