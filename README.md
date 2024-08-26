# HomeTorrentMultiplatform

---

# 1、简介

## 1.1 背景

本项目是使用compose multiplatform技术开发的一个磁力搜索工具，支持Android、iOS、Windows、Linux、MacOs五大平台。

主要目标是验证compose multiplatform作为跨平台技术载体的可行性，解决跨平台组件的各种问题，总结踩坑经验。
次要目标是构建一个有实际用途的app，例如支持磁力搜索

## 1.2 项目架构

### 1.2.1 主目录
本项目使用gradle构建，不同组件模块化，方便复用，主目录如下

| 目录         | 简介      |
|------------|---------|
| composeApp | 工程app目录 |
| framework  | 基础组件    |
| business   | 业务目录    |

### 1.2.1 framework目录

当前framework目录中含有以下子库。包含数据库、图片、视频、网络、资源等通用的模块，


| 目录          | 名称         | 简介                                                                    |
|-------------|------------|-----------------------------------------------------------------------|
| kmmdatabase | 数据库组件      | 使用room+sqlite3跨平台                                                     |
| kmmimage    | 图片组件       | 使用了coil3实现跨平台 ，以api方式引入coil3                                          |
| kmmplayer   | 播放器组件      | 当前在桌面端使用vlc跨平台，在android使用MediaPlayer + TextureView, 在ios使用AVPLayer    |
| libcommon   | 基础依赖组件     | 以api方式依赖了一些常用库，例如okio、kotlin datetime，简化使用                            |
| network     | 网络库组件      | 以api方式依赖ktor, 使用ktor实现网络请求                                            |
| perference  | 持久化组件      | 以api方式依赖datastore,使用datastore实现跨平台，作用类似于MMKV、android的SharedPerference |
| resources   | 资源组件       | 存放图片、语言等信息                                                            |
| utils       | 工具类组件      | 提供一些格式化或其它工具                                                          |
| widget      | compose组件  | 封装的一些compose组件，如主题、页面加载、按钮、toast等等                                    |

有些模块使用的官方或第三方组件本身已支持跨平台，但配置相对繁琐，在framework中简化了这些配置
另一些模块目前没有支持跨平台的组件，或难以使用，对于这类模块，在framework中使用expect actual在不同平台各自实现组件功能

在framework模块中，部分第三方库使用api方式引入，这是为了解决在不同平台编译或运行时找不到类的问题，例如在macos打包时，
使用implementation依赖库时如果不在composeApp的build.gradle.kts中声明依赖，就会编译失败或运行时异常。如采用api依赖的
库产生了版本冲突，可根据需要exclude库来解决冲突。

### 1.2.2 项目使用的技术栈和原理

本项目使用了以下技术或组件

| 名称                        | 功能                                               | 官网                                                           |
|---------------------------|--------------------------------------------------|--------------------------------------------------------------|
| kotlin multiplatform(KMM) | Kotlin跨平台，本项目的基础，可将支持KMM适配到compose multiplatform | https://www.jetbrains.com/kotlin-multiplatform/              |
| compose multiplatform     | compose 跨平台，建立在KMM之上的UI框架                        | https://www.jetbrains.com/lp/compose-multiplatform/          |
| voyager                   | 跨平台的路由组件                                         | https://voyager.adriel.cafe/                                 |
| coil3                     | 跨平台的图片组件                                         | https://coil-kt.github.io/coil/upgrading_to_coil3/           |
| ktor                      | 跨平台的网络请求框架                                       | https://ktor.io/                                             |
| okio                      | 跨平台文件IO组件，替代File                                 | https://square.github.io/okio/                               |
| vlcj                      | Vlc播放器的java接口，用来实现桌面端播放器                         | https://www.capricasoftware.co.uk/projects/vlcj              |
| room                      | 跨平台数据库orm组件                                      | https://developer.android.com/kotlin/multiplatform/room      |
| sqlite                    | 跨平台数据库                                           | https://developer.android.com/kotlin/multiplatform/sqlite    |
| datastore                 | 跨平台的KV存储组件                                       | https://developer.android.com/kotlin/multiplatform/datastore |
| kotlin serialization      | 跨平台的序列化框架，可支持json解析                              | https://kotlinlang.org/docs/serialization.html               |

# 2、踩坑记录

## 2.1 依赖问题

### 2.1.1 MacOS下的编译依赖问题

问题1：在Mac上编译时，使用implementation管理依赖，若不在composeApp目录下的build.gradle.kts中引用依赖，则会编译时或运行时找不到类。

解决方式：

    1、在composeApp目录下的build.gradle.kts中使用implementation引用丢失的依赖（优点：没有潜在的库版本冲突问题，缺点，kts文件巨大无比）
    2、常用基础组件包装为一个模块，以api方式引入第三方依赖（优点：包装后可做到开箱即用，不用过多配置，缺点：库版本冲突问题，不不过可用exclude解）

问题2: 依赖传递问题。

