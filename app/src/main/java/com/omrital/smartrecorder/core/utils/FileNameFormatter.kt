package com.omrital.smartrecorder.core.utils

class FileNameFormatter {
    companion object {
        fun format(fileName: String): String {
            return fileName
                .replace(" ", "_")
                .replace(":", "_")
                .replace(",", "")
        }
    }
}
