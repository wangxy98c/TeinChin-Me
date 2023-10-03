import useDictStore from '@/store/modules/dict'
import { getDicts } from '@/api/system/dict/data'//请求字典

/**
 * 获取字典数据，先判断store中有没有，如果没有去后端请求（@/api/system/dict/data/getDicts方法）
 */
export function useDict(...args) {
  const res = ref({});
  return (() => {
    args.forEach((dictType, index) => {
      res.value[dictType] = [];
      const dicts = useDictStore().getDict(dictType);
      if (dicts) {
        res.value[dictType] = dicts;
      } else {
        getDicts(dictType).then(resp => {
          //这里是因为后端返回的不是一定是value而是dicValue。需要重设为value、Type等
          res.value[dictType] = resp.data.map(p => ({ label: p.dictLabel, value: p.dictValue, elTagType: p.listClass, elTagClass: p.cssClass }))
          useDictStore().setDict(dictType, res.value[dictType]);//参数分别是 key，value
        })
      }
    })
    return toRefs(res.value);
  })()
}