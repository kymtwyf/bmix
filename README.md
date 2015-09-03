# bmix
#### Bluemix 短跑节作品 - Android端

---
## Idea
做一个简化版的大众点评，通过用户的喜欢或者不喜欢的点击，训练后台模型。根据用户事先定义的喜好以及训练的模型，提供推荐。

之后可以做一些优惠券及导航等内容

## 设计图

感谢Mickey

![首页推荐](http://res.cloudinary.com/denrcs4mp/image/upload/c_scale,w_300/v1441288889/Homepage.jpg)
![餐厅活动](http://res.cloudinary.com/denrcs4mp/image/upload/c_scale,w_300/v1441288985/coupon_direction.jpg)
![活动后的点评](http://res.cloudinary.com/denrcs4mp/image/upload/c_scale,w_300/v1441289021/Ranking.jpg)


## 后台
用Bluemix搭的后台，数据来源是大众点评。`成龙`大哥写了一个python的爬虫。`畅哥`写的推荐系统。

###### 后台提供相应接口的数据APP就可以工作。由于时间原因，没做用户这一块，演示只针对一个用户。
