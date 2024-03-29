package com.itacademy.utils

import android.net.Uri

sealed class NavCommands {
    data class Browser(val url: String) : NavCommands()
    data class DeepLink(val url: Uri,
                        val isModal: Boolean,
                        val isSingleTop: Boolean = false
    ) : NavCommands()
}
