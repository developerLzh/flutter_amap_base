package me.yohom.amapbase.map

import android.content.Intent
import android.graphics.Bitmap
import android.os.Handler
import com.amap.api.maps.AMap
import com.amap.api.maps.AMapUtils
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.CoordinateConverter
import com.amap.api.maps.model.*
import com.amap.api.maps.offlinemap.OfflineMapActivity
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import me.yohom.amapbase.AMapBasePlugin
import me.yohom.amapbase.AMapBasePlugin.Companion.registrar
import me.yohom.amapbase.MapMethodHandler
import me.yohom.amapbase.common.log
import me.yohom.amapbase.common.parseFieldJson
import me.yohom.amapbase.common.toFieldJson
import me.yohom.amapbase.map.adapter.ArriveStartWindowAdapter
import me.yohom.amapbase.map.adapter.DisTimeWindowAdapter
import me.yohom.amapbase.map.adapter.WaitAcceptAdapter
import me.yohom.amapbase.map.marker.MySmoothMarker
import me.yohom.amapbase.map.marker.SimpleLoc
import me.yohom.amapbase.map.wave.MarkerWave
import java.io.*
import java.text.DecimalFormat
import java.util.*

val beijingLatLng = LatLng(39.941711, 116.382248)

object SetCustomMapStyleID : MapMethodHandler {
    private lateinit var map: AMap

    override fun with(map: AMap): MapMethodHandler {
        this.map = map
        return this
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        val styleId = call.argument("styleId") ?: ""

        log("方法map#setCustomMapStyleID android端参数: styleId -> $styleId")

        map.setCustomMapStyleID(styleId)

        result.success(success)
    }
}

object SetCustomMapStylePath : MapMethodHandler {

    private lateinit var map: AMap

    override fun with(map: AMap): SetCustomMapStylePath {
        this.map = map
        return this
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        val path = call.argument("path") ?: ""

        log("方法map#setCustomMapStylePath android端参数: path -> $path")

        var outputStream: FileOutputStream? = null
        var inputStream: InputStream? = null
        val filePath: String?
        try {
            inputStream = registrar.context().assets.open(registrar.lookupKeyForAsset(path))
            val b = ByteArray(inputStream!!.available())
            inputStream.read(b)

            filePath = registrar.context().filesDir.absolutePath
            val file = File("$filePath/$path")
            if (file.exists()) {
                file.delete()
            }

            if (!file.parentFile.exists()) {
                file.parentFile.mkdirs()
            }
            file.createNewFile()
            outputStream = FileOutputStream(file)
            outputStream.write(b)
        } catch (e: IOException) {
            result.error(e.message, e.localizedMessage, e.printStackTrace())
            return
        } finally {
            inputStream?.close()
            outputStream?.close()
        }

        map.setCustomMapStylePath("$filePath/$path")

        result.success(success)
    }
}

object SetMapCustomEnable : MapMethodHandler {

    private lateinit var map: AMap

    override fun with(map: AMap): SetMapCustomEnable {
        this.map = map
        return this
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        val enabled = call.argument("enabled") ?: false

        log("方法map#setMapCustomEnable android端参数: enabled -> $enabled")

        map.setMapCustomEnable(enabled)

        result.success(success)
    }
}

object ConvertCoordinate : MapMethodHandler {

    lateinit var map: AMap

    private val types = arrayListOf(
            CoordinateConverter.CoordType.GPS,
            CoordinateConverter.CoordType.BAIDU,
            CoordinateConverter.CoordType.MAPBAR,
            CoordinateConverter.CoordType.MAPABC,
            CoordinateConverter.CoordType.SOSOMAP,
            CoordinateConverter.CoordType.ALIYUN,
            CoordinateConverter.CoordType.GOOGLE
    )

    override fun with(map: AMap): ConvertCoordinate {
        this.map = map
        return this
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        val lat = call.argument<Double>("lat")!!
        val lon = call.argument<Double>("lon")!!
        val typeIndex = call.argument<Int>("type")!!
        val amapCoordinate = CoordinateConverter(AMapBasePlugin.registrar.context())
                .from(types[typeIndex])
                .coord(LatLng(lat, lon, false))
                .convert()

        result.success(amapCoordinate.toFieldJson())
    }
}

object CalcDistance : MapMethodHandler {
    lateinit var map: AMap

