import request from '@/utils/request'

// 查询活动列表
export function listActivity(query) {
  return request({
    url: '/tienchin/activity/list',
    method: 'get',
    params: query
  })
}
// 查询新活动的可选渠道
export function listChannel(query) {
  return request({
    url: '/tienchin/activity/channel/list',
    method: 'get',
    params: query
  })
}

// 查询岗位详细
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

// 删除岗位
export function delPost(postId) {
  return request({
    url: '/system/post/' + postId,
    method: 'delete'
  })
}
