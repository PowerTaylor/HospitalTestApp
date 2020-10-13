@file:JvmName("ContextExtensions")

package com.example.core.extensions

import android.content.Context
import java.io.IOException

@Throws(IOException::class)
fun Context.readJsonAsset(fileName: String): String =
    assets.open(fileName).bufferedReader().use { it.readText() }
