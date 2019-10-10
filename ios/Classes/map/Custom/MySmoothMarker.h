//
//  MySmoothMarker.h
//  amap_base
//
//  Created by xiaoka on 2019/10/10.
//

#import <Foundation/Foundation.h>
#import "MapModels.h"

NS_ASSUME_NONNULL_BEGIN

@interface MySmoothMarker : NSObject

- (instancetype)initWithMarker:(MoveMarkerAnnotation *)marker
                       mapView:(MAMapView *)mapView;


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
       isContinue:(BOOL)isContinue;


/**
 * 移除
 */
- (void)removeSmoothMarker;

@end

NS_ASSUME_NONNULL_END