假设有三个模块 A/B/C，C依赖B,B依赖A，A和C都是支持跨android\ios\desktop的组件，但B不是，那C无法支持A中支持的能力，且在MacOS上无法编译通过，或编译后找不到类

解决方式:

    即使B中仅需要一个commonMain, 也需要将模块B也配置为支持跨所有平台的组件，在模块B的build.gradle.kts中声明对android\ios\desktop的支持

### 2.1.2 commonMain中无法使用java的类库

compose multiplatform不是完全基于jvm的，只有compose desktop, android基于jvm,

ios并不基于jvm,所以commomMain，iosMain无法使用java类库。desktopMain或androidMain则可以使用

### 2.1.3 桌面端的协程没有Dispatcher.Main问题

compose multiplatform的桌面端基于swing实现，需要特殊的dispatcher

[https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/-dispatchers/-main.html](https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/-dispatchers/-main.html)

### 2.1.4 ViewModel问题

compose multiplatform的 viewmodel目前是实验性的支持，在macos、ios上有一堆bug,如必须强制提供factory的问题，小版本不同也会有差异，建议先放弃官方的viewmodel

[https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-viewmodel.html](https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-viewmodel.html)


解决方法：

    1、使用voyager 的ScreenModel替代viewmodel
    2、等待viewmodel稳定下来，不再是实验性的

## 2.2 常用库或插件的配置问题

### 2.2.1 compose multiplatform 工程的ksp配置问题

官方文档:[https://kotlinlang.org/docs/ksp-overview.html#supported-libraries](https://kotlinlang.org/docs/ksp-overview.html#supported-libraries)

一些库依赖ksp生成代码，例如room等，但在compose multiplatform上有很多坑，官方文档不清晰，不正确配置无法实现预想的功能

compose multiplatform工程配置与KMM有点差异。需要先在使用ksp模块的build.gradle.kts中声明插件，然后在该kts顶级目录中通过add方式添加
kspDesktop、kspAndroid、kspIosArm64、kspIosX64、kspIosSimulatorArm64。实际例子可参考:[business:torrent:build.gradle.kts](business/torrent/build.gradle.kts)

```kotlin
plugin {
    alias(libs.plugins.ksp)
}
dependencies {
    add("kspDesktop", libs.roomCompiler)
    add("kspAndroid", libs.roomCompiler)
    add("kspIosSimulatorArm64", libs.roomCompiler)
    add("kspIosX64", libs.roomCompiler)
    add("kspIosArm64", libs.roomCompiler)
}

```

### 2.2.2 Room配置问题

room在android平台上使用起来比较方便，但在compose multiplatform中会有一堆坑。

实际例子可参考:

1、[business:torrent:build.gradle.kts](business/torrent/build.gradle.kts)

2、[framework:kmmdatabase:build.gradle.kts](framework/kmmdatabase/build.gradle.kts)

关键点

1、需要配置ksp、room插件

2、需要依赖room-runtime、room-paging两个库

3、需要在顶层声明room schema

4、KMM版room,定义database时，强制要求设置ConstructedBy，并且要声明一个expect object, 继承RoomDatabaseConstructor，但我们不能在代码里实现 actual object，原因是ksp插件会自动生成actual object
。不这样处理会导致编译错误或运行时错误

可参考

[com.home.torrent.collect.database.table.TorrentDatabase](business/torrent/src/commonMain/kotlin/com/home/torrent/collect/database/table/TorrentDatabase.kt)

[com.home.torrent.collect.database.table.TorrentDatabaseConstructor](business/torrent/src/commonMain/kotlin/com/home/torrent/collect/database/table/TorrentDatabaseConstructor.kt)

```kotlin
plugin {
    alias(libs.plugins.room)
    alias(libs.plugins.ksp)
}

kotlin {
    sourceSets {
        val desktopMain by getting
        commonMain.dependencies {
            api(libs.roomRuntime)
            api(libs.roomPaging)
        }
        iosMain.dependencies {
            api(libs.sqliteBundle)
        }

        desktopMain.dependencies {
            api(libs.sqliteBundle)
            implementation(libs.coroutineSwing)
        }
        
    }
}


room {
    schemaDirectory("$projectDir/schemas")
}
```

## 2.3 编译产物问题

### 2.3.1 编译目标平台错误的编译了另一个平台特有的库

gradle构建时会用到构建缓存，因此，需要在编译另一个平台产物时，先clean下之前构建的缓存


### 2.3.2 使用graalvm将desktop端的jar产物编译为二进制

在gradle task中，找到位于compose desktop中的packageUberJarForCurrentOS,双击可以生成jar包

jar包可在编译系统上通过java -jar xxx.jar运行

如果想将其编译成二进制可执行文件，可以使用graalvm的native-image命令编译,在高版本kotlin 或java上,
必须加--strict-image-heap参数，不然可能因为kotlin的新特性编译失败

[https://github.com/oracle/graal/issues/6957](https://github.com/oracle/graal/issues/6957)

```shell
native-image -jar xx.jar --strict-image-heap # mac/linux上
native-image.bat  -jar xx.jar --strict-image-heap # windows上
```