    override fun with(map: AMap): CalcDistance {
        this.map = map
        return this
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        val p1 = call.argument<Map<String, Any>>("p1")
        val p2 = call.argument<Map<String, Any>>("p2")
        val latlng1 = p1!!.getLntlng()
        val latlng2 = p2!!.getLntlng()
        val dis = AMapUtils.calculateLineDistance(latlng1, latlng2)
        result.success(dis)
    }

    private fun Map<String, Any>.getLntlng(): LatLng {
        val lat = get("latitude") as Double
        val lng = get("longitude") as Double
        return LatLng(lat, lng)
    }
}

object ClearMap : MapMethodHandler {

    lateinit var map: AMap

    override fun with(map: AMap): ClearMap {
        this.map = map
        return this
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        map.clear()

        result.success(success)
    }
}

object OpenOfflineManager : MapMethodHandler {

    override fun with(map: AMap): MapMethodHandler {
        return this
    }

    override fun onMethodCall(p0: MethodCall, p1: MethodChannel.Result) {
        AMapBasePlugin.registrar.activity().startActivity(
                Intent(AMapBasePlugin.registrar.activity(),
                        OfflineMapActivity::class.java)
        )
    }
}

object SetLanguage : MapMethodHandler {
    lateinit var map: AMap

    override fun with(map: AMap): SetLanguage {
        this.map = map
        return this
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        val language = call.argument<String>("language") ?: "0"

        log("方法map#setLanguage android端参数: language -> $language")

        map.setMapLanguage(language)

        result.success(success)
    }
}

object SetMapType : MapMethodHandler {

    lateinit var map: AMap

    override fun with(map: AMap): SetMapType {
        this.map = map
        return this
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        val mapType = call.argument<Int>("mapType") ?: 1

        log("方法map#setMapType android端参数: mapType -> $mapType")

        map.mapType = mapType

        result.success(success)
    }
}

object SetMyLocationStyle : MapMethodHandler {

    lateinit var map: AMap

    override fun with(map: AMap): SetMyLocationStyle {
        this.map = map
        return this
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        val styleJson = call.argument<String>("myLocationStyle") ?: "{}"

        log("方法setMyLocationEnabled android端参数: styleJson -> $styleJson")

        styleJson.parseFieldJson<UnifiedMyLocationStyle>().applyTo(map)

        result.success(success)
    }
}

object SetUiSettings : MapMethodHandler {

    lateinit var map: AMap

    override fun with(map: AMap): SetUiSettings {
        this.map = map
        return this
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        val uiSettingsJson = call.argument<String>("uiSettings") ?: "{}"

        log("方法setUiSettings android端参数: uiSettingsJson -> $uiSettingsJson")

        uiSettingsJson.parseFieldJson<UnifiedUiSettings>().applyTo(map)

        result.success(success)
    }
}

object ShowIndoorMap : MapMethodHandler {

    lateinit var map: AMap

    override fun with(map: AMap): ShowIndoorMap {
        this.map = map
        return this
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        val enabled = call.argument<Boolean>("showIndoorMap") ?: false

        log("方法map#showIndoorMap android端参数: enabled -> $enabled")

        map.showIndoorMap(enabled)

        result.success(success)
    }
}

object AddMarker : MapMethodHandler {

    lateinit var map: AMap

    override fun with(map: AMap): AddMarker {
        this.map = map
        return this
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        val optionsJson = call.argument<String>("markerOptions") ?: "{}"

        log("方法marker#addMarker android端参数: optionsJson -> $optionsJson")

        optionsJson.parseFieldJson<UnifiedMarkerOptions>().applyTo(map)

        result.success(success)
    }
}

object AddMarkers : MapMethodHandler {

    lateinit var map: AMap

    override fun with(map: AMap): AddMarkers {
        this.map = map
        return this
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        val moveToCenter = call.argument<Boolean>("moveToCenter") ?: true
        val optionsListJson = call.argument<String>("markerOptionsList") ?: "[]"
        val clear = call.argument<Boolean>("clear") ?: false

        log("方法marker#addMarkers android端参数: optionsListJson -> $optionsListJson")
        log("方法marker#addMarkers android端参数: moveToCenter -> $moveToCenter")

        val optionsList = ArrayList(optionsListJson.parseFieldJson<List<UnifiedMarkerOptions>>().map { it.toMarkerOption() })
        if (clear) map.mapScreenMarkers.forEach { it.remove() }
        map.addMarkers(optionsList, moveToCenter)

        result.success(success)
    }
}

