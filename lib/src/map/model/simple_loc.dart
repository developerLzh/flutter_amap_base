import 'dart:convert';

class SimpleLoc {
  final num lat;
  final num lng;
  final num bearing;

  const SimpleLoc(this.lat, this.lng,this.bearing);

  Map<String, Object> toJson() {
    return {
      'lat': lat,
      'lng': lng,
      'bearing': bearing,
    };
  }

  String toJsonString() => jsonEncode(toJson());

  SimpleLoc.fromJson(Map<String, dynamic> json)
      : lat = json['lat'] as num,
        lng = json['lng'] as num,
        bearing = json['bearing'] as num;

  SimpleLoc copyWith({
    num lat,
    num lng,
    num bearing,
  }) {
    return SimpleLoc(
      lat ?? this.lat,
      lng ?? this.lng,
      bearing ?? this.bearing,
    );
  }

  @override
  bool operator ==(Object other) =>
      identical(this, other) ||
          other is SimpleLoc &&
              runtimeType == other.runtimeType &&
              lat == other.lat &&
              lng == other.lng &&
              bearing == other.bearing
  ;

  @override
  int get hashCode => lat.hashCode ^ lng.hashCode ^ bearing.hashCode;

  @override
  String toString() {
    return 'SimpleLoc{lat: $lat, lng: $lng, bearing: $bearing}';
  }


}
