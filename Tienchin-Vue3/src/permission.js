import router from './router'
import { ElMessage } from 'element-plus'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import { getToken } from '@/utils/auth'
import { isHttp } from '@/utils/validate'
import { isRelogin } from '@/utils/request'
import useUserStore from '@/store/modules/user'
import useSettingsStore from '@/store/modules/settings'
import usePermissionStore from '@/store/modules/permission'

NProgress.configure({ showSpinner: false });

const whiteList = ['/login', '/register'];

router.beforeEach((to, from, next) => {
  NProgress.start()//进度条
  if (getToken()) {//从Cookie中拿到JWT令牌，作为已经登陆的依据
    to.meta.title && useSettingsStore().setTitle(to.meta.title)
    /* has token*/
    if (to.path === '/login') {//登陆后还去登陆页面，不合理。直接进主页
      next({ path: '/' })
      NProgress.done()
    } else {
      if (useUserStore().roles.length === 0) {//刚登陆还没请求用户信息时（动态菜单也没请求）/刷新后。 可以通过它判断从后端加载的动态菜单是否还存在
        isRelogin.show = true
        // 判断当前用户是否已拉取完user_info信息
        useUserStore().getInfo().then(() => {//加载完用户信息后，加载路由
          isRelogin.show = false
          usePermissionStore().generateRoutes().then(accessRoutes => {
            //console.log("前置守卫====>accessRoutes:",accessRoutes)
            // 根据roles权限生成可访问的路由表
            accessRoutes.forEach(route => {
              if (!isHttp(route.path)) {
                router.addRoute(route) // 动态添加可访问路由表。且由于addRoute函数是会递归处理children的，所以不只是添加外层的还包括children里的
              }
            })
            //console.log("前置守卫===>getAllRoutes:",router.getRoutes())
            next({ ...to, replace: true }) // hack方法 确保addRoutes已完成
          })
        }).catch(err => {
          useUserStore().logOut().then(() => {
            ElMessage.error(err)
            next({ path: '/' })
          })
        })
      } else {//菜单、router之类的都在，直接进入就行
        next()
      }
    }
  } else {
    // 没有token，视为没登陆。如果不需要登陆就可以去。否则重定向到登陆页
    if (whiteList.indexOf(to.path) !== -1) {
      // 在免登录白名单，直接进入
      next()
    } else {
      next(`/login?redirect=${to.fullPath}`) // 否则全部重定向到登录页
      NProgress.done()
    }
  }
})

router.afterEach(() => {
  NProgress.done()
})