object AddPolyline : MapMethodHandler {
    lateinit var map: AMap

    var lastPolyline: Polyline? = null

    override fun with(map: AMap): MapMethodHandler {
        this.map = map
        return this
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        val options = call.argument<String>("options")?.parseFieldJson<UnifiedPolylineOptions>()
        val clearLast = call.argument<Boolean>("clearLast")

        log("map#AddPolyline android端参数: options -> $options")

        val temp = options?.applyTo(map)

        if (clearLast!!) {
            lastPolyline?.remove()
        }

        lastPolyline = temp

        result.success(success)
    }
}

object ClearMarker : MapMethodHandler {

    lateinit var map: AMap

    override fun with(map: AMap): ClearMarker {
        this.map = map
        return this
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        map.mapScreenMarkers.forEach { it.remove() }

        result.success(success)
    }
}

object ChangeLatLng : MapMethodHandler {

    lateinit var map: AMap

    override fun with(map: AMap): ChangeLatLng {
        this.map = map
        return this
    }

    override fun onMethodCall(methodCall: MethodCall, methodResult: MethodChannel.Result) {
        val targetJson = methodCall.argument<String>("target") ?: "{}"

        map.animateCamera(CameraUpdateFactory.changeLatLng(targetJson.parseFieldJson<LatLng>()))

        methodResult.success(success)
    }
}

object GetCenterLnglat : MapMethodHandler {
    lateinit var map: AMap
    override fun with(map: AMap): MapMethodHandler {
        this.map = map
        return this
    }

    override fun onMethodCall(methodCall: MethodCall, methodResult: MethodChannel.Result) {
        val target = map.cameraPosition.target
        methodResult.success(target.toFieldJson())
    }
}

object SetMapStatusLimits : MapMethodHandler {

    lateinit var map: AMap

    override fun with(map: AMap): SetMapStatusLimits {
        this.map = map
        return this
    }

    override fun onMethodCall(methodCall: MethodCall, methodResult: MethodChannel.Result) {
        val swLatLng: LatLng? = methodCall.argument<String>("swLatLng")?.parseFieldJson()
        val neLatLng: LatLng? = methodCall.argument<String>("neLatLng")?.parseFieldJson()

        map.setMapStatusLimits(LatLngBounds(swLatLng, neLatLng))

        methodResult.success(success)
    }
}

object SetPosition : MapMethodHandler {

    lateinit var map: AMap

    override fun with(map: AMap): SetPosition {
        this.map = map
        return this
    }

    override fun onMethodCall(methodCall: MethodCall, methodResult: MethodChannel.Result) {
        val target: LatLng = methodCall.argument<String>("target")?.parseFieldJson()
                ?: beijingLatLng
        val zoom: Double = methodCall.argument<Double>("zoom") ?: 10.0
        val tilt: Double = methodCall.argument<Double>("tilt") ?: 0.0
        val bearing: Double = methodCall.argument<Double>("bearing") ?: 0.0

        map.moveCamera(CameraUpdateFactory.newCameraPosition(CameraPosition(target, zoom.toFloat(), tilt.toFloat(), bearing.toFloat())))

        methodResult.success(success)
    }
}

object SetZoomLevel : MapMethodHandler {

    lateinit var map: AMap

    override fun with(map: AMap): SetZoomLevel {
        this.map = map
        return this
    }

    override fun onMethodCall(methodCall: MethodCall, methodResult: MethodChannel.Result) {
        val zoomLevel = methodCall.argument<Int>("zoomLevel") ?: 15

        map.moveCamera(CameraUpdateFactory.zoomTo(zoomLevel.toFloat()))

        methodResult.success(success)
    }
}

object ZoomToSpan : MapMethodHandler {

    lateinit var map: AMap

    override fun with(map: AMap): ZoomToSpan {
        this.map = map
        return this
    }

