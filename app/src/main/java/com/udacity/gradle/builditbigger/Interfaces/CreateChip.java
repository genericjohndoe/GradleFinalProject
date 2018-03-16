package com.udacity.gradle.builditbigger.Interfaces;

import com.udacity.gradle.builditbigger.Messaging.ComposeMessage.ComposeMessageFragment;
import com.udacity.gradle.builditbigger.Models.HilarityUser;

import java.util.List;

/**
 * INTERFACE  used
 */

public interface CreateChip {

    void addChipView(HilarityUser hu);

    List<ComposeMessageFragment.HilarityUserChip> getSelectedUsers();
}
