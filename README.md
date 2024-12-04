<p align="center">
<a href="https://zjxzjw.github.io/Ananas-UI/#/ananas">
  <text style="font-size: 40px; font-weight: bold; color: #f1d270;">Ananas</text>
  <text style="font-size: 40px; font-weight: bold; color: #95cf88">Lock</text>
</a>
</p>

# 介绍

> 优雅的本地/分布式锁注解 不需要手动控制锁 执行 和 Try Catch
> 
> 如果开启分布式锁需要引入 redisson-spring-boot-starter 依赖 并配置

## 安装教程

```bash
<dependency>
    <groupId>io.github.zjxzjw</groupId>
    <artifactId>ananas-lock-spring-boot-starter</artifactId>
    <version>0.0.1</version>
</dependency>
```

## 使用教程

### Yml配置

```javascript
ananas:
  lock:
    #指定锁前缀
    key-prefix: ananas:lock
    #指定锁类型
    storage-type: local
```

### 使用方式
#### 开启锁
> @EnableAnanasLock(order = 1)

```javascript
package com.knife.example;

import cn.zjx.ananas.annotation.EnableAnanasLock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAnanasLock(order = 1)
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}
```

#### 使用锁
> @AnanasLock(keys = {"#name"})

```
@AnanasLock(keys = {"#name"})
public String test(name) {
    return "success";
}
```