    override fun onMethodCall(methodCall: MethodCall, methodResult: MethodChannel.Result) {
        val boundJson = methodCall.argument<String>("bound") ?: "[]"
        val paddingLeft = methodCall.argument<Int>("paddingLeft") ?: 80
        val paddingRight = methodCall.argument<Int>("paddingRight") ?: 80
        val paddingTop = methodCall.argument<Int>("paddingTop") ?: 80
        val paddingBottom = methodCall.argument<Int>("paddingBottom") ?: 80

        map.moveCamera(CameraUpdateFactory.newLatLngBoundsRect(
                LatLngBounds.builder().run {
                    boundJson.parseFieldJson<List<LatLng>>().forEach {
                        include(it)
                    }
                    build()
                },
                paddingLeft, paddingRight, paddingTop, paddingBottom
        ))

        methodResult.success(success)
    }
}

object ScreenShot : MapMethodHandler {
    lateinit var map: AMap
    override fun with(map: AMap): MapMethodHandler {
        this.map = map
        return this
    }

    override fun onMethodCall(methodCall: MethodCall, methodResult: MethodChannel.Result) {
        map.getMapScreenShot(object : AMap.OnMapScreenShotListener {
            override fun onMapScreenShot(bitmap: Bitmap?) {
            }

            override fun onMapScreenShot(bitmap: Bitmap?, status: Int) {
                if (bitmap == null) {
                    methodResult.error("截图失败", null, null)
                    return
                }
                if (status != 0) {
                    val outputStream = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                    methodResult.success(outputStream.toByteArray())
                } else {
                    methodResult.error("截图失败,渲染未完成", "截图失败,渲染未完成", null)
                }
            }
        })
    }
}

/**
 * 绘制面
 */
object AddPolygon : MapMethodHandler {
    lateinit var map: AMap

    override fun with(map: AMap): MapMethodHandler {
        this.map = map
        return this
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        val options = call.argument<String>("options")?.parseFieldJson<UnifiedPolygonOptions>()

        log("map#AddPolygon android端参数: options -> $options")

        options?.applyTo(map)

        result.success(success)
    }
}

/**
 * 可以平滑移动的marker
 */
object SmoothMarker : MapMethodHandler {
    lateinit var map: AMap
    private var smoothMoveMarker: MySmoothMarker? = null

    private var handler: Handler? = null
    var timer: Timer? = null
    var timerTask: TimerTask? = null

    var sec = 0

    private fun initTimer() {
        timer?.cancel()
        timerTask?.cancel()
        timer = Timer()
        timerTask = object : TimerTask() {
            override fun run() {
                sec--
                if (sec <= 0) {
                    sec = 0
                }
                handler?.sendEmptyMessage(0)
            }
        }
        timer?.schedule(timerTask, 0, 1000)
    }

    override fun with(map: AMap): MapMethodHandler {
        this.map = map
        return this
    }

    private fun getLeftTimeStr(time: Int): String {
        val hour = time / 60 / 60
        val minute = time / 60 % 60
        val leftTime: String
        if (hour > 0) {
            leftTime = "${hour}时${minute}分"
        } else {
            leftTime = "${minute}分"
        }
        return leftTime
    }

