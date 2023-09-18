import auth from '@/plugins/auth'
import router, { constantRoutes, dynamicRoutes } from '@/router'
import { getRouters } from '@/api/menu'
import Layout from '@/layout/index'
import ParentView from '@/components/ParentView'
import InnerLink from '@/layout/components/InnerLink'

/**
 * 整体思路再理一下吧（暂不考虑铺平的情况）
 * 1. 从后台拿到router格式数据后，先格式化为json字符串（废话）
 * 2. 然后filterAsyncRouter把数据都递归处理好（处理过程中把那些本身没有导入的页面通过后段数据中的路径字段来导入import）
 * 3. 此文件代码还会根据权限把菜单处理好（/src/permission会根据这里存储的内容来完成页面的add，先调用generateRoutes再根据返回结果继续处理）
 * 
 * > 至于后台定义返回的菜单，实际的addRoute则是在/scr/permission中来完成
 */
// 匹配views里面所有的.vue文件
const modules = import.meta.glob('./../../views/**/*.vue')

const usePermissionStore = defineStore(
  'permission',
  {
    state: () => ({
      routes: [],//在“首页搜索（页面搜索）;tagview”中使用。并不涉及到页面渲染，单纯处理页面的逻辑。这个routes中的children铺平了（3-n级全部变为2级菜单，且path需要修改用于搜索）
      addRoutes: [],//暂时没用到的东西，可删
      defaultRoutes: [],//动态路由的备份（完整的<会变化的sidebarRoutes>的备份）关闭顶部菜单时需要用它来恢复
      topbarRouters: [],
      sidebarRouters: []//左侧导航栏，参与渲染。它会发生变化：如果开启顶部菜单，它是2-n级的菜单（即只显示当前菜单下的子菜单）
    }),
    actions: {
      setRoutes(routes) {
        this.addRoutes = routes
        this.routes = constantRoutes.concat(routes)//constantRoutes公共路由
      },
      setDefaultRoutes(routes) {
        this.defaultRoutes = constantRoutes.concat(routes)
      },
      setTopbarRoutes(routes) {
        this.topbarRouters = routes
      },
      setSidebarRouters(routes) {
        this.sidebarRouters = routes
      },
      generateRoutes(roles) {
        return new Promise(resolve => {
          // 向后端请求路由数据，收到的数据即数据库中的页面
          getRouters().then(res => {//getRouters封装后的请求方法，res是返回的JSON
            //三个变量用于深拷贝。为了不指向同一内存地址。其实也可以Object.assign来深拷贝
            const sdata = JSON.parse(JSON.stringify(res.data))
            const rdata = JSON.parse(JSON.stringify(res.data))
            const defaultData = JSON.parse(JSON.stringify(res.data))

            const sidebarRoutes = filterAsyncRouter(sdata)
            const rewriteRoutes = filterAsyncRouter(rdata, false, true)//重写后的routes用来将3-n级菜单变为2级菜单（铺平）
            const defaultRoutes = filterAsyncRouter(defaultData)
            const asyncRoutes = filterDynamicRoutes(dynamicRoutes)//根据用户权限查询出’本地定义的,可用的‘路由
            //console.log("===>getAllRoutes:",router.getRoutes())
            //console.log("====>asyncRoutes:",asyncRoutes);
            //其实这里的是已经在前端定义好的那些动态路由（为什么动态呢？因为需要根据权限来决定是否要显示出来）
            //后端传过来的路由的add在另一个permission中（/src/）
            asyncRoutes.forEach(route => {console.log(route);router.addRoute(route) })
            //console.log("generateRoutes===>getAllRoutes:",router.getRoutes())
            this.setRoutes(rewriteRoutes)
            this.setSidebarRouters(constantRoutes.concat(sidebarRoutes))
            this.setDefaultRoutes(sidebarRoutes)
            this.setTopbarRoutes(defaultRoutes)

            //console.log("generateRoutes====>rewriteRoutes:",rewriteRoutes)
            resolve(rewriteRoutes)//返回
          })
        })
      }
    }
  })

// 遍历后台传来的路由字符串，将component字符串转换为组件对象。转化为组件对象后，其实就相当于在前端已经import引入这个组件了。
//接下来可以直接使用component: Layout这种形式，而不是component:import()这种
function filterAsyncRouter(asyncRouterMap, lastRouter = false, type = false) {
  return asyncRouterMap.filter(route => {
    if (type && route.children) {
      route.children = filterChildren(route.children)
    }
    if (route.component) {
      // Layout ParentView 组件特殊处理
      if (route.component === 'Layout') {
        route.component = Layout//import Layout from '@/layout/index' 来自定义好的对象
      } else if (route.component === 'ParentView') {
        route.component = ParentView
      } else if (route.component === 'InnerLink') {
        route.component = InnerLink
      } else {
        route.component = loadView(route.component)//把字串形式的compont变成实际的组件对象
      }
    }
    if (route.children != null && route.children && route.children.length) {//递归处理children中的component字符串
      route.children = filterAsyncRouter(route.children, route, type)
    } else {
      delete route['children']
      delete route['redirect']
    }
    return true
  })
}

//用于菜单铺平：1. 同一主菜单下的若干级菜单都改为二级菜单  2.修改它们的path
function filterChildren(childrenMap, lastRouter = false) {//此时传入的数据都是同一个一级菜单下的子菜单数据。都拍到children变量中
  var children = []
  childrenMap.forEach((el, index) => {
    if (el.children && el.children.length) {//子菜单还有children且子子菜单不为0个
      if (el.component === 'ParentView' && !lastRouter) {
        el.children.forEach(c => {
          c.path = el.path + '/' + c.path
          if (c.children && c.children.length) {//递归处理可能存在的若干级子菜单
            children = children.concat(filterChildren(c.children, c))
            return
          }
          children.push(c)
        })
        return
      }
    }
    if (lastRouter) {
      el.path = lastRouter.path + '/' + el.path
    }
    children = children.concat(el)
  })
  return children
}

// 动态路由遍历，验证是否具备权限。在index中定义的动态路由并没有添加到router。
//过滤出本地定义的动态路由中有哪些可用的(有权限能看的)
export function filterDynamicRoutes(routes) {
  const res = []
  routes.forEach(route => {
    if (route.permissions) {
      if (auth.hasPermiOr(route.permissions)) {
        res.push(route)
      }
    } else if (route.roles) {
      if (auth.hasRoleOr(route.roles)) {
        res.push(route)
      }
    }
  })
  return res
}

export const loadView = (view) => {
  let res;
  for (const path in modules) {//modules在最上面定义，保存了所有的功能性组件的地址。key-value格式。key是路径（././///index.vue）、value是懒加载的函数（import("src/xxx/.."））
    const dir = path.split('views/')[1].split('.vue')[0];//切割得到非全的路径（比如：/sys/xx）
    if (dir === view) {
      res = () => modules[path]();//返回对应的import("....")这种
    }
  }
  return res;
}

export default usePermissionStore
