import 'dart:async';
import 'dart:convert';
import 'dart:typed_data';

import 'package:amap_base/amap_base.dart';
import 'package:amap_base/src/common/log.dart';
import 'package:amap_base/src/map/model/marker_options.dart';
import 'package:amap_base/src/map/model/my_location_style.dart';
import 'package:amap_base/src/map/model/polyline_options.dart';
import 'package:amap_base/src/map/model/ui_settings.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:meta/meta.dart';

import 'model/polygon_options.dart';
import 'model/simple_loc.dart';

class AMapController {
  final MethodChannel _mapChannel;
  final EventChannel _markerClickedEventChannel;
  final EventChannel _mapDragEventChannel;
  final EventChannel _mapClickEventChannel;

  AMapController.withId(int id)
      : _mapChannel = MethodChannel('me.yohom/map$id'),
        _markerClickedEventChannel = EventChannel('me.yohom/marker_clicked$id'),
        _mapDragEventChannel = EventChannel('me.yohom/map_drag_change$id'),
        _mapClickEventChannel =
            EventChannel('me.yohom/map_click_event_channel$id');

  void dispose() {}

  //region dart -> native
  Future setMyLocationStyle(MyLocationStyle style) {
    final _styleJson =
        jsonEncode(style?.toJson() ?? MyLocationStyle().toJson());

    L.p('方法setMyLocationStyle dart端参数: styleJson -> $_styleJson');
    return _mapChannel.invokeMethod(
      'map#setMyLocationStyle',
      {'myLocationStyle': _styleJson},
    );
  }

  Future setUiSettings(UiSettings uiSettings) {
    final _uiSettings = jsonEncode(uiSettings.toJson());

    L.p('方法setUiSettings dart端参数: _uiSettings -> $_uiSettings');
    return _mapChannel.invokeMethod(
      'map#setUiSettings',
      {'uiSettings': _uiSettings},
    );
  }

  Future addMarker(MarkerOptions options) {
    final _optionsJson = options.toJsonString();
    L.p('方法addMarker dart端参数: _optionsJson -> $_optionsJson');
    return _mapChannel.invokeMethod(
      'marker#addMarker',
      {'markerOptions': _optionsJson},
    );
  }

  Future addMarkers(
    List<MarkerOptions> optionsList, {
    bool moveToCenter = true,
    bool clear = true,
  }) {
    final _optionsListJson =
        jsonEncode(optionsList.map((it) => it.toJson()).toList());
    L.p('方法addMarkers dart端参数: _optionsListJson -> $_optionsListJson');
    return _mapChannel.invokeMethod(
      'marker#addMarkers',
      {
        'moveToCenter': moveToCenter,
        'markerOptionsList': _optionsListJson,
        'clear': clear,
      },
    );
  }

  Future showIndoorMap(bool enable) {
    return _mapChannel.invokeMethod(
      'map#showIndoorMap',
      {'showIndoorMap': enable},
    );
  }

  Future setMapType(int mapType) {
    return _mapChannel.invokeMethod(
      'map#setMapType',
      {'mapType': mapType},
    );
  }

  Future setLanguage(int language) {
    return _mapChannel.invokeMethod(
      'map#setLanguage',
      {'language': language},
    );
  }

  Future clearMarkers() {
    return _mapChannel.invokeMethod('marker#clear');
  }

  Future clearMap() {
    return _mapChannel.invokeMethod('map#clear');
  }

  /// 设置缩放等级
  Future setZoomLevel(int level) {
    L.p('setZoomLevel dart端参数: level -> $level');

    return _mapChannel.invokeMethod(
      'map#setZoomLevel',
      {'zoomLevel': level},
    );
  }

  /// 设置地图中心点
  Future setPosition({
    @required LatLng target,
    double zoom = 10,
    double tilt = 0,
    double bearing = 0,
  }) {
    L.p('setPosition dart端参数: target -> $target, zoom -> $zoom, tilt -> $tilt, bearing -> $bearing');

    return _mapChannel.invokeMethod(
      'map#setPosition',
      {
        'target': target.toJsonString(),
        'zoom': zoom,
        'tilt': tilt,
        'bearing': bearing,
      },
    );
  }

