//
//  XKMoveAnnotationView.h
//  amap_base
//
//  Created by xiaoka on 2019/10/10.
//

#import <MAMapKit/MAMapKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface XKMoveAnnotationView : MAAnnotationView

/**
 * 设置车头信息
 @param heading 车头角度
 */
- (void)setUpHeading:(CGFloat)heading;

@end

NS_ASSUME_NONNULL_END
