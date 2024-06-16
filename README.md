## FileCloud

项目说明：文件上传+文件管理



快速上手

> 查找TODO，根据TODO提示完成

application.yml

- 修改数据库并配置数据库
- 上传文件配置



上传后得到图片结果(例子)：http://127.0.0.1:8088/api/file/2023/06/05/f98bddb0-e548-4399-868c-536a23ff8773.png



> 注意：路径正确(如/api)



## 文件上传配置

yml配置允许支持上传的文件类型

```
file:
  # 允许文件后缀
  suffers:
    - .jpg
    - .png
    - .gif
    - .bmp
    - .jpeg
    - .svg
```



`FileProperties.savePath`设置保存文件路径



## 具体实现

- 配置静态资源映射(CorsConfig重写addResourceHandlers)

- 配置文件常量FileProperties(配置保存文件路径和允许文件后缀)
- yml允许支持上传的文件后缀
- 实体类FileInfo
- 编写Controller和service


