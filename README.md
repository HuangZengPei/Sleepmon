# Sleepmon
我的Android团队项目，一款可以监测、统计睡眠质量情况的睡眠健康伴侣应用。

项目名称：Sleepmon（眠眠萌）

软件环境：Windows10、Android Studio、Android手机

项目描述：

开发一款可以监测、统计睡眠质量情况的睡眠健康伴侣应用，陪伴用户度过夜晚美好的睡眠时间，培养良好睡眠习惯，感知并记录下睡眠过程，呈现可视化的睡眠报告并进行评分，帮助用户更了解自己的睡眠。该应用具有睡眠过程检测（使用传感器产生睡眠质量参数）、睡眠报告生成、催眠曲播放（柔美、轻松的睡眠音乐）及眠梦日记（包含密码设置和登录、日记添加和修改）等功能。


项目所采用的技术、方法和工具

技术、方法：
（1）界面框架设计遵循 Material Design 规范；（2）事件处理（点击事件、activity跳转等）；（3）Database 和 sharedPref 数据存储方式（日记和睡眠报告的存储、日记密码的存储）；（4）Widegt；（5）Broadcast（睡眠时间的统计、解锁的广播）；（6）Service（催眠曲播放功能）；（7）传感器使用（睡眠质量参数的产生：加速度传感器等）；（8）2D 动画（自定布局生成图表）；（9）网络访问（讯飞语音输入）；（10）后台交互（Fragment 之间的通信）。

工具：第三方框架讯飞语音识别（日记语音输入）

方法：
睡眠报告产生的相关参数有：用户的睡眠时间、用户每次的睡眠时长、用户的起床时间与理想睡眠时间的差异、用户在点击开始睡眠后到进入睡眠前的手机使用情况（手机锁屏次数检测）、用户睡眠过程中的“翻身”次数（通过加速度传感器检测）等。
