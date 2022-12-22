/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ofd.heartbeat

import android.content.ComponentName
import android.content.Context
import android.graphics.drawable.Icon
import android.util.Log
import androidx.wear.watchface.complications.data.*
import androidx.wear.watchface.complications.datasource.ComplicationDataSourceUpdateRequester
import androidx.wear.watchface.complications.datasource.ComplicationRequest
import androidx.wear.watchface.complications.datasource.SuspendingComplicationDataSourceService
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger


class MoonPhaseProviderService : SuspendingComplicationDataSourceService() {


    companion object {
        private const val TAG = "MoonPhaseProviderService"
        val uctr = AtomicInteger(0)
        val sdf = SimpleDateFormat("MM/dd/yyyy HH:mm", Locale.US)
        val newmoon = sdf.parse("01/02/2022 18:35").time
        val period = 29.53058770576
        val dayms = TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS).toDouble()
    }

    var tt : TimerTask? = null

    class TT(var applicationContext: Context) : TimerTask() {
        override fun run() {
            uctr.incrementAndGet()

            Log.d(TAG, "Update:"+uctr.get())

            val component = ComponentName(
                "com.ofd.heartbeat",
                "com.ofd.heartbeat.MoonPhaseProviderService"
            )

            val updater = ComplicationDataSourceUpdateRequester.create(
                applicationContext,
                component
            )
            updater.requestUpdateAll()
        }

    }
    /*
     * Called when a complication has been activated. The method is for any one-time
     * (per complication) set-up.
     *
     * You can continue sending data for the active complicationId until onComplicationDeactivated()
     * is called.
     */
    override fun onComplicationActivated(
        complicationInstanceId: Int,
        type: ComplicationType
    ) {
        Log.d(TAG, "onComplicationActivated(): $complicationInstanceId")
        if(tt==null){
            tt = TT(applicationContext)
            Timer().scheduleAtFixedRate(tt, 0, 2000)
        }
    }

    /*
     * A request for representative preview data for the complication, for use in the editor UI.
     * Preview data is assumed to be static per type. E.g. for a complication that displays the
     * date and time of an event, rather than returning the real time it should return a fixed date
     * and time such as 10:10 Aug 1st.
     *
     * This will be called on a background thread.
     */
    override fun getPreviewData(type: ComplicationType): ComplicationData {
        return ShortTextComplicationData.Builder(
            text = PlainComplicationText.Builder(text = "6?").build(),
            contentDescription = PlainComplicationText.Builder(text = "Short Text version of Number.")
                .build()
        )
            .setTapAction(null)
            .build()
    }

    var moons = arrayOf(
        R.drawable.moon0,
        R.drawable.moon1,
        R.drawable.moon2,
        R.drawable.moon3,
        R.drawable.moon4,
        R.drawable.moon5,
        R.drawable.moon6,
        R.drawable.moon7,
        R.drawable.moon8,
        R.drawable.moon9,
        R.drawable.moon10,
        R.drawable.moon11,
        R.drawable.moon12,
        R.drawable.moon13,
        R.drawable.moon14,
        R.drawable.moon15,
        R.drawable.moon16,
        R.drawable.moon17,
        R.drawable.moon18,
        R.drawable.moon19,
        R.drawable.moon20,
        R.drawable.moon21,
        R.drawable.moon22,
        R.drawable.moon23,
        R.drawable.moon24, // extra new moon
    )

    override suspend fun onComplicationRequest(request: ComplicationRequest): ComplicationData? {

        val now = System.currentTimeMillis()
        val gap = now - newmoon
        val days = gap.toDouble() / dayms
        val period = days / period
        val remperiod = period - period.toInt().toDouble()
        val offset = (remperiod * (moons.size-1) + .5).toInt()
        Log.d(TAG, "onComplicationRequest(): ${offset} "+ request.complicationType)

        var fullmoon = Icon.createWithResource(
            this,
            moons[offset]
//                    moons[uctr.get().mod(24)]
        )

        return when (request.complicationType) {

            ComplicationType.PHOTO_IMAGE ->
                PhotoImageComplicationData.Builder(
                    fullmoon,
                    PlainComplicationText.Builder("fullmoon").build()
                ).build()

            ComplicationType.SMALL_IMAGE ->
                SmallImageComplicationData.Builder(
                    SmallImage.Builder(fullmoon, SmallImageType.PHOTO).build(),
                    PlainComplicationText.Builder("fullmoon").build()
                ).build()

            else -> {
                Log.w(TAG, "Unexpected complication type ${request.complicationType}")
                null
            }
        }
    }

    override fun onComplicationDeactivated(complicationInstanceId: Int) {
        Log.d(TAG, "Deactivated")
        if(tt != null){
            tt!!.cancel()
            tt=null
        }
    }

    override fun onCreate() {
        Log.d(TAG, "onCreate")
        super.onCreate()
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy")
        super.onDestroy()
    }
}
