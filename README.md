# RoundProgressView
##一、介绍

RoundProgressView是一个圆形的进度控件，可显示文字，也可当成评分控件
主要提供了：
    

 - 进度渐变颜色自定义
 - 进度文案颜色大小自定义等多种属性
 - 支持显示动画属性

##二、使用参数如下
使用方法只需要按如下配置响应参数，然后执行初始化即可；



         roundProgressView = (RoundProgressView)findViewById(R.id.roundProgressView);
        		 RoundProgressConfig roundProgressConfig = new RoundProgressConfig();
        			roundProgressConfig
                .setCircleBgColor(R.color.rcharview_cover_range_blue)
                .setCircleInsideColor(R.color.xiyou_white)
                .setEndColor(R.color.xiyou_pink)
                .setStartColor(R.color.xiyou_white)
                .setMaxProgress(100)
                .setMinProgress(0)
                .setTextSize(24)
                .setTextUnitSize(16)
                .setUnitText("分")
                .setProgressBarWidth(Helper.dip2px(getApplicationContext(), 18))
                .setTextColor(R.color.xiyou_pink)
                .setTextUnitColor(R.color.xiyou_pink);
        roundProgressView.init(roundProgressConfig);
        roundProgressView.setProgress(60, true);
        
    
效果图如下：

![](http://7xnby9.com1.z0.glb.clouddn.com/roundprogressview.png)	
----------


若有问题，可及时联系：

QQ:452825089

mail:452825089@qq.com

blog:http://iceanson.github.io
