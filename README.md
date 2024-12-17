

## mee_timed
mee_timed 为 scheduled 及 shedlock 做的二开，其中主目录下：

[mee-timed](mee-timed) ：为 scheduled + shedlock 做的二开（推荐)

[mee-timed-test](mee-timed-test) ： 是对mee_timed做的springboot兼容测试代码

[shedlock](shedlock) : 是对shedlock做的二开相关代码(不推荐)


### [mee-timed](mee-timed) 是本工程的核心
具体请见介绍[README.md](mee-timed%2FREADME.md)

## how to use 

```
<dependency>
    <groupId>io.github.funnyzpc</groupId>
    <artifactId>mee-timed</artifactId>
    <version>1.0.1</version>
</dependency>
```