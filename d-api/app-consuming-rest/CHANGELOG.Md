### 20171016

```aidl
    抓取数据时候进行验证码识别；

```

### 20171013

```aidl
    Test4j 在 MacOs JNI 调用出错，语言包大于20M 的情况；
    现改用 CMD 方式调用；
    
    运行时候 vm options 配置：
    -Djava.library.path=app-consuming-rest/libs
    
```    

### 20171011
    
    加入验证码识别；
    tessdata 测试数据目录；
    
```aidl

Tesseract的历史，其最早由HP实验室在1985年研发，10年后，也就是1995年已经是业内最准确的识别引擎之一了，但是HP后来放弃了OCR业务，不过好消息是后来HP决定反正不用，不如开源出来，所以2005年，又是10年后，Tesseract开源了，并且委托Google进行改进、优化的工作。

```    

```aidl
    安装准备
        xcode-select –install
        sudo xcodebuild -license 
        
    //安装支持 pdf 识别    
    brew install ghostscript
               
    支持tiff需要安装 libtiff和leptonica 

    
    //安装tesseract的同时安装训练工具
    brew install --with-training-tools tesseract
    
    //安装tesseract的同时安装所有语言，语言包比较大，如果安装的话时间较长，建议不安装，按需选择
    brew install  --all-languages tesseract
    
    //安装tesseract，并安装训练工具和语言
    brew install --all-languages --with-training-tools tesseract 
    
    //只安装tesseract，不安装训练工具
    brew install  tesseract

    //查看本地存在的语言库
    tesseract --list-langs
    /usr/local/Cellar/tesseract/3.05.01/share/tessdata

    //指定多语言，用+号相连
    tesseract -l chi_sim+eng imgName result
    
    //指定使用简体中文
    tesseract -l chi_sim imgName result
    
    //安装图形预处理工具
    brew reinstall opencv3 --with-java
    brew info opencv3
    
```

```aidl 

#下载对应版本的源码 opencv-330.jar
#unzip opencv;
#mkdir build
#cd build
#cmake -D BUILD_SHARED_LIBS=OFF ../
#make

mvn install:install-file \
-Dfile=/Users/moyong/project/env-myopensource/1-spring/12-spring/fast-api/d-api/app-consuming-rest/libs/opencv-330.jar \
-DgroupId=net.sourceforge.opencv  \
-DartifactId=opencv-java-3.3.0  \
-Dversion=3.3.0 \
-Dpackaging=jar

<dependency>
        <groupId>org.opencv</groupId>
        <artifactId>opencv</artifactId>
        <version>3.3.0</version>
        <systemPath>/Users/moyong/project/env-myopensource/1-spring/12-spring/fast-api/d-api/app-consuming-rest/libs/opencv-330.jar</systemPath>
        <scope>system</scope>
</dependency>

```

```aidl
    Tesseract的命令格式为：
    
    tesseract imagename outputbase [-l lang] [-psm pagesegmode] [configfile...]
    
    其中有几个参数：
    
    imagename是你要识别的图片的名字，不需要打引号，直接输入即可。
    
    outputbase是输出结果文件的名字，不需要文件类型后缀，就是txt，这个参数就是txt的文件名。
    
    lang是要用的语言代码，默认是英语，如果你要识别中文，就写 -l chi_sim，如果你要中英都识别，用 + 号把语言代码连起来：-l chi_sim+eng。
    
    pagesegmode是用来识别的模式，之前输入tesseract命令时应该也看到了，包含这些模式：
    
        0 = Orientation and script detection (OSD) only.
        1 = Automatic page segmentation with OSD.
        2 = Automatic page segmentation, but no OSD, or OCR
        3 = Fully automatic page segmentation, but no OSD. (Default)
        4 = Assume a single column of text of variable sizes.
        5 = Assume a single uniform block of vertically aligned text.
        6 = Assume a single uniform block of text.
        7 = Treat the image as a single text line.
        8 = Treat the image as a single word.
        9 = Treat the image as a single word in a circle.
        10 = Treat the image as a single character.


         0 定向脚本监测（OSD）
         1 使用OSD自动分页
         2 自动分页，但是不使用OSD或OCR（Optical Character Recognition，光学字符识别）
         3 全自动分页，但是没有使用OSD（默认）
         4 假设可变大小的一个文本列。
         5 假设垂直对齐文本的单个统一块。
         6 假设一个统一的文本块。
         7 将图像视为单个文本行。
         8 将图像视为单个词。
         9 将图像视为圆中的单个词。
         10 将图像视为单个字符。
    
   
    tesseract test.png out
    识别完成后会在下载目录多处一个out.txt的文件，里面包含识别出的文字
    
    tesseract test.png result -l chi_sim
    正确识别出来（识别后的文件为result.txt）
    
```

### 20170929

    POI 信息获取及存储优化
    
    
### 20170927

    测试驱动方式开发；
    
    httpclient 采集封装；
    
    htmlunit 采集封装；
    
    图吧信息采集 示例 http://poi.mapbar.com
        城市->分类->地点信息；
        城市->城区->政府机关信息；
        
        
        
### Json 接口采集

    