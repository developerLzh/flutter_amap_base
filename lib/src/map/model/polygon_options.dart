import 'dart:convert';
import 'dart:ui';

import 'package:amap_base/amap_base.dart';
import 'package:flutter/material.dart';
import 'package:meta/meta.dart';

class PolygonOptions {
  /// 顶点 [Android, iOS]
  final List<LatLng> latLngList;

  /// 描边的宽度 [Android, iOS]
  final double strokeWidth;

  /// 描边的颜色 [Android, iOS]
  final Color strokeColor;

  /// 填充的颜色 [Android, iOS]
  final Color fillColor;


  PolygonOptions({
    @required this.latLngList,
    this.strokeColor = Colors.black,
    this.fillColor = Colors.black,
    this.strokeWidth = 10,
  });

  Map<String, Object> toJson() {
    return {
      'latLngList': latLngList?.map((it) => it.toJson())?.toList() ?? List(),
      'strokeWidth': strokeWidth,
      'strokeColor': strokeColor.value.toRadixString(16),
      'fillColor': fillColor.value.toRadixString(16),
    };
  }

  String toJsonString() => jsonEncode(toJson());

  @override
  String toString() {
    return 'PolygonOptions{latLngList: $latLngList, strokeWidth: $strokeWidth, strokeColor: $strokeColor, fillColor: $fillColor}';
  }


}
