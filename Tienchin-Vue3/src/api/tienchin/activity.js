import request from '@/utils/request'

// 查询活动列表
export function listActivity(query) {
  return request({
    url: '/tienchin/activity/list',
    method: 'get',
    params: query
  })
}
// 查询渠道（活动页需要渠道相关信息）：为什么后端要再写一个，因为涉及到不同用户的权限问题。未必有渠道查询的权限
export function listChannel(query) {
  return request({
    url: '/tienchin/activity/channel/list',
    method: 'get',
    params: query
  })
}

// 查询活动详细
export function getActivity(activityId) {
  return request({
    url: '/tienchin/activity/' + activityId,
    method: 'get'
  })
}

// 新增活动
export function addActivity(data) {
  return request({
    url: '/tienchin/activity',
    method: 'post',
    data: data
  })
}

// 修改活动
export function updateActivity(data) {
  return request({
    url: '/tienchin/activity',
    method: 'put',
    data: data
  })
}

// 删除活动
export function delActivity(activityId) {
  return request({
    url: '/tienchin/activity/' + activityId,
    method: 'delete'
  })
}
