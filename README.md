### 功能介绍

主要提供 :
1. 应用内网下载功能
2. 应用日志查看功能
     通用的应用日志实时查看功能，同时提供日志上传下载功能。

### 项目目录说明

项目采用组件化方式进行
 1.  主UI模块App
       
 2. 次UI模块以M字母开头，MLocalLog,MRemoteLog.在编译期根据需求引入。
     1. gradle.properties文件配置组件(如MlocalLog,MRemoteLog)是否引入，在App的build.gradle会读取该配置文件动态引入对应组件
     2. settings.gradle 需要去除对应module的编译,组件化的目的就是减少开发时编译时间，仅编译需要的模块。
 
 3. AppUI为各UI模块提供界面相关可复用工具
 
 4. AppService提供网络接口服务及数据库服务
      
 5. AppCommon提供各类非UI相关可复用工具
      
 6. docs  项目相关内容文档集
 
### 其他说明

#### 1. 引入kotlin  说明

#### 2. 引入dagger2 说明

#### 3. 网络层封装说明
   