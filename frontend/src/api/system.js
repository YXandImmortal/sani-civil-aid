import request from '@/utils/request'

/**
 * 获取系统基本信息
 * @returns {Promise<{ systemInfo, techStack?, teamMembers?, relatedLinks? }>}
 */
export function getSystemInfo() {
  return request.get('/system/info')
}
