# Spring Boot 后台管理系统

基于 Spring Boot 3 构建的后端管理系统，实现用户注册、登录、权限管理等核心功能。

## 技术栈

- **框架**：Spring Boot 3
- **数据库**：MySQL 8.0 + MyBatis
- **安全认证**：JWT 无状态 Token + BCrypt 密码加密
- **权限控制**：Spring Security

## 主要功能

- 用户注册（BCrypt 加密存储密码）
- 用户登录（返回 JWT Token）
- 用户列表查询（需携带 Token）
- 用户信息修改（需携带 Token）

## 接口说明

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | /user/register | 用户注册 |
| POST | /user/login | 用户登录，返回Token |
| GET  | /user/list | 查询用户列表 |
| PUT  | /user/update | 修改用户信息 |

## 运行方式

1. 导入数据库：创建 MySQL 数据库，执行建表语句
2. 修改配置：编辑 `src/main/resources/application.properties`，填写数据库账号密码
3. 启动项目：运行 `DemoApplication.java`
4. 接口测试：使用 Postman 访问 `http://localhost:8080`
