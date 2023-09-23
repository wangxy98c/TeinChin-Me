import hasRole from './permission/hasRole'
import hasPermi from './permission/hasPermi'
import copyText from './common/copyText'

export default function directive(app){
  app.directive('hasRole', hasRole)
  app.directive('hasPermi', hasPermi)
  app.directive('copyText', copyText)
}
/**
export default function directive(app) { ... }: 这是一个导出默认函数的语句。这个函数接受一个参数 app，它用于注册自定义指令到 Vue 应用中。
app.directive('hasRole', hasRole): 这行代码使用 app 对象的 directive 方法注册一个名为 'hasRole' 的自定义指令，并且该指令的实现是由名为 hasRole 的函数来提供
 */