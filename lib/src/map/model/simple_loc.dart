import 'dart:convert';

class SimpleLoc {
  final double lat;
  final double lng;
  final double bearing;
  final int dis;
  final int time;

  const SimpleLoc(this.lat, this.lng,this.bearing,this.time,this.dis);

  Map<String, Object> toJson() {
    return {
      'lat': lat,
      'lng': lng,
      'bearing': bearing,
      'dis': dis,
      'time': time,
    };
  }

  String toJsonString() => jsonEncode(toJson());

  SimpleLoc.fromJson(Map<String, dynamic> json)
      : lat = json['lat'] as double,
        lng = json['lng'] as double,
        dis = json['dis'] as int,
        time = json['time'] as int,
        bearing = json['bearing'] as double;

  SimpleLoc copyWith({
    double lat,
    double lng,
  }) {
    return SimpleLoc(
      lat ?? this.lat,
      lng ?? this.lng,
      bearing ?? this.bearing,
      time ?? this.time,
      dis ?? this.dis,
    );
  }

  @override
  bool operator ==(Object other) =>
      identical(this, other) ||
          other is SimpleLoc &&
              runtimeType == other.runtimeType &&
              lat == other.lat &&
              lng == other.lng &&
              dis == other.dis &&
              time == other.time &&
              bearing == other.bearing
  ;

  @override
  int get hashCode => lat.hashCode ^ lng.hashCode ^ bearing.hashCode ^ dis.hashCode^time.hashCode;

  @override
  String toString() {
    return 'SimpleLoc{lat: $lat, lng: $lng, bearing: $bearing, dis: $dis, time: $time}';
  }


}
