import request from '@/utils/request'
/**
 * 页面基本套用了role的页面，其对应的请求方法也基本类似。直接拷贝一份然后修改即可。（页面也是这样，能省不少事）
 */

// 查询渠道列表
export function listChannel(query) {
  return request({
    url: '/tienchin/channel/list',
    method: 'get',
    params: query
  })
}

// 查询渠道详情
export function getChannel(channelId) {
  return request({
    url: '/tienchin/channel/' + channelId,
    method: 'get'
  })
}

// 新增渠道
export function addChannel(data) {
  return request({
    url: '/tienchin/channel',
    method: 'post',
    data: data
  })
}

// 修改渠道
export function updateChannel(data) {
  return request({
    url: '/tienchin/channel',
    method: 'put',
    data: data
  })
}

// 删除渠道
export function delChannel(channelId) {
  return request({
    url: '/tienchin/channel/' + channelId,
    method: 'delete'
  })
}


