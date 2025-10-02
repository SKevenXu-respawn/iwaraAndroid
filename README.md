# Iwara-Android  
「零依赖、纯 Kotlin / Java」实现的 Iwara 客户端 Demo，支持分页列表、下拉刷新、触底加载、视频直链解析、ExoPlayer 播放、时间格式化、空安全等一站式示例。

---

## ✨ 主要功能
- 首页视频列表（分页加载 + 下拉刷新 + 触底加载更多）
- 真实 MP4/M3U8 直链解析，系统播放器/ExoPlayer 一键播放
- 时间格式化：ISO-8601 → `yyyy年MM月dd日HH时mm分`
- 空安全：接口字段缺失或 null 自动降级，无 Crash
- 图片加载：用户头像、封面占位图自动兜底
- 开源播放器插件：ExoPlayer / Just Player / VLC 任选
- 多平台构建：Android / HarmonyOS / Flutter 均可参考

---

## 📸 截图
| 列表页 | 播放页 | 头像占位 |
| :----: | :----: | :------: |
| ![list](docs/list.jpg) | ![player](docs/player.jpg) | ![avatar](docs/avatar.jpg) |

---

## 🚀 快速开始
1. **Clone 代码**
```bash
git clone https://github.com/YOUR_NAME/Iwara-Android.git
cd Iwara-Android
