package com.udacity.gradle.builditbigger.models

/**
 * A class used to represent the country the user is from
 *
 * @property twoDigit is the two digit code used to refer to a country
 * @property emoji is the string representation of the nation's flag
 */

data class Country(val twoDigit: String, val emoji: String)