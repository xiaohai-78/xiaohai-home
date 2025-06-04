# 🤖 Xiaohai Home - Java Agent 实验项目

<div align="center">

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![Spring AI](https://img.shields.io/badge/Spring_AI-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![DeepSeek](https://img.shields.io/badge/DeepSeek-000000?style=for-the-badge&logo=deepseek&logoColor=white)

</div>

## 📖 项目简介

Xiaohai Home 是一个基于 Spring Boot 和 Spring AI 框架的 Java Agent 实验项目。该项目旨在探索和实践 AI 代理在 Java 应用中的各种可能性，为开发者提供一个灵活、可扩展的 AI 代理实现方案。

## ✨ 主要特性

- 🚀 基于 Spring Boot 3.5.0 和 Spring AI 1.0.0
- 🤖 支持多种 AI 模型集成（DeepSeek、OpenAI 等）
- 🔄 灵活的模型切换机制
- 📝 简洁的 API 设计
- 🔒 安全的配置管理

## 🛠️ 技术栈

- **核心框架**: Spring Boot 3.5.0
- **AI 框架**: Spring AI 1.0.0
- **AI 模型**: DeepSeek、OpenAI
- **构建工具**: Maven
- **JDK 版本**: Java 21

## 🚀 快速开始

### 环境要求

- JDK 21+
- Maven 3.6+
- Spring Boot 3.5.0+

### 配置说明

在 `application.yml` 中配置你的 AI 模型参数：

```yaml
spring:
  ai:
    openai:
      api-key: ${YOUR_API_KEY}
      base-url: https://dashscope.aliyuncs.com/compatible-mode
      chat:
        options:
          model: deepseek-r1
```

### 运行项目

```bash
# 克隆项目
git clone https://github.com/xiaohai-78/xiaohai-home.git

# 进入项目目录
cd xiaohai-home

# 编译运行
mvn spring-boot:run
```

## 📝 API 文档

### 聊天接口

```http
POST /chat/request
Content-Type: text/plain

你好，请介绍一下你自己
```

## 🎯 项目目标

1. 探索 Java Agent 在 AI 应用中的最佳实践
2. 提供可复用的 AI 代理实现方案
3. 支持多种 AI 模型的灵活切换
4. 构建安全、可靠的 AI 应用框架

## 🤝 贡献指南

欢迎提交 Issue 和 Pull Request 来帮助改进这个项目。在提交代码之前，请确保：

1. 代码符合项目的编码规范
2. 添加了必要的测试用例
3. 更新了相关文档

## 📄 开源协议

本项目采用 [Apache License 2.0](LICENSE) 开源协议。

## 👨‍💻 作者

- **xiaoyuntao** - [GitHub](https://github.com/xiaohai-78)

## 🙏 致谢

感谢所有为这个项目提供帮助和建议的开发者们！

---

<div align="center">
  <sub>Built with ❤️ by xiaoyuntao</sub>
</div> 