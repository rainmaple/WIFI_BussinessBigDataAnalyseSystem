## WIFI_BussinessBigDataAnalyseSystem
## 基于WIFI探针的商业大数据分析技术（纪念大学最后一次中软杯）
**河海大学**

**SUPER SUPER HANDSOME 组**

**组员：魏、陶、丁**

****
### 作品简介
该平台主要针对WIFI探针采集到的大量客流数据进行分析。
服务端主要接收探针每三秒发送一次的数据，于接收端搭建Tomcat纵向集群，有效处理1300台以上的并发请求，将数据保存到数据分析平台待用，文件系统使用HDFS分布式文件系统。数据分析平台搭建于Linux系统，采用Spark&Habse的分布式搭建模式，后台数据分析程序在3秒之内分析完实时数据，传至前台可视化，实现实时数据的展示。
作品较好的完成包括客流量、入店量、入店率、来访周期、新老顾客、顾客活跃度、驻店时长、来访周期、跳出率、深访率在内的九大基础指标的分析。并于前端页面上注重直观展示数据的变化趋势，数据分析所涉及的范围根据店铺具体情况支持自定义阕值。
对于探针功能的拓展：探针绑定短信模块，后台实现短信接口，从而对探针实现远程控制与状态监控。针对不同店铺大小推出小店铺探针，中性店铺三探针，大型店铺多探针模式，中型以上店铺支持定位，从而进一步分析呈现区域热点。
此外作品特色的添加了分析预测的功能，并基于分析预测功能结合历史数据智能的为商家提供商业决策支持，其中包括营销方案的推送，店铺排名波动的提醒功能。

示例图
![image](https://github.com/rainmaple/WIFI_BussinessBigDataAnalyseSystem/raw/master/images_about/%E5%95%86%E5%9F%8E%E5%8C%BA%E5%9F%9F%E7%83%AD%E7%82%B9.png)
### 1.主要框架
本系统使用Spark框架，结合HBase数据库、服务器Tomcat纵向集群、Hadoop集群组成。
缓冲服务器端主要使用HBase和HDFS，HBase用来存储结构化数据，HDFS用来存储训练模型数据，
主要分析系统是通过Spark集群来实现其快速分析，通过tomcat和echarts把可视化数据传到页面显示端口，Bootstrap框架搭建前台页面使得系统通过PC和手机都可以访问

### 2.创新点
  - 在完成功能性需求后，我们还增加了预测功能，根据以往的年月日，在高峰期前可以提醒商户进行货物调度
  - 对于依赖于学生或在学校周围的类似商店，我们选择了周预测，提醒商户为高峰期做好准备
  - 通过单片机可以通过发送不同的命令可以对探针进行远程控制，如：关闭、开启、重启、定时关闭、定时开启等功能

### 3.我们的视频上传地址
![点此观看](http://v.youku.com/v_show/id_XMjg1ODE1NDc2MA==.html?spm=a2hzp.8244740.0.0)密码为 `super2017`

### 4.主要说明我们上传文件的分类

***文件目录，如下：***

  - 需求规格说明书.pdf
  - 软件设计说明书.pdf
  - 测试文档.pdf
  - 软件参数.pdf
  - 程序源代码（文件夹）

    - Spark分析源代码（文件夹）
    - WIFI探针短信模块源代码（文件夹）
    - 接收服务器源代码（文件夹）
    - 网页请求服务器源代码（文件夹）
    - 网页源代码（文件夹）
    - 源代码说明.pdf

***文件内容说明***
关于宣传册内容
![封面](https://github.com/rainmaple/WIFI_BussinessBigDataAnalyseSystem/raw/master/images_about/%E5%AE%A3%E4%BC%A0%E5%86%8C/00%20%E5%B0%81%E9%9D%A2.jpg)

![作品概况](https://github.com/rainmaple/WIFI_BussinessBigDataAnalyseSystem/raw/master/images_about/宣传册/01%20作品概况.jpg)

![技术架构](https://github.com/rainmaple/WIFI_BussinessBigDataAnalyseSystem/raw/master/images_about/%E5%AE%A3%E4%BC%A0%E5%86%8C/02%20%E6%8A%80%E6%9C%AF%E6%9E%B6%E6%9E%84.jpg)

![功能概览](https://github.com/rainmaple/WIFI_BussinessBigDataAnalyseSystem/raw/master/images_about/%E5%AE%A3%E4%BC%A0%E5%86%8C/03%20%E5%8A%9F%E8%83%BD%E6%A6%82%E8%A7%88.jpg)

| 文件| 主要内容 |
| ------ | ------ |
| 需求规格说明书.pdf |详细描述本题目的功能与非功能需求 |
| 软件设计说明书.pdf | 详细描述软件的架构、设计理念、算法思路等 |
| 测试文档.pdf | 描述测试环境的搭建、准备的测试数据、测试方法等 |
| 软件参数.pdf| 包括系统整体架构图、主要开发语言、使用的开源框架、数据存储环境、前端框架等|
| 程序源代码 | 指未编译的按照一定的程序设计语言规范书写的程序代码 |
| 源代码说明.pdf | 说明源码中各个文件的作用及解释|

## 5.WIFI探针远程控制说明
 - 正常情况下通过探针监控页面关闭或打开探针不会使探针产生异常
 - 探针当前状态如果数据库中卫OFF，则恒显示为状态良好，如果为ON但通过数据库比对发现距离上一次收到数据超过五分钟，则为非正常
 - 当服务器在接受探针发送过来的数据后检测发送数据的探针在数据库中状态，如果为OFF则修改为ON

****

###### SUPER SUPER HANDSOME 组
