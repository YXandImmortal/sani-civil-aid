# 典亮撒尼 · 彝族民事法律援助系统

<p align="center">
  <img src="frontend/public/logo.svg" alt="典亮撒尼" width="120" />
</p>

<p align="center">
  <strong>汉彝双语民事法律援助平台</strong><br>
  让民法典走进彝族村寨，让法律服务跨越语言障碍
  🌐 官网：<a href="https://sani-civil-aid.lstudio.top" target="_blank">https://sani-civil-aid.lstudio.top</a>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Vue-3.5-%234FC08D?logo=vue.js" alt="Vue 3" />
  <img src="https://img.shields.io/badge/Vite-8.0-%23646CFF?logo=vite" alt="Vite" />
  <img src="https://img.shields.io/badge/Spring_Boot-4.0-%236DB33F?logo=spring-boot" alt="Spring Boot" />
  <img src="https://img.shields.io/badge/Java-25-%23ED8B00?logo=openjdk" alt="Java" />
  <img src="https://img.shields.io/badge/MySQL-8.0-%234479A1?logo=mysql" alt="MySQL" />
  <img src="https://img.shields.io/badge/license-MIT-blue.svg" alt="License" />
</p>

---

## 📖 项目简介

**典亮撒尼**（Sani Civil Aid）是一款面向彝族（撒尼支系）群众的民事法律援助系统。项目采用前后端分离架构，提供汉彝双语界面，旨在降低少数民族群众获取法律服务的语言门槛，让《民法典》真正成为保障彝族群众合法权益的"社会百科全书"。

> "典亮"寓意以《民法典》之光照亮民族地区法治进程，"撒尼"则是彝族的一个重要支系，主要分布于云南石林、弥勒等地。

## ✨ 核心功能

| 功能模块 | 说明 | 访问权限 |
|---------|------|---------|
| 🤖 智能法律咨询 | 用户在线提交民事法律问题，系统优先匹配 FAQ 库，未命中时调用 DeepSeek AI 生成汉彝双语专业解答 | 公开浏览，提问需登录 |
| 📚 词汇对照表 | 民法典常用法律术语汉彝双语对照，支持实时搜索 | 完全公开 |
| 🔍 法条查询 | 民法典法条全文检索，支持分类浏览与 AI 智能解读 | 完全公开 |
| 🔤 字体管理 | 本地 Yi Script 彝文字体加载开关、字号/行高自定义调节 | 完全公开 |
| 👤 用户中心 | 个人信息管理、登录/注册、密码修改、图形验证码 | 需登录 |
| 🌓 主题切换 | 支持浅色/深色（暗黑）模式，适配不同使用环境 | 完全公开 |

## 🖼️ 界面预览

> 系统采用彝族崇尚的黑色系作为主色调，所有核心界面文本均提供汉彝双语对照。

```
┌─────────────────────────────────────────────────────────┐
│  🌙  典亮撒尼 · 彝文注释                                │
│  ─────────────────────────────────────────────────────  │
│  ┌────────┐  ┌────────────────────────────────────────┐ │
│  │💬 法律咨询 │  智能法律咨询 · 彝文注释              │ │
│  │📖 词汇对照 │                                      │ │
│  │🔍 法条查询 │  [我要提问 · 彝文注释]                │ │
│  │🔤 字体管理 │                                      │ │
│  │👤 个人中心 │  ┌──────────────────────────────────┐│ │
│  │ℹ️ 关于系统 │  │ 问：借款不还怎么办？             │ │ │
│  └────────┘      │ 答：根据《民法典》第675条...     │ │ │
│                  │ 状态：已回答         2025-05-12  │ │ │
│                  └──────────────────────────────────┘ │ │
│                                                       │ │
└─────────────────────────────────────────────────────────┘
```

## 🏗️ 技术架构

