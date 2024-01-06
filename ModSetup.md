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
# 基本概念
## Side
源自我对[Sides/1.18 - Forge Community Wiki (gemwire.uk)](https://forge.gemwire.uk/wiki/Sides/1.18)的理解
forge提供的isClientSide为一个布尔值，true对应客户端，false对应服务端。但需要说明的是，Minecraft使用了“物理端”和”逻辑端“这两个概念，因此，不产生歧义的方式是进行一个笛卡尔乘积，Side共有四个分类：
- **物理客户端** - _物理客户端_ 是每当您从启动器启动 Minecraft 时运行的整个程序。在游戏的图形化、可交互生存期内运行的所有线程、进程和服务都是物理客户端的一部分。
- **物理服务端** - 通常称为专用服务器，物理服务器是每当您启动任何专用 _服务器_ 可执行文件或 JAR （`minecraft_server.jar`） 时运行的整个程序，该可执行文件或 JAR （`minecraft_server.jar`） 不会显示可播放的 GUI。
- **逻辑服务端** - 逻辑服务器负责运行游戏 _逻辑_ ：生物生成、天气、更新库存、健康、AI 和所有其他游戏机制。逻辑服务器存在于物理服务器中，但也可以与逻辑客户端一起在物理客户端中作为单玩家世界运行。逻辑服务器始终在名为 `Server Thread`的线程中运行。<u> 也就是说，单人游戏里存在这样的一个内置服务器，执行多人游戏中服务器的功能.</u>
- **逻辑客户端** - 逻辑客户端接受来自播放器的输入并将其中继到 _逻辑_ 服务器。此外，它还从逻辑服务器接收信息，并以图形方式提供给玩家。逻辑客户端在 `Render Thread` 中运行，尽管通常会生成几个其他线程来处理音频和块渲染批处理等事情。
	<p style="color: aqua; margin-left: 20px">
	以下是一个闪电荆棘里的简单案例 </p>
```
//之前的一些关于isClientSide的应用
if(!pAttacker.level.isClientSide() &&pTarget!=null) {  
    ServerLevel world = ((ServerLevel) pAttacker.level);
    BlockPos position = pTarget.blockPosition();  
if(pLevel == 1) {
EntityType.LIGHTNING_BOLT.spawn(world, null, null, position, MobSpawnType.TRIGGERED, true, true);
    }  
}
```
## Events
源自我对[Events/1.18 - Forge Community Wiki (gemwire.uk)](https://forge.gemwire.uk/wiki/Events/1.18)以及Kaupenjoe(Youtube上做模组制作教程播放量最多的人)教程的理解
<p><span style="color:brown">事件（Events）
</span>是发生在<span style="color:brown">事件总线（eventBus）
</span>上的、用来激活那些
<span style="color:brown">已注册过的监听器（registered listener）</span>，让他们改变状态或者实现一些行为的一种信号</p>
这是forge允许模组和原版内容/资源/行为进行互动的最初级的方式，forge提供了一个事件数组，在其中的事件会被不同的游戏内容而激活。

当然，你也可以通过创建一个Events的子类来自定义一个事件，用于本模组或者关联模组的使用。这可以提高兼容性。

泛型事件：一些事件提供了泛型，允许开发者筛选对应类型（比如Block,Entity等）的事件，它们均实现了IGenericEvents<\T>接口，这些泛型事件类还可以通过接口里定义的```IGenericEvent#getType()```方法来取得泛型类型（有点类似于反射最初的应用）。如果我们开发者需要写一个泛型事件，需要实现GenericEvents<\T>接口而不是实现 IGenericEvent接口。
<p style="color:red;font-weight:bold;font-size:40px">-----Important ！-----
</p>
1.事件的泛型必须精确匹配监听器的泛型。
2.监听Object的监听器不会被某个精确指定了类型的事件（例如
```AttachCapabilitiesEvent<\ItemStack>```）激活。
3.如果事件监听器是被EventBus的实例方法register<\Object>所注册的，那么该监听器会监听提供了通配符 `<?>`的泛型事件，并忽略嵌套的泛型。
## Registration
源自我对[Registration/1.18 - Forge Community Wiki (gemwire.uk)](https://forge.gemwire.uk/wiki/Registration/1.18)以及Kaupenjoe教程的理解

## Language

## Config
源自我对[Configs/1.18 - Forge Community Wiki (gemwire.uk)](https://forge.gemwire.uk/wiki/Configs/1.18)的理解

## 论外更新
导入了fastutil(阿里云镜像)