  /// 限制地图的显示范围
  Future setMapStatusLimits({
    /// 西南角 [Android]
    @required LatLng swLatLng,

    /// 东北角 [Android]
    @required LatLng neLatLng,

    /// 中心 [iOS]
    @required LatLng center,

    /// 纬度delta [iOS]
    @required double deltaLat,

    /// 经度delta [iOS]
    @required double deltaLng,
  }) {
    L.p('setPosition dart端参数: swLatLng -> $swLatLng, neLatLng -> $neLatLng, center -> $center, deltaLat -> $deltaLat, deltaLng -> $deltaLng');

    return _mapChannel.invokeMethod(
      'map#setMapStatusLimits',
      {
        'swLatLng': swLatLng.toJsonString(),
        'neLatLng': neLatLng.toJsonString(),
        'center': center.toJsonString(),
        'deltaLat': deltaLat,
        'deltaLng': deltaLng,
      },
    );
  }

  /// 添加线
  Future addPolyline(PolylineOptions options, {bool clearLast = false}) {
    L.p('addPolyline dart端参数: options -> $options');

    return _mapChannel.invokeMethod(
      'map#addPolyline',
      {
        'options': options.toJsonString(),
        'clearLast': clearLast,
      },
    );
  }

  /// 移动镜头到当前的视角
  Future zoomToSpan(
    List<LatLng> bound, {
    int paddingLeft = 80,
    int paddingRight = 80,
    int paddingTop = 80,
    int paddingBottom = 80,
  }) {
    final boundJson =
        jsonEncode(bound?.map((it) => it.toJson())?.toList() ?? List());

    L.p('zoomToSpan dart端参数: bound -> $boundJson');

    return _mapChannel.invokeMethod(
      'map#zoomToSpan',
      {
        'bound': boundJson,
        'paddingLeft': paddingLeft,
        'paddingRight': paddingRight,
        'paddingTop': paddingTop,
        'paddingBottom': paddingBottom,
      },
    );
  }

  /// 移动指定LatLng到中心
  Future changeLatLng(LatLng target) {
    L.p('changeLatLng dart端参数: target -> $target');

    return _mapChannel.invokeMethod(
      'map#changeLatLng',
      {'target': target.toJsonString()},
    );
  }

  /// 获取中心点
  Future<LatLng> getCenterLatlng() async {
    String result = await _mapChannel.invokeMethod("map#getCenterPoint");
    return LatLng.fromJson(json.decode(result));
  }

  /// 截图
  ///
  /// 可能会抛出 [PlatformException]
  Future<Uint8List> screenShot() async {
    try {
      var result = await _mapChannel.invokeMethod("map#screenshot");
      if (result is List<dynamic>) {
        return Uint8List.fromList(result.map((i) => i as int).toList());
      } else if (result is Uint8List) {
        return result;
      }
      throw PlatformException(code: "不支持的类型");
    } catch (e) {
      if (e is PlatformException) {
        L.d(e.code);
        throw e;
      }
      throw Error();
    }
  }

  /// 设置自定义样式的文件路径
  Future setCustomMapStylePath(String path) {
    L.p('setCustomMapStylePath dart端参数: path -> $path');

    return _mapChannel.invokeMethod(
      'map#setCustomMapStylePath',
      {'path': path},
    );
  }

  /// 使能自定义样式
  Future setMapCustomEnable(bool enabled) {
    L.p('setMapCustomEnable dart端参数: enabled -> $enabled');

    return _mapChannel.invokeMethod(
      'map#setMapCustomEnable',
      {'enabled': enabled},
    );
  }

  /// 使用在线自定义样式
  Future setCustomMapStyleID(String styleId) {
    L.p('setCustomMapStyleID dart端参数: styleId -> $styleId');

    return _mapChannel.invokeMethod(
      'map#setCustomMapStyleID',
      {'styleId': styleId},
    );
  }

  /// 添加面
  Future addPolygon(PolygonOptions options) {
    L.p('addPolyline dart端参数: options -> $options');

    return _mapChannel.invokeMethod(
      'map#addPolygon',
      {'options': options.toJsonString()},
    );
  }

  ///设置平滑移动的marker信息
  Future setSmoothMarker(MarkerOptions options) {
    final _optionsJson = options.toJsonString();
    L.p('方法setSmoothMarker dart端参数: _optionsJson -> $_optionsJson');
    return _mapChannel.invokeMethod(
      'marker#setSmoothMarker',
      {'markerOptions': _optionsJson},
    );
  }