    private fun getLeftDisStr(dis: Int): String {
        val km = dis / 1000
        val leftDis: String
        leftDis = if (km >= 1) {
            val disKm = DecimalFormat("#0.0").format(dis.toDouble() / 1000)
            "${disKm}千米"
        } else {
            "${dis}米"
        }
        return leftDis
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        when (call.method) {
            "marker#setSmoothMarker" -> {
                val optionsJson = call.argument<String>("markerOptions") ?: "{}"
                log("marker#setSmoothMarker android端参数: markerOptions -> $optionsJson")
                val temp = optionsJson.parseFieldJson<UnifiedMarkerOptions>()

                smoothMoveMarker = MySmoothMarker(map, temp.applyTo(map))
                smoothMoveMarker?.startMove(temp.position, 0, true)
                log("smoothMarker == null ? ${smoothMoveMarker == null}")
            }
            "marker#moveSmoothMarker" -> {

                val simpleLoc = call.argument<String>("simpleLoc")?.parseFieldJson<SimpleLoc>()
                log("marker#moveSmoothMarker android端参数: simpleLoc -> ${simpleLoc.toString()}")

                val latLng = LatLng(simpleLoc!!.lat, simpleLoc.lng)
                smoothMoveMarker?.startMove(latLng, 3000, true)
                val marker = smoothMoveMarker!!.marker
                if (null != marker) {
                    marker.rotateAngle = 360.0f - simpleLoc.bearing + map.cameraPosition.bearing
                    marker.isDraggable = false
                    marker.isInfoWindowEnable = true
                    marker.isClickable = false
                    marker.setAnchor(0.5f, 0.5f)
                }
            }
            "marker#removeSmoothMarker" -> {
                smoothMoveMarker?.destory()
                smoothMoveMarker = null
            }
            "marker#arriveStartSmoothMarker" -> {
                val optionsJson = call.argument<String>("markerOptions") ?: "{}"
                log("marker#setSmoothMarker android端参数: markerOptions -> $optionsJson")
                val temp = optionsJson.parseFieldJson<UnifiedMarkerOptions>()

                smoothMoveMarker = MySmoothMarker(map, temp.applyTo(map))
                smoothMoveMarker?.startMove(temp.position, 0, true)
                log("smoothMarker == null ? ${smoothMoveMarker == null}")
                //到达起点后的倒计时
                this.map.setInfoWindowAdapter(ArriveStartWindowAdapter(AMapView.ctx))
                val arriveTime: Int = call.argument<Int>("arriveTime")
                        ?: ((System.currentTimeMillis() / 1000).toInt())
                val bookTime: Int = call.argument<Int>("bookTime")
                        ?: ((System.currentTimeMillis() / 1000).toInt())
                sec = bookTime - arriveTime
                handler = Handler {
                    if (it.what == 0) {
                        val min = sec / 60
                        val sec = sec % 60
                        val minString: String = if (min > 9) {
                            "$min"
                        } else {
                            "0$min"
                        }
                        val secString: String = if (sec > 9) {
                            "$sec"
                        } else {
                            "0$sec"
                        }

                        val marker = smoothMoveMarker!!.marker
                        marker.isInfoWindowEnable = true
                        marker?.title = "$minString:$secString"
                        marker?.showInfoWindow()

                        if (this@SmoothMarker.sec == 0) {
                            timer?.cancel()
                            timerTask?.cancel()
                        }
                    }
                    initTimer()
                    return@Handler true
                }
                initTimer()
            }
            "marker#countDownSmoothMarker" -> {
                this.map.setInfoWindowAdapter(ArriveStartWindowAdapter(AMapView.ctx))
                val leftSec: Int = call.argument<Int>("leftSec") ?: 0
                val min = leftSec / 60
                val sec = leftSec % 60
                val minString: String = if (min > 9) {
                    "$min"
                } else {
                    "0$min"
                }
                val secString: String = if (sec > 9) {
                    "$sec"
                } else {
                    "0$sec"
                }

                val marker = smoothMoveMarker!!.marker
                marker.isInfoWindowEnable = true
                marker?.title = "$minString:$secString"
                marker?.showInfoWindow()
            }
            "marker#showLeftSmoothMarker" -> {
                val dis: Int = call.argument<Int>("dis") ?: 0
                val time: Int = call.argument<Int>("time") ?: 0
                val des: String = call.argument<String>("des") ?: "剩余"

                this.map.setInfoWindowAdapter(DisTimeWindowAdapter(AMapView.ctx, des))
                smoothMoveMarker?.marker!!.title = getLeftTimeStr(time)
                smoothMoveMarker?.marker!!.snippet = getLeftDisStr(dis)
                smoothMoveMarker?.marker!!.showInfoWindow()

            }
        }

        result.success(success)
    }
}

/**
 * 绘制面
 */
object IsInGeoArea : MapMethodHandler {
    lateinit var map: AMap

    override fun with(map: AMap): MapMethodHandler {
        this.map = map
        return this
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        val target = call.argument<String>("target")?.parseFieldJson<LatLng>()
        val latlngs = call.argument<String>("area")?.parseFieldJson<List<LatLng>>()

        val options = PolygonOptions()
        if (latlngs != null) {
            for (i in latlngs) {
                options.add(i)
            }
        }
        options.visible(false) //设置区域是否显示
        val polygon = map.addPolygon(options)
        val contains = polygon.contains(target)
        polygon.remove()

        result.success(contains)
    }
}

/**
 * 波纹扩散动画
 */
object WaveAnimation : MapMethodHandler {
    lateinit var map: AMap
    lateinit var markerWave: MarkerWave

