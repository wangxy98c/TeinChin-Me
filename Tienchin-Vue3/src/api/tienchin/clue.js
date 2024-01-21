import request from '@/utils/request'

// 查询课程列表
export function listClue(query) {
  return request({
    url: '/tienchin/clue/list',
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
export function getClueById(clueId){
  return request({
    url: '/tienchin/clue/'+clueId,
    method: 'get',
  })
}

export function clueFollow(data){
  return request({
    url: '/tienchin/clue/follow',
    method: 'post',
    data: data
  })
}
export function assignClue(data){
  return request({
    url: '/clue/assign',
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

// 查询线索跟进记录
export function getClueRecordByClueId(clueId) {
  return request({
    url: '/tienchin/clue/follow/record/' + clueId,
    method: 'get'
  })
}
//查询渠道有哪些，为什么后端要再写一个，因为涉及到不同用户的权限问题。未必有渠道查询的权限
export function listChannel(query) {
    return request({
      url: '/tienchin/clue/channel/list',
      method: 'get',
      params: query
    })
}
//根据所选渠道，查询对应渠道下所拥有的活动
export function listActivity(channelId) {
    return request({
        url: '/tienchin/clue/activity/list/'+channelId,
        method: 'get',
    })
}
// 新增线索
export function addClue(data) {
  return request({
    url: '/tienchin/clue',
    method: 'post',
    data: data
  })
}

// 修改
export function updateClue(data) {
  return request({
    url: '/tienchin/clue/',
    method: 'put',
    data: data
  })
}

// 删除课程
export function delClue(clueId) {
  return request({
    url: '/tienchin/clue/' + clueId,
    method: 'delete'
  })
}

export function getClueSummary(clueId) {
  return request({
      url: '/tienchin/clue/summary/'+clueId,
      method: 'get',
  })
}