  /// 平滑移动marker到下一个点
  Future moveSmoothMarker(SimpleLoc simpleLoc) {
    final _locJson = simpleLoc.toJsonString();
    L.p('方法moveSmoothMarker dart端参数: _optionsJson -> $_locJson');
    return _mapChannel.invokeMethod(
      'marker#moveSmoothMarker',
      {'simpleLoc': _locJson},
    );
  }

  /// 移除平滑移动的marker
  Future removeSmoothMarker() {
    return _mapChannel.invokeMethod(
      'marker#removeSmoothMarker',
    );
  }

  /// 到达预约地后倒计时
  Future arriveStartSmoothMarker(
      MarkerOptions options, int arriveTime, int bookTime) {
    final _optionsJson = options.toJsonString();
    return _mapChannel.invokeMethod('marker#arriveStartSmoothMarker', {
      'markerOptions': _optionsJson,
      'arriveTime': arriveTime,
      'bookTime': bookTime
    });
  }

  /// 倒计时infoWindow
  Future countDownSmoothMarker(int leftSec) {
    return _mapChannel.invokeMethod('marker#countDownSmoothMarker', {
      'leftSec': leftSec,
    });
  }

  /// 平滑移动marker到下一个点
  Future showLeftSmoothMarker(int time, int dis, {String des = '剩余'}) {
    return _mapChannel.invokeMethod(
      'marker#showLeftSmoothMarker',
      {
        'time': time,
        'dis': dis,
        'des': des,
      },
    );
  }

  /// 判断目标点是否在某个面中
  Future<bool> isInGeoArea(LatLng target, List<LatLng> area) {
    if (area.length < 3) {
      return Future<bool>(() {
        return false;
      });
    }
    final _target = target.toJsonString();
    final _area = jsonEncode(area.map((it) => it.toJson()).toList());
    return _mapChannel.invokeMethod(
      'map#isInGeoArea',
      {
        'target': _target,
        'area': _area,
      },
    );
  }

  /// 添加波纹扩散动画
  Future addWaveAnimation(LatLng latlng,
      {fillColor = '#FF9220', strokeColor = '#FF9220'}) {
    final _optionsJson = latlng.toJsonString();

    return _mapChannel.invokeMethod('map#addWaveAnimation', {
      'latlng': _optionsJson,
      'waveFillColor': fillColor,
      'waveStrokeColor': strokeColor,
    });
  }

  /// 移除波纹扩散动画
  Future removeWaveAnimation() {
    return _mapChannel.invokeMethod(
      'map#removeWaveAnimation',
    );
  }

  /// 添加带倒计时的等待接marker
  Future addWaitAcceptMarker(MarkerOptions options, int bookTime) {
    final _optionsJson = options.toJsonString();

    return _mapChannel.invokeMethod('marker#addWaitAcceptMarker', {
      'markerOptions': _optionsJson,
      'bookTime': bookTime,
    });
  }

  /// 移除带倒计时的等待接marker
  Future removeWaitAcceptMarker() {
    return _mapChannel.invokeMethod(
      'marker#removeWaitAcceptMarker',
    );
  }

  ///添加带时间距离的marker
  Future addDisTimeMarker(MarkerOptions options, int time, int dis,
      {String des = '剩余'}) {
    final _optionsJson = options.toJsonString();
    L.p('方法addMarker dart端参数: _optionsJson -> $_optionsJson');
    return _mapChannel.invokeMethod(
      'marker#addDisTimeMarker',
      {
        'markerOptions': _optionsJson,
        'time': time,
        'dis': dis,
        'des': des,
      },
    );
  }

  //endregion

  /// marker点击事件流
  Stream<MarkerOptions> get markerClickedEvent => _markerClickedEventChannel
      .receiveBroadcastStream()
      .map((data) => MarkerOptions.fromJson(jsonDecode(data)));

  /// 地图拖拽
  Stream<LatLng> get mapDragEvent => _mapDragEventChannel
      .receiveBroadcastStream()
      .map((data) => LatLng.fromJson(jsonDecode(data)));

  /// 地图点击
  Stream<LatLng> get mapClickEvent => _mapClickEventChannel
      .receiveBroadcastStream()
      .map((data) => LatLng.fromJson(jsonDecode(data)));
}