    override fun with(map: AMap): MapMethodHandler {
        this.map = map
        markerWave = MarkerWave()
        return this
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        val methodName = call.method
        if (methodName == "map#addWaveAnimation") {
            val optionsJson = call.argument<String>("latlng") ?: "{}"
            val latlng = optionsJson.parseFieldJson<LatLng>()

            val fillColor = call.argument<String>("waveFillColor") ?: "#FF9220"
            val strokeColor = call.argument<String>("waveStrokeColor") ?: "#FF9220"
            markerWave.addWaveAnimation(latlng, map, fillColor, strokeColor)
        } else if (methodName == "map#removeWaveAnimation") {
            markerWave.removeCircleWave()
        }

        result.success(success)
    }
}

object WaitAcceptMarker : MapMethodHandler {
    lateinit var map: AMap

    private var handler: Handler? = null
    private var marker: Marker? = null

    var timer: Timer? = null
    var timerTask: TimerTask? = null

    var sec = 0

    private fun initTimer() {
        timer?.cancel()
        timerTask?.cancel()
        timer = Timer()
        timerTask = object : TimerTask() {
            override fun run() {
                sec++
                handler?.sendEmptyMessage(0)
            }
        }
        timer?.schedule(timerTask, 0, 1000)
    }

    override fun with(map: AMap): MapMethodHandler {
        this.map = map
        this.map.setInfoWindowAdapter(WaitAcceptAdapter(AMapView.ctx))
        log("AMapView.ctx == null ? ${AMapView.ctx == null}")
        handler = Handler {
            if (it.what == 0) {
                val min = sec / 60
                val sec = sec % 60
                val minString: String = if (min > 9) {
                    "$min"
                } else {
                    "0$min"
                }
                val secString: String = if (sec > 9) {
                    "$sec"
                } else {
                    "0$sec"
                }
                marker?.title = "$minString:$secString"
                marker?.showInfoWindow()
            }
            return@Handler true
        }
        return this
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        when (call.method) {
            "marker#addWaitAcceptMarker" -> {
                val optionsJson = call.argument<String>("markerOptions") ?: "{}"
                val bookTime: Int = call.argument<Int>("bookTime")
                        ?: (System.currentTimeMillis() / 1000).toInt()
                sec = (System.currentTimeMillis() / 1000 - bookTime).toInt()

                marker = optionsJson.parseFieldJson<UnifiedMarkerOptions>().applyTo(map)
                marker?.isDraggable = false
                marker?.isInfoWindowEnable = true
                marker?.isClickable = false
                initTimer()

                result.success(success)
            }
            "marker#removeWaitAcceptMarker" -> {
                sec = 0
                timer?.cancel()
                timerTask?.cancel()
                marker?.remove()
                result.success(success)
            }
        }
    }
}

object AddDisTimeMarker : MapMethodHandler {

    lateinit var map: AMap

    override fun with(map: AMap): AddDisTimeMarker {
        this.map = map
        return this
    }

    private fun getLeftTimeStr(time: Int): String {
        val hour = time / 60 / 60
        val minute = time / 60 % 60
        val leftTime: String
        if (hour > 0) {
            leftTime = "${hour}时${minute}分"
        } else {
            leftTime = "${minute}分"
        }
        return leftTime
    }

    private fun getLeftDisStr(dis: Int): String {
        val km = dis / 1000
        val leftDis: String
        leftDis = if (km >= 1) {
            val disKm = DecimalFormat("#0.0").format(dis.toDouble() / 1000)
            "${disKm}千米"
        } else {
            "${dis}米"
        }
        return leftDis
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        val optionsJson = call.argument<String>("markerOptions") ?: "{}"

        log("方法marker#addMarker android端参数: optionsJson -> $optionsJson")

        var marker = optionsJson.parseFieldJson<UnifiedMarkerOptions>().applyTo(map)

        val dis: Int = call.argument<Int>("dis") ?: 0
        val time: Int = call.argument<Int>("time") ?: 0
        val des: String = call.argument<String>("des") ?: "剩余"

        this.map.setInfoWindowAdapter(DisTimeWindowAdapter(AMapView.ctx, des))
        marker.title = getLeftTimeStr(time)
        marker.snippet = getLeftDisStr(dis)
        marker.showInfoWindow()

        result.success(success)
    }
}
