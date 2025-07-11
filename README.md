<p align="center">
   <img src="./Logo.png" width="200px" height="200px" alt="MHDF-Scheduler">
</p>

<div align="center">

# MHDF-Scheduler

_✨一款便携性的MC插件调度器✨_

_✨轻量 便携 快捷 支持Folia✨_

</div>

<p align="center">
    <a href="https://github.com/ChengZhiMeow/MHDF-Scheduler/issues">
        <img src="https://img.shields.io/github/issues/ChengZhiMeow/MHDF-Scheduler?style=flat-square" alt="issues">
    </a>
    <a href="https://github.com/ChengZhiMeow/MHDF-Scheduler/blob/main/LICENSE">
        <img src="https://img.shields.io/github/license/ChengZhiMeow/MHDF-Scheduler?style=flat-square" alt="license">
    </a>
    <a href="https://qm.qq.com/cgi-bin/qm/qr?k=T047YB6lHNMMcMuVlK_hGBcT5HNESxMA&jump_from=webapi&authKey=0/IFGIO6xLjjHB2YKF7laLxkKWbtWbDhb1lt//m7GgbElJSWdRZ8RjbWzSsufkO6">
        <img src="https://img.shields.io/badge/QQ群-129139830-brightgreen?style=flat-square" alt="qq-group">
    </a>
</p>

<div align="center">
    <a href="https://github.com/ChengZhiMeow/MHDF-Scheduler/pulse">
        <img src="https://repobeats.axiom.co/api/embed/e58f3e1358766291db33ba451d3e90be99811f4f.svg" alt="pulse">
    </a>
</div>

## maven配置

```xml
<repositories>
   <repository>
      <id>chengzhimeow-maven-repo-releases</id>
      <name>橙汁喵の仓库</name>
      <url>https://maven.chengzhimeow.cn/releases</url>
   </repository>
</repositories>

<dependencies>
   <dependency>
      <groupId>cn.chengzhiya</groupId>
      <artifactId>MHDF-Scheduler</artifactId>
      <version>1.0.1</version>
      <scope>compile</scope>
   </dependency>
</dependencies>
```

## 使用方法

1. 将本库导入至您的项目
2. 主线程执行Task:
   ```java
    MHDFScheduler.getGlobalRegionScheduler().runTask(this, () -> System.out.println("hello world!"));
   ```

## 贡献者

<a href="https://github.com/ChengZhiMeow/MHDF-Scheduler/graphs/contributors">
  <img src="https://stg.contrib.rocks/image?repo=ChengZhiMeow/MHDF-Scheduler" alt="contributors"/>
</a>

## 精神支柱

- [Xiao-MoMi](https://github.com/Xiao-MoMi)

## Star

[![Stargazers over time](https://starchart.cc/ChengZhiMeow/MHDF-Scheduler.svg?variant=adaptive)](https://starchart.cc/ChengZhiMeow/MHDF-Scheduler)

## 友链

<div>
    <a href="https://plugin.mhdf.cn/">插件文档</a>
    <a href="https://www.mhdf.cn/">梦回东方</a>
    <a href="https://www.yuque.com/xiaoyutang-ayhvn/rnr4ym/">鱼酱の萌新开服教程</a>
</div>
