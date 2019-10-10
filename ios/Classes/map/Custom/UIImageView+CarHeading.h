//
//  UIImageView+CarHeading.h
//  iOSClientV6
//
//  Created by xiaoka on 2018/12/10.
//  Copyright (C), 2012-2019, Sichuan Xiaoka Technology Co., Ltd.
//  司机车头

#import <UIKit/UIKit.h>

@interface UIImageView (CarHeading)

/**
 * 旋转车头

 @param heading 车头方向
 */
- (void)rotateWithHeading:(CGFloat)heading;

@end
