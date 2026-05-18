import request from '@/utils/request'

/**
 * 获取系统基本信息
 * @returns {Promise<{ systemInfo, techStack?, teamMembers?, relatedLinks? }>}
 */
export function getSystemInfo() {
  return request.get('/system/info')
}

/**
 * 获取更新公告
 * @returns {Promise<{ currentVersion, title, content, needShow }>}
 */
export function getAnnouncement() {
  return request.get('/system/announcement')
}

/**
 * 标记公告已读
 * @returns {Promise<null>}
 */
export function markAnnouncementRead() {
  return request.post('/system/announcement/read')
}
