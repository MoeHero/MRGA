package io.kazutoiris.mrga

import com.highcapable.yukihookapi.annotation.xposed.InjectYukiHookWithXposed
import com.highcapable.yukihookapi.hook.factory.configs
import com.highcapable.yukihookapi.hook.factory.encase
import com.highcapable.yukihookapi.hook.factory.method
import com.highcapable.yukihookapi.hook.log.YLog
import com.highcapable.yukihookapi.hook.type.android.ActivityClass
import com.highcapable.yukihookapi.hook.xposed.proxy.IYukiHookXposedInit

@InjectYukiHookWithXposed
object HookEntry : IYukiHookXposedInit {
    override fun onInit() = configs {
        isDebug = false
    }

    override fun onHook() = encase {
        loadApp {
            "com.bilibili.lib.blkv.internal.sp.BatchedSpImpl".toClass().method {
                name = "getBoolean"
            }.hookAll {
                before {
                    if (args(0).string().startsWith("user_blocked")) {
                        result = false
                    }
                }
            }.onAllFailure { YLog.error(it.toString()) }

            "app.revanced.bilibili.patches.main.ApplicationDelegate".toClass().method {
                returnType = ActivityClass
            }.hook {
                replaceTo(null)
            }.onAllFailure { YLog.error(it.toString()) }
        }
    }
}