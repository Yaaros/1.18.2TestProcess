//本文均以1.18.2来讲授
# 下载mdk
1.进入[Downloads for Minecraft Forge for Minecraft 1.18.2](https://files.minecraftforge.net/net/minecraftforge/forge/index_1.18.2.html)下载模组开发者工具mdk
2.下载下来是一个压缩包，解压到工作目录，用idea以项目打开即可
3.配置idea的maven镜像/使用手动下载的maven或gradle构建工具等教程不在此处展示

# 初始化
1.更改软件包名称为com.作者名字.模组名称，(以下使用com.name.modid表示)主文件名字改为Main或者模组驼峰名称写法(以下用Main表示)
2.在src/main/java/resources/META-INF/mods.toml、build.gradle和Main里面分别修改模组名称
## Main.java
除包头、注释、和导包之外的前三行应该为：
@Mod(Main.MOD_ID)  
public class Main {  
    public static final String MOD_ID = "undefined_ctm";
其中"undefined_ctm"是我的模组名称
## build.gradle 
version = '0.0.1-1.18.2'  
group = 'com.yaaros.ctm_undefined'  
archivesBaseName = 'undefined_ctm'
## mods.toml 
注意修改dependencies.modid

3.进入[Getting Started (parchmentmc.org)](https://parchmentmc.org/docs/getting-started) 配置parchment混淆，用来把官方的p12182_1之类的变量名改为直接能看出意思的变量名
具体配置方法(如果不想看英文原文的话)是点开build.gradle，
### 3.1 在buildscript的repositories里添加
```
maven { url = 'https://maven.parchmentmc.org' }
```
### 3.2 在buildscript的dependencies里添加
```
classpath 'org.parchmentmc:librarian:1.+'
```
### 3.3 在plugins的下方有个
apply plugin: 'net.minecraftforge.gradle'
一定要在这行的下面添加
```
apply plugin: 'org.parchmentmc.librarian.forgegradle'
```
按官方说法是千万不能将两个插件的次序颠倒
### 3.4 在minecraft部分改变映射通道
```
mappings channel: 'parchment', version: '2022.11.06-1.18.2'
```


4.重新构建Gradle，构建完毕后点击
gradle->forge gradle runs->genIntellijRuns生成一些东西，再点击runClient执行任务,如果mc界面能够出现，证明成功