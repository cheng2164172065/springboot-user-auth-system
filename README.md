# SpringBoot用户认证管理系统

## 项目介绍

基于 Spring Boot 开发的用户认证管理系统，实现用户注册、登录、用户信息查询以及密码修改等功能。

项目采用前后端分离思想进行设计，通过 DTO 接收前端请求参数，通过 VO 返回用户数据，提高系统安全性和代码可维护性。

## 技术栈

- Java 17
- Spring Boot 3.5.14
- Spring Data JPA
- MySQL
- JWT
- BCrypt
- Maven
- Redis
- Spring Security

## 核心功能

### 用户模块

- 用户注册
- 用户登录
- 用户信息查询
- 用户密码修改

### 认证与安全

- Spring Security 接口访问控制
- JWT 无状态身份认证
- 自定义 JWT 认证过滤器
- BCrypt 加密存储用户密码
- 登录与注册接口免认证访问

### Redis 缓存

- 用户信息查询缓存
- 缓存过期机制
- 用户数据变更后的缓存失效

### 异常处理

- 自定义业务异常
- 全局异常处理
- 统一返回 Result 格式

### 数据安全

- DTO 接收前端参数
- VO 返回用户数据
- 密码不直接返回

## 项目结构

```
com.example.demo
├── common
│   └── Result                     # 统一返回结果
├── config
│   ├── PasswordConfig              # BCrypt配置
│   └── RedisConfig                 # Redis配置
|   └── SecurityConfig              # 安全配置                          
├── controller
│   └── UserController              # 用户接口
├── dto
│   ├── LoginDTO                    # 登录参数
│   ├── RegisterDTO                 # 注册参数
│   └── UpdateDTO                   # 修改密码参数
├── entity
│   └── User                        # 用户实体
├── exception
│   ├── BusinessException           # 业务异常
│   └── GlobalExceptionHandler      # 全局异常处理
├── repository
│   └── UserRepository              # 数据访问层
├── service
│   ├── UserService                 # 服务接口
│   └── impl
│       └── UserServiceImpl         # 服务实现
├── constant
│   └── RedisConstant               # Redis缓存Key常量
├── filter
│   └── JwtAuthenticationFilter     # JWT认证过滤器
├── util
│   └── JwtUtil                     # JWT工具类
└── vo
    └── UserVO                      # 用户返回对象
```

## 项目启动

### 环境要求

- JDK 17+
- MySQL 8.0+
- Redis 6.0+
- Maven

### 数据库配置

创建数据库：

```sql
CREATE DATABASE demo;
```

修改 `application.properties`：

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/demo?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
spring.datasource.username=root
spring.datasource.password=your_password
spring.data.redis.host=127.0.0.1
spring.data.redis.port=6379
```

### 启动方式

运行 `DemoApplication.java`，或者执行：

```bash
mvn spring-boot:run
```

## 接口说明

### 用户注册

**请求**

```
POST /user/register
```

**请求参数**

```json
{
  "username": "test",
  "password": "123456"
}
```

**功能**

- 校验用户名是否存在
- BCrypt 加密密码
- 保存用户信息

### 用户登录

**请求**

```
POST /user/login
```

**请求参数**

```json
{
  "username": "test",
  "password": "123456"
}
```

**功能**

- 查询用户信息
- 校验用户密码
- 登录成功生成 JWT Token

### 查询用户

**请求**

```
GET /user/list
```http
Authorization: Bearer <JWT_TOKEN>

**功能**

- 校验 JWT 身份认证
- 查询用户列表
- 使用 UserVO 返回数据
- 隐藏密码等敏感信息
- 优先从 Redis 获取缓存数据

### 修改密码

**请求**

```
PUT /user/update
```http
Authorization: Bearer <JWT_TOKEN>

**请求参数**

```json
{
  "username": "test",
  "password": "123456"
}
```

**功能**

- 校验 JWT 身份认证
- 修改用户密码
- 更新数据库后清理对应 Redis 缓存

## 项目特点

- 使用 Controller-Service-Repository 分层架构
- 使用 DTO/VO 实现请求数据与数据库实体分离
- 使用 Spring Security + JWT 实现无状态身份认证
- 通过自定义 JWT Filter 完成 Token 解析与接口访问控制
- 使用 Redis 缓存用户查询数据，并在数据变更后主动失效缓存
- 使用 BCrypt 实现密码加密存储
- 使用全局异常处理统一管理业务异常

## 后续优化方向

- 增加用户角色与权限管理
- 增加参数校验及异常统一处理
- 增加操作日志与审计记录
- 完善 Redis 缓存策略及异常降级机制
- 增加接口限流等安全机制
