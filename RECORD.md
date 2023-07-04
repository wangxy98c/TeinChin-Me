# 后端相关



# 前端相关

###  npm run dev

![image-20230704133315622](/Users/wangxy/project/TeinChin/RECORD.assets/image-20230704133315622-8448973.png)

每次都需要:`export NODE_OPTIONS=--openssl-legacy-provider`后再`npm run dev`



# 代码格式化

### format code

注意filter java类型的。其他类型的动了容易出问题

### 项目全局替换

1. 包名: com.ruoyi => com.wangxy.teinchin
1. com.ruoyi => com.wangxy
1. 版本号: 3.8.6 => 0.0.1
1. ruoyi => teinchin
1. 若依 => 天勤
1. 模块名：rename and directory
1. 项目名
1. com.wangxy.framework.config.CaptchaConfig 中修改KaptchaTextCreator地址
1. common 中 config 的ruoyiconfig => teinchinconfig
1. 重新打开项目
1. 前端 src/utils/ruoyi.js => teinchin.js
1. src/assets/styles/ruoyi.scss => teinchin.scss

