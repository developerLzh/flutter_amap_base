//
//  UIImageView+CarHeading.m
//  iOSClientV6
//
//  Created by xiaoka on 2018/12/10.
//  Copyright (C), 2012-2019, Sichuan Xiaoka Technology Co., Ltd.
//

#import "UIImageView+CarHeading.h"

@implementation UIImageView (CarHeading)

- (void)rotateWithHeading:(CGFloat)heading
{
    //将设备的方向角度换算成弧度
    CGFloat headings = M_PI * heading / 180.0;
    //创建不断旋转CALayer的transform属性的动画
    CABasicAnimation * rotateAnimation = [CABasicAnimation animationWithKeyPath:@"transform"];
    //动画起始值
    CATransform3D formValue = self.layer.transform;
    rotateAnimation.fromValue = [NSValue valueWithCATransform3D:formValue];
    //绕Z轴旋转heading弧度的变换矩阵
    CATransform3D toValue = CATransform3DMakeRotation(headings, 0, 0, 1);
    //设置动画结束值
    rotateAnimation.toValue = [NSValue valueWithCATransform3D:toValue];
    rotateAnimation.duration = 0.35;
    [rotateAnimation setRemovedOnCompletion:YES];
    //设置动画结束后layer的变换矩阵
    self.layer.transform = toValue;
    //添加动画
    [self.layer addAnimation:rotateAnimation forKey:nil];
}




@end
