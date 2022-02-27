package com.ssn.sxmusic.util

import com.ssn.sxmusic.model.Song

interface OnClickItem {
    fun onClickListener(Song: Song)
}