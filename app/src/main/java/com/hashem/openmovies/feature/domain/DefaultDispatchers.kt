package com.hashem.openmovies.feature.domain

import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

interface Dispatchers {
    fun io(): CoroutineDispatcher
    fun main(): CoroutineDispatcher
}

class DefaultDispatchers @Inject constructor(
) : Dispatchers {
    override fun io(): CoroutineDispatcher = kotlinx.coroutines.Dispatchers.IO
    override fun main(): CoroutineDispatcher = kotlinx.coroutines.Dispatchers.Main
}