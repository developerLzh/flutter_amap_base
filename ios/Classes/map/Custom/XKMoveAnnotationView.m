//
//  XKMoveAnnotationView.m
//  amap_base
//
//  Created by xiaoka on 2019/10/10.
//

#import "XKMoveAnnotationView.h"
#import "UIImageView+CarHeading.h"

@interface XKMoveAnnotationView()
/** 定位img */
@property (nonatomic, strong) UIImageView * locationImg;

@end

@implementation XKMoveAnnotationView

- (void)setSelected:(BOOL)selected
{
    [self setSelected:selected animated:NO];
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated
{
    if (self.selected == selected) {
        return;
    }
    [super setSelected:selected animated:animated];
}

#pragma mark - Life Cycle
- (id)initWithAnnotation:(id<MAAnnotation>)annotation reuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithAnnotation:annotation reuseIdentifier:reuseIdentifier];
    if (self)
    {
        self.bounds = CGRectMake(0.f, 0.f, 20, 35);
        self.backgroundColor = [UIColor clearColor];
        self.locationImg = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, 20, 35)];
        self.locationImg.image = [UIImage imageNamed:@"SQZcCar"];
        [self addSubview:self.locationImg];
    }
    return self;
}


- (void)setUpHeading:(CGFloat)heading
{
    [self.locationImg rotateWithHeading:heading];
}

@end
