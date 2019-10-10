//
//  MySmoothMarker.m
//  amap_base
//
//  Created by xiaoka on 2019/10/10.
//

#import "MySmoothMarker.h"
#import "MAMapView.h"

@interface MySmoothMarker()

@property (nonatomic, strong) MAMapView *mapView;

@property (nonatomic, strong) MoveMarkerAnnotation *marker;

@property (nonatomic, strong) LatLng *lastLatLng;

@end

@implementation MySmoothMarker

- (instancetype)initWithMarker:(MoveMarkerAnnotation *)marker
                       mapView:(MAMapView *)mapView
{
    self = [super init];
    if (self) {
        self.mapView = mapView;
        self.marker = marker;
    }
    return self;
}


/**
 * 开始移动

 @param latLng 经纬度
 @param time   时间
 @param isContinue 是否在以上次停止后的坐标点继续移动 当list.size()=1
 *                   注意:如果调用 startMove(list,time,isContinue) 如果list.size=1 只传递了一个点并且isContinue=false
 *                   那么 onSetGeoPoint回调方法返回的角度是0 因为只有一个点是无法计算角度的
 */
- (void)startMove:(LatLng *)latLng
             time:(CGFloat)time
       isContinue:(BOOL)isContinue
{
    if (time <= 0) {
        time = 1.0f;
    }
    
    NSMutableArray *pointArray = [NSMutableArray array];
    if (isContinue && _lastLatLng.latitude != 0 && _lastLatLng.longitude != 0) {
        [pointArray addObject:_lastLatLng];
    }
    
    [pointArray addObject:latLng];
    
    CLLocationCoordinate2D coords[pointArray.count];
    for (int i = 0; i < pointArray.count; i ++) {
        LatLng *latLng = pointArray[i];
        coords[i] = CLLocationCoordinate2DMake(latLng.latitude, latLng.longitude);
    }
    [_marker addMoveAnimationWithKeyCoordinates:coords
                                          count:sizeof(coords) / sizeof(coords[0])
                                   withDuration:time
                                       withName:nil
                               completeCallback:^(BOOL isFinished) {
        
    }];
    free(coords);
}


- (void)removeSmoothMarker {
    [_mapView removeAnnotation:_marker];
    _marker = nil;
}


@end
