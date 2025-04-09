# 文档系统接口说明文档 (前端)

## 一、WebSocket 通信规范

### 1.1 连接信息
- 连接地址：`ws://${host}:${port}/websocket/document`
- 认证参数：`token` (URL参数)
- 协议：WebSocket

### 1.2 消息类型定义

#### 发送消息格式
```json
{
  "type": "document-update",
  "content": {
    "documentId": "number?",     // 文档ID，创建时可选
    "parentId": "number?",       // 父级文档ID，顶级为0
    "title": "string?",          // 文档标题
    "content": "string?",        // 文档内容
    "status": "number?",         // 文档状态
    "version": "number?",        // 版本号
    "operateType": "number"      // 操作类型
  }
}
```

#### 接收消息格式
```json
{
  "type": "document-notify",
  "content": {
    "userId": "number",          // 操作用户ID
    "userNickname": "string?",   // 用户昵称
    "documentId": "number",      // 文档ID
    "parentId": "number",        // 父级文档ID
    "title": "string?",          // 文档标题
    "content": "string?",        // 文档内容
    "status": "number?",         // 状态
    "version": "number?",        // 版本号
    "operateType": "number",     // 操作类型
    "operateTime": "number"      // 时间戳
  }
}
```

### 1.3 操作类型说明
| operateType | 说明 | 备注 |
|-------------|------|------|
| 1 | 创建文档 | 新建文档时使用 |
| 2 | 更新文档 | 需要版本号 |
| 3 | 删除文档 | 会级联删除子文档 |
| 4 | 自动保存 | 不更新版本号 |

### 1.4 文档状态说明
| status | 说明 |
|--------|------|
| 0 | 草稿 |
| 1 | 已发布 |
| 2 | 已归档 |

## 二、HTTP接口规范

### 2.1 文档分页查询
- 请求路径：`GET /api/infra/document/page`
- 请求参数：
```json
{
  "parentId": "number?",    // 父级文档ID
  "title": "string?",       // 标题关键字
  "status": "number?",      // 状态
  "pageNo": "number",       // 页码
  "pageSize": "number"      // 每页条数
}
```
- 响应格式：
```json
{
  "list": [{
    "id": "number",         // 文档ID
    "parentId": "number",   // 父级文档ID
    "title": "string",      // 标题
    "content": "string",    // 内容
    "status": "number",     // 状态
    "version": "number",    // 版本号
    "lastUpdatedBy": "number", // 最后更新人
    "createTime": "string", // 创建时间
    "updateTime": "string"  // 更新时间
  }],
  "total": "number"         // 总条数
}
```

### 2.2 获取子文档列表
- 请求路径：`GET /api/infra/document/children/{parentId}`
- 响应格式：
```json
{
  "list": [{
    "id": "number",         // 文档ID
    "parentId": "number",   // 父级文档ID
    "title": "string",      // 标题
    "status": "number"      // 状态
  }]
}
```

## 三、功能实现指南

### 3.1 文档树实现
1. 数据结构
   - 使用 parentId 构建树形结构
   - 顶级文档 parentId = 0
   - 子文档 parentId 为父文档ID

2. 推荐实现方式
   - 初始只加载顶级文档
   - 展开节点时按需加载子文档
   - 本地缓存已加载的文档树
   - 定期或操作后刷新缓存

### 3.2 实时保存机制
1. 触发时机
   - 用户停止输入后延迟触发（建议3秒）
   - 切换文档前触发
   - 页面关闭前触发

2. 保存策略
   - 普通保存：完整保存，更新版本号
   - 自动保存：仅保存内容，不更新版本号
   - 建议使用防抖处理

### 3.3 协同编辑处理
1. 数据同步
   - 监听文档更新通知
   - 根据 operateType 处理不同操作
   - 处理版本冲突提示

2. 冲突处理
   - 版本号不匹配时提示刷新
   - 自动保存冲突时保留最新
   - 显示最后编辑人信息

## 四、注意事项

### 4.1 性能优化
1. 文档树缓存
2. 大文档分段加载
3. 自动保存节流控制
4. WebSocket 心跳检测

### 4.2 异常处理
1. 网络断开重连机制
2. 保存失败重试策略
3. 版本冲突提示
4. 离线数据处理

### 4.3 用户体验
1. 保存状态实时展示
2. 协作者信息显示
3. 操作结果及时反馈
4. 断线重连提示

## 五、集成建议

### 5.1 开发准备
1. WebSocket 连接管理
2. 状态管理方案
3. 缓存策略
4. 异常处理方案

### 5.2 功能实现顺序
1. 基础文档管理
2. 树形结构支持
3. 实时保存机制
4. 协同编辑功能
5. 异常处理优化

### 5.3 测试重点
1. 多人协作场景
2. 网络异常情况
3. 大文档性能
4. 并发操作处理

## 六、其他说明

### 6.1 版本控制
- 版本号从1开始递增
- 每次更新+1
- 自动保存不更新版本号

### 6.2 权限说明
- 需要登录态
- WebSocket需要token认证
- 文档操作需要相应权限

### 6.3 数据安全
- 敏感数据加密传输
- 定期保存备份
- 异常时数据恢复 