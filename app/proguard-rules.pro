# 代码混淆配置
-dontobfuscate
-dontoptimize

# 保留模型类
-keep class com.smartclicker.app.model.* {
    *;
}

# 保留数据类
-keep class * extends kotlinx.serialization.Serializable

# 保留ViewModel
-keep class com.smartclicker.app.viewmodel.* {
    *;
}

# 保留Service
-keep class com.smartclicker.app.service.* {
    *;
}