本项目采用前后端分离架构，分为三个核心模块：

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│    前端前端     │     │     后端后端     │     │     数据数据    │
│   Vue 3 + Vite  │◄───►│  Spring Boot 4  │◄───►│     MySQL 8     │
│  Element Plus   │     │  Spring Security│     │   JPA / JDBC    │
│   Pinia + Axios │     │   JWT + JJWT    │     │   DDL Scripts   │
│   Yi Script Font│     │  DeepSeek AI    │     │   FAQ / Terms   │
└─────────────────┘     └─────────────────┘     └─────────────────┘
```

### 前端技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| Vue | 3.5.32 | 框架，采用 Composition API |
| Vite | 8.0.8 | 构建工具 |
| Element Plus | 2.13.7 | UI 组件库 |
| Pinia | 3.0.4 | 状态管理 |
| Vue Router | 5.0.4 | 前端路由 |
| Axios | - | HTTP 客户端 |
| SCSS | - | 样式预处理器 |

### 后端技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| Spring Boot | 4.0.6 | 核心框架 |
| Java | 25 | 开发语言 |
| Spring Data JPA | - | ORM 数据访问 |
| Spring Security | - | 安全认证与授权 |
| Spring Validation | - | 参数校验 |
| MySQL Connector/J | - | MySQL 驱动 |
| JJWT | 0.13.0 | JWT Token 生成与验证 |
| Lombok | - | 简化代码 |
| Hutool | 5.8.44 | 中文分词、JSON 处理等工具 |
| DeepSeek API | - | AI 大模型接口（汉彝双语生成） |

## 📁 项目结构

```
sani-civil-aid/
├── frontend/                   # 前端源码（Vue 3）
│   ├── public/                 # 静态资源（不经过构建）
│   │   ├── logo.svg
│   │   └── icon.ico
│   ├── src/
│   │   ├── api/                # API 接口封装
│   │   ├── assets/             # 构建资源（字体、样式、图片）
│   │   ├── components/         # 公共组件
│   │   ├── composables/        # 组合式函数
│   │   ├── layout/             # 布局组件
│   │   ├── router/             # 路由配置
│   │   ├── stores/             # Pinia 状态管理
│   │   ├── utils/              # 工具函数（Axios 封装）
│   │   ├── views/              # 页面视图
│   │   ├── App.vue
│   │   └── main.js
│   ├── index.html
│   ├── vite.config.js          # Vite 配置（含开发代理）
│   └── package.json
│
├── backend/                    # 后端源码（Spring Boot）
│   ├── src/main/java/com/idtech/sanicivilaid/
│   │   ├── config/             # 安全配置
│   │   ├── controller/         # REST API 控制器
│   │   ├── service/            # 业务逻辑层
│   │   ├── repository/         # 数据访问层（JPA）
│   │   ├── entity/             # 实体类
│   │   ├── dto/                # 请求数据传输对象
│   │   ├── vo/                 # 响应视图对象
│   │   ├── exception/          # 全局异常处理
│   │   ├── security/           # JWT 过滤器
│   │   ├── util/               # 工具类（JWT、验证码）
│   │   └── BackendApplication.java
│   ├── src/main/resources/
│   │   ├── application.yaml    # 应用配置
│   │   └── logback-spring.xml  # 日志配置
│   ├── src/test/               # 单元测试
│   ├── pom.xml                 # Maven 依赖
│   └── mvnw / mvnw.cmd         # Maven Wrapper
│
├── database/                   # 数据库脚本
│   ├── sani_civil_aid_db.sql   # 完整数据库初始化脚本（推荐）
│   └── nuosu_civil_aid_db.sql  # 历史版本脚本
│
├── 彝文翻译清单.txt             # 彝文翻译文本记录
├── 彝文翻译清单.xlsx            # 彝文翻译 Excel 清单
└── README.md                   # 本文件
```

## 🚀 快速开始

### 环境要求

| 组件 | 版本要求 |
|------|---------|
| Node.js | `^20.19.0 \|\| >=22.12.0` |
| Java | 25 |
| Maven | 3.8+ |
| MySQL | 8.0+ |

### 1. 初始化数据库

```bash
# 创建数据库（默认名 sani_civil_aid_db）
mysql -u root -p -e "CREATE DATABASE sani_civil_aid_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"

# 导入初始化脚本
mysql -u root -p sani_civil_aid_db < database/sani_civil_aid_db.sql
```

### 2. 启动后端服务

```bash
cd backend

# 方式一：使用 Maven Wrapper
./mvnw spring-boot:run

# 方式二：使用本地 Maven
mvn spring-boot:run

# 方式三：先打包再运行
./mvnw clean package
java -jar target/sanicivilaid-*.jar
```

后端服务默认启动在 `http://localhost:8080/api`，所有 API 均以 `/api` 为上下文路径。

> **配置说明**：`application.yaml` 中支持通过环境变量配置数据库连接和 JWT 密钥：
> - `DB_USERNAME` / `DB_PASSWORD` — 数据库账号密码
> - `JWT_SECRET` / `JWT_EXPIRATION` — JWT 签名密钥与有效期
> - `DEEPSEEK_API_KEY` / `DEEPSEEK_API_URL` — AI 接口配置（默认已内置）

