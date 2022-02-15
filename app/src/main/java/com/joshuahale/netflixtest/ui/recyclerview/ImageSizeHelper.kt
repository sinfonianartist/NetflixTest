package com.joshuahale.netflixtest.ui.recyclerview

import android.content.res.Resources
import com.joshuahale.netflixtest.constants.MoviesConstants

class ImageSizeHelper {

    companion object {

        fun backdropSize(sizes: List<String>): String {
            val backdropWidth = Resources.getSystem().displayMetrics.widthPixels
            for (size in sizes) {
                val digits = size.filter { it.isDigit() }.toInt()
                if (digits > backdropWidth) return size
            }
            return sizes[sizes.lastIndex]
        }

        fun posterSize(sizes: List<String>): String {
            val posterSize = Resources
                .getSystem().displayMetrics.widthPixels / MoviesConstants.NUMBER_OF_POSTER_COLUMNS
            for (size in sizes) {
                val digits = size.filter { it.isDigit() }.toInt()
                if (digits > posterSize) return size
            }
            return sizes[sizes.lastIndex]
        }
    }
}