//
//  SimpleLoc.h
//  amap_base
//
//  Created by xiaoka on 2019/10/10.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface SimpleLoc : NSObject

@property (nonatomic, assign) CGFloat lat;

@property (nonatomic, assign) CGFloat lng;

@property (nonatomic, assign) CGFloat bearing;

@end

NS_ASSUME_NONNULL_END
