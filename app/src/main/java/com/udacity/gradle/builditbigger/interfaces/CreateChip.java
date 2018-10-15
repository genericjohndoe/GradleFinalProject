package com.udacity.gradle.builditbigger.interfaces;

import com.udacity.gradle.builditbigger.models.HilarityUser;

import java.util.List;

/**
 * INTERFACE  used
 */

public interface CreateChip {

    void addChipView(HilarityUser hu);

    List<HilarityUser> getSelectedUsers();

    void removeChipView(HilarityUser hu);
}
