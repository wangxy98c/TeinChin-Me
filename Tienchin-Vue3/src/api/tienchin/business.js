import request from '@/utils/request'

//// 查询商机列表
export function listBusiness(query) {
  return request({
    url: '/tienchin/business/list',
    method: 'get',
    params: query
  })
}
export function listUsers(deptId){
  return request({
    url:'/tienchin/clue/users/'+deptId,
    method: 'get'
  })
}
export function getCourseByType(typeId){
  return request({
    url: '/tienchin/business/course/'+typeId,
    method: 'get',
  })
}
export function getBusinessById(businessId){
  return request({
    url: '/tienchin/business/'+businessId,
    method: 'get',
  })
}
////
export function businessFollow(data){
  return request({
    url: '/tienchin/business/follow',
    method: 'post',
    data: data
  })
}
export function businessSave(data){
  return request({
    url: '/tienchin/business/save',
    method: 'post',
    data: data
  })
}
//// 
//注意此处是clue
export function assignBusiness(data){
  return request({
    url: '/tienchin/assign',
    method: 'post',
    data: data
  })
}

export function invalidateClue(data){
  return request({
    url: '/tienchin/clue/invalidate',
    method: 'post',
    data: data
  })
}

//// 查询跟进记录
export function getBusinessRecordById(businessId) {
  return request({
    url: '/tienchin/follow/record/business/' + businessId,
    method: 'get'
  })
}
////查询渠道有哪些，为什么后端要再写一个，因为涉及到不同用户的权限问题。未必有渠道查询的权限
export function listChannel(query) {
    return request({
      url: '/tienchin/business/channel/list',
      method: 'get',
      params: query
    })
}
//根据所选渠道，查询对应渠道下所拥有的活动
export function listActivity(channelId) {
    return request({
        url: '/tienchin/business/activity/list/'+channelId,
        method: 'get',
    })
}
//// 新增
export function addBusiness(data) {
  return request({
    url: '/tienchin/business',
    method: 'post',
    data: data
  })
}

////修改
export function updateBusiness(data) {
  return request({
    url: '/tienchin/business/',
    method: 'put',
    data: data
  })
}

////删除课程
export function delBusiness(businessIds) {
  return request({
    url: '/tienchin/business/' + businessIds,
    method: 'delete'
  })
}

export function getClueSummary(clueId) {
  return request({
      url: '/tienchin/clue/summary/'+clueId,
      method: 'get',
  })
}

export function toBusiness(clueId){
  return request({
    url: '/tienchin/clue/tobusiness/'+clueId,
    method: 'post',
  })
}
