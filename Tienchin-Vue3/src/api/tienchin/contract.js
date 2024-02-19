import request from '@/utils/request'

export function getCourseByType(typeId){
    return request({
        url: '/tienchin/business/course/'+typeId,
        method: 'get',
    })
}
export function addContract(data){
    return request({
      url: '/tienchin/contract',
      method: 'post',
      data: data
    })
}
export function getUnapproveTask(){
    return request({
      url: '/tienchin/contract/unapprove',
      method: 'get',
    })
}
export function getContractById(contractId){
    return request({
      url: '/tienchin/contract/views/'+contractId,
      method: 'get',
    })
}
export function getCommitTask(){
    return request({
      url: '/tienchin/contract/commit',
      method: 'get',
    })
}
export function handleApprove(data){
    return request({
      url: '/tienchin/contract/approveOrReject',
      method: 'post',
      data: data
    })
}
export function getApprovedTask(){
  return request({
    url: '/tienchin/contract/approved',
    method: 'get',
  })
}
export function editContract(data){
  return request({
    url: '/tienchin/contract/edit',
    method: 'post',
    data: data
  })
}