### 3. 启动前端开发服务器

```bash
cd frontend

# 安装依赖
npm install

# 启动开发服务器
npm run dev
```

前端服务默认启动在 `http://localhost:5173`，开发服务器已配置代理，将 `/api` 转发至后端 `http://localhost:8080/api`。

### 4. 生产构建

```bash
# 前端生产构建
cd frontend
npm run build
# 构建产物输出至 frontend/dist/ 目录

# 后端生产构建
cd backend
./mvnw clean package
# 构建产物输出至 backend/target/sanicivilaid-*.jar
```

生产环境建议将 `frontend/dist` 部署至 Nginx，并通过反向代理将 `/api` 请求转发至后端服务。

## 🔌 API 接口概览

后端所有接口前缀为 `/api`。

### 用户认证

| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| POST | `/auth/login` | 用户登录 | 否 |
| POST | `/auth/register` | 用户注册 | 否 |
| GET | `/auth/info` | 获取当前用户信息 | 是 |
| PUT | `/auth/info` | 修改用户信息（自动刷新 Token） | 是 |
| POST | `/auth/change-password` | 修改密码 | 是 |

### 验证码

| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| GET | `/captcha/generate` | 获取图形验证码（Base64） | 否 |
| POST | `/captcha/verify` | 校验验证码 | 否 |

### 智能法律咨询

| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| POST | `/civil/consultation/submit` | 提交咨询（FAQ 优先，AI 兜底） | 可选 |
| GET | `/civil/consultation/my-list` | 获取我的咨询历史 | 是 |
| DELETE | `/civil/consultation/delete/{id}` | 删除咨询记录（逻辑删除） | 是 |

### 法条查询

| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| GET | `/civil/article/search` | 本地数据库法条关键词搜索 | 否 |
| GET | `/civil/article/category/{id}` | 按分类获取法条列表 | 否 |
| GET | `/civil/article/ai-search` | DeepSeek AI 智能解读（汉彝双语） | 否 |

### 词汇对照

| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| GET | `/civil/term/list` | 法律术语列表（支持关键词搜索） | 否 |

### 系统信息

| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| GET | `/system/info` | 获取系统基本信息与技术栈 | 否 |

## 🔤 彝文字体说明

系统支持加载本地 Yi Script 彝文字体，用户可在「字体管理」页面：

- **启用/禁用**本地彝文字体（禁用后回退至系统默认字体）
- **调节字号**：12px ~ 32px 无级缩放
- **调节行高**：1.0 ~ 2.5 倍行距
- **自定义预览文本**：实时查看字体渲染效果

字体设置通过 Pinia Store 持久化到 `localStorage`，刷新页面后自动恢复。

## 🌓 主题切换

系统内置浅色与深色两套主题：

- 切换按钮位于顶部导航栏右侧（🌞 / 🌙 图标）
- 主题状态同步至 Element Plus 的暗黑模式 CSS 变量
- 偏好设置保存在 `localStorage` 中

## 🤖 AI 智能解答说明

系统集成了 **DeepSeek** 大模型 API，用于以下场景：

1. **法律咨询**：当用户提交的问题在 FAQ 库中未匹配到答案时，系统自动调用 DeepSeek，根据《民法典》生成**汉彝双语**专业解答。
2. **法条解读**：当本地数据库未收录用户查询的法条时，可通过 AI 检索获取汉彝双语解释。

AI 返回内容通过 `contentCn`（汉文）和 `contentSani`（彝文）字段与前端对接，确保双语展示一致性。

## 🤝 参与贡献

彝文翻译是本项目的核心工作之一。如发现界面中彝文翻译有误，或希望补充新的法律术语翻译，欢迎：

1. Fork 本仓库
2. 在 `frontend/src/views/` 及相关组件中修改对应 `.yi-placeholder` 文本
3. 同时更新 `彝文翻译清单（已完成）.txt` 中的记录
4. 提交 Pull Request

## 📄 许可证

[MIT](LICENSE) © 典亮撒尼项目组

<p algin="center">
  <sub>本工作由云南省教育厅科学研究基金项目（编号：2025J1331）资助。</sub>
  <sub>This work is supported by Yunnan Provincial Education Department Science Foundation of China under Grant 2025J1331.</sub>
</p>

---

<p align="center">
  <sub>让法律之光，照亮每一个民族村寨 🏔️</sub>
  <sub>🌐 官网：<a href="https://sani-civil-aid.lstudio.top" target="_blank">https://sani-civil-aid.lstudio.top</a></sub>
</p>
