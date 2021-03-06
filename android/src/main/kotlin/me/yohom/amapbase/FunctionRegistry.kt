package me.yohom.amapbase

import me.yohom.amapbase.location.Init
import me.yohom.amapbase.location.StartLocate
import me.yohom.amapbase.location.StopLocate
import me.yohom.amapbase.map.*
import me.yohom.amapbase.navi.handler.StartNavi
import me.yohom.amapbase.search.*

/**
 * 地图功能集合
 */
val MAP_METHOD_HANDLER: Map<String, MapMethodHandler> = mapOf(
        "map#setMyLocationStyle" to SetMyLocationStyle,
        "map#setUiSettings" to SetUiSettings,
        "marker#addMarker" to AddMarker,
        "marker#addMarkers" to AddMarkers,
        "marker#clear" to ClearMarker,
        "map#showIndoorMap" to ShowIndoorMap,
        "map#setMapType" to SetMapType,
        "map#setLanguage" to SetLanguage,
        "map#clear" to ClearMap,
        "map#setZoomLevel" to SetZoomLevel,
        "map#setPosition" to SetPosition,
        "map#setMapStatusLimits" to SetMapStatusLimits,
        "tool#convertCoordinate" to ConvertCoordinate,
        "tool#calcDistance" to CalcDistance,
        "offline#openOfflineManager" to OpenOfflineManager,
        "map#addPolyline" to AddPolyline,
        "map#zoomToSpan" to ZoomToSpan,
        "map#screenshot" to ScreenShot,
        "map#setCustomMapStylePath" to SetCustomMapStylePath,
        "map#setMapCustomEnable" to SetMapCustomEnable,
        "map#setCustomMapStyleID" to SetCustomMapStyleID,
        "map#getCenterPoint" to GetCenterLnglat,
        "map#changeLatLng" to ChangeLatLng,
        //以下为扩展的方法
        "map#addPolygon" to AddPolygon,
        "marker#setSmoothMarker" to SmoothMarker,
        "marker#moveSmoothMarker" to SmoothMarker,
        "marker#removeSmoothMarker" to SmoothMarker,
        "marker#arriveStartSmoothMarker" to SmoothMarker,
        "marker#countDownSmoothMarker" to SmoothMarker,
        "marker#showLeftSmoothMarker" to SmoothMarker,
        //水纹扩散效果
        "map#addWaveAnimation" to WaveAnimation,
        "map#removeWaveAnimation" to WaveAnimation,
        //判断是否在区域内
        "map#isInGeoArea" to IsInGeoArea,
        //起点等待派单带倒计时infoWindow
        "marker#addWaitAcceptMarker" to WaitAcceptMarker,
        "marker#removeWaitAcceptMarker" to WaitAcceptMarker,
        //带剩余时间距离的marker
        "marker#addDisTimeMarker" to AddDisTimeMarker)

/**
 * 搜索功能集合
 */
val SEARCH_METHOD_HANDLER: Map<String, SearchMethodHandler> = mapOf(
        "search#calculateDriveRoute" to CalculateDriveRoute,
        "search#searchPoi" to SearchPoiKeyword,
        "search#searchPoiBound" to SearchPoiBound,
        "search#searchPoiPolygon" to SearchPoiPolygon,
        "search#searchPoiId" to SearchPoiId,
        "search#searchRoutePoiLine" to SearchRoutePoiLine,
        "search#searchRoutePoiPolygon" to SearchRoutePoiPolygon,
        "search#searchGeocode" to SearchGeocode,
        "search#searchReGeocode" to SearchReGeocode,
        "search#searchBusStation" to SearchBusStation,
        "tool#distanceSearch" to DistanceSearchHandler
)

/**
 * 导航功能集合
 */
val NAVI_METHOD_HANDLER: Map<String, NaviMethodHandler> = mapOf(
        "navi#startNavi" to StartNavi
)

/**
 * 定位功能集合
 */
val LOCATION_METHOD_HANDLER: Map<String, LocationMethodHandler> = mapOf(
        "location#init" to Init,
        "location#startLocate" to StartLocate,
        "location#stopLocate" to StopLocate
)
