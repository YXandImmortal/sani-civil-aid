<template>
  <div class="about-page">
    <!-- 顶部横幅 -->
    <div class="about-hero">
      <div class="hero-logo">
        <img src="/logo.svg" alt="典亮撒尼" class="hero-logo-img" />
      </div>
      <h1 class="hero-title yi-bilingual">
        <span>{{ systemInfo.systemName }}</span>
        <span class="yi-placeholder">凲凴嘍勍</span>
      </h1>
      <p class="hero-version">
        {{ systemInfo.version }}
        <el-tag v-if="systemInfo.buildDate" size="small" type="info" class="build-tag">
          {{ systemInfo.buildDate }}
        </el-tag>
      </p>
      <p class="hero-desc">{{ systemInfo.description }}</p>
    </div>

    <el-row :gutter="24">
      <!-- 左侧：系统信息 -->
      <el-col :xs="24" :lg="14">
        <!-- 系统简介 -->
        <el-card class="about-card" shadow="never">
          <template #header>
            <div class="card-header">
              <el-icon><InfoFilled /></el-icon>
              <span class="yi-bilingual">
                <span>系统简介</span>
                <span class="yi-placeholder">偒嘫叏侼</span>
              </span>
            </div>
          </template>
          <p class="intro-text">{{ systemInfo.introduction }}</p>

          <el-descriptions :column="1" border class="info-descriptions">
            <el-descriptions-item>
              <template #label>
                <span class="yi-bilingual">
                  <span>系统全称</span>
                  <span class="yi-placeholder">偒嘫啔偯墜</span>
                </span>
              </template>
              {{ systemInfo.fullName }}
            </el-descriptions-item>
            <el-descriptions-item>
              <template #label>
                <span class="yi-bilingual">
                  <span>当前版本</span>
                  <span class="yi-placeholder">冮勦噊伅</span>
                </span>
              </template>
              <el-tag type="primary" effect="plain">{{ systemInfo.version }}</el-tag>
            </el-descriptions-item>
            <el-descriptions-item>
              <template #label>
                <span class="yi-bilingual">
                  <span>发布日期</span>
                  <span class="yi-placeholder">亞倎償佢</span>
                </span>
              </template>
              {{ systemInfo.releaseDate }}
            </el-descriptions-item>
            <el-descriptions-item>
              <template #label>
                <span class="yi-bilingual">
                  <span>运行环境</span>
                  <span class="yi-placeholder">咡兌匑咺</span>
                </span>
              </template>
              {{ systemInfo.environment }}
            </el-descriptions-item>
            <el-descriptions-item v-if="systemInfo.contactEmail">
              <template #label>
                <span class="yi-bilingual">
                  <span>联系邮箱</span>
                  <span class="yi-placeholder">剷僎傁刼</span>
                </span>
              </template>
              <el-link :href="`mailto:${systemInfo.contactEmail}`" type="primary">
                {{ systemInfo.contactEmail }}
              </el-link>
            </el-descriptions-item>
          </el-descriptions>
        </el-card>

        <!-- 技术栈 -->
        <el-card class="about-card" shadow="never">
          <template #header>
            <div class="card-header">
              <el-icon><Cpu /></el-icon>
              <span class="yi-bilingual">
                <span>技术架构</span>
                <span class="yi-placeholder">唃壪傛佊</span>
              </span>
            </div>
          </template>

          <div class="tech-section">
            <h4 class="tech-title yi-bilingual">
              <span>前端技术</span>
              <span class="yi-placeholder">唦仱唃壪</span>
            </h4>
            <div class="tech-tags">
              <el-tag
                v-for="item in techStack.frontend"
                :key="item"
                type="info"
                effect="plain"
                class="tech-tag"
              >
                {{ item }}
              </el-tag>
            </div>
          </div>

          <el-divider />

          <div class="tech-section">
            <h4 class="tech-title yi-bilingual">
              <span>后端技术</span>
              <span class="yi-placeholder">嚳佇唃壪</span>
            </h4>
            <div class="tech-tags">
              <el-tag
                v-for="item in techStack.backend"
                :key="item"
                type="info"
                effect="plain"
                class="tech-tag"
              >
                {{ item }}
              </el-tag>
            </div>
          </div>

          <el-divider />

          <div class="tech-section">
            <h4 class="tech-title yi-bilingual">
              <span>基础设施</span>
              <span class="yi-placeholder">嗴仩咡仠</span>
            </h4>
            <div class="tech-tags">
              <el-tag
                v-for="item in techStack.infrastructure"
                :key="item"
                type="info"
                effect="plain"
                class="tech-tag"
              >
                {{ item }}
              </el-tag>
            </div>
          </div>
        </el-card>

        <!-- 相关链接 -->
        <el-card class="about-card" shadow="never">
          <template #header>
            <div class="card-header">
              <el-icon><Link /></el-icon>
              <span class="yi-bilingual">
                <span>相关链接</span>
                <span class="yi-placeholder">吳凈凓僘</span>
              </span>
            </div>
          </template>

          <div class="link-list">
            <el-link
              v-for="link in relatedLinks"
              :key="link.url"
              :href="link.url"
              target="_blank"
              type="primary"
              class="link-item"
            >
              <el-icon><Document /></el-icon>
              {{ link.label }}
            </el-link>
          </div>
        </el-card>
      </el-col>

      <!-- 右侧：团队与版权 -->
      <el-col :xs="24" :lg="10">
        <!-- 开发团队 -->
        <el-card class="about-card" shadow="never">
          <template #header>
            <div class="card-header">
              <el-icon><UserFilled /></el-icon>
              <span class="yi-bilingual">
                <span>开发团队</span>
                <span class="yi-placeholder">兇亣卪唸</span>
              </span>
            </div>
          </template>

          <div class="team-list">
            <div
              v-for="member in teamMembers"
              :key="member.name"
              class="team-member"
            >
              <el-avatar :size="48" :icon="UserFilled" class="member-avatar" />
              <div class="member-info">
                <div class="member-name">{{ member.name }}</div>
                <div class="member-role">{{ member.role }}</div>
                <div v-if="member.description" class="member-desc">{{ member.description }}</div>
              </div>
            </div>
          </div>
        </el-card>

        <!-- 开源协议与版权 -->
        <el-card class="about-card" shadow="never">
          <template #header>
            <div class="card-header">
              <el-icon><DocumentChecked /></el-icon>
              <span class="yi-bilingual">
                <span>版权与协议</span>
                <span class="yi-placeholder">噊劰俠傛厼啗</span>
              </span>
            </div>
          </template>

          <div class="copyright-section">
            <p class="copyright-text">
              <strong>{{ systemInfo.copyright }}</strong>
            </p>
            <p class="copyright-sub">{{ systemInfo.license }}</p>
            <p v-if="systemInfo.rightsReserved" class="copyright-sub">{{ systemInfo.rightsReserved }}</p>
          </div>

          <el-divider />

          <div class="acknowledgments">
            <h4 class="ack-title yi-bilingual">
              <span>特别致谢</span>
              <span class="yi-placeholder">卙圐務倗</span>
            </h4>
            <p class="ack-text">{{ systemInfo.acknowledgments }}</p>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import {
  InfoFilled,
  Cpu,
  UserFilled,
  DocumentChecked,
  Link,
  Document
} from '@element-plus/icons-vue'
import { getSystemInfo } from '@/api/system'

// ================= 系统信息 =================
const systemInfo = ref({
  systemName: '典亮撒尼',
  fullName: '典亮撒尼 · 彝族民事法律援助系统',
  version: 'v1.0.0',
  buildDate: '',
  description: '面向彝族地区的智能民事法律援助平台',
  introduction: '本系统致力于通过人工智能技术，为彝族地区群众提供便捷、专业的民事法律援助服务。系统整合了法律咨询、法条查询、彝汉词汇对照等功能，支持彝汉双语交互，助力少数民族地区法治建设与普法宣传。',
  releaseDate: '2026-05-12',
  environment: 'Web 浏览器',
  contactEmail: '',
  copyright: '© 2026 典亮撒尼开发团队',
  license: '本系统及相关文档受知识产权保护，未经授权不得复制、传播或用于商业目的。',
  rightsReserved: '保留所有权利。',
  acknowledgments: '感谢所有为彝族地区法律援助事业贡献力量的组织与个人。'
})

// ================= 技术栈 =================
const techStack = ref({
  frontend: ['Vue 3', 'Element Plus', 'Vite', 'Pinia', 'Vue Router', 'SCSS', 'Axios'],
  backend: ['（由后端提供）'],
  infrastructure: ['（由后端提供）']
})

// ================= 开发团队 =================
const teamMembers = ref([
  { name: '（待补充）', role: '项目负责人', description: '统筹项目规划与团队协调' },
  { name: '（待补充）', role: '前端开发', description: '负责用户界面与交互实现' },
  { name: '（待补充）', role: '后端开发', description: '负责服务端架构与业务逻辑' },
  { name: '（待补充）', role: '算法工程师', description: '负责 NLP 模型与法律知识库' }
])

// ================= 相关链接 =================
const relatedLinks = ref([
  { label: '用户手册', url: '#' },
  { label: '隐私政策', url: '#' },
  { label: '服务条款', url: '#' },
  { label: '问题反馈', url: '#' }
])

// ================= 加载系统信息 =================
const loadSystemInfo = async () => {
  try {
    const res = await getSystemInfo()
    if (res && res.systemInfo) {
      systemInfo.value = { ...systemInfo.value, ...res.systemInfo }
    }
    if (res && res.techStack) {
      techStack.value = { ...techStack.value, ...res.techStack }
    }
    if (res && res.teamMembers) {
      teamMembers.value = res.teamMembers
    }
    if (res && res.relatedLinks) {
      relatedLinks.value = res.relatedLinks
    }
  } catch {
    // 静默失败，使用默认数据
  }
}

onMounted(() => {
  loadSystemInfo()
})
</script>

<style scoped lang="scss">
.about-page {
  .about-hero {
    text-align: center;
    padding: 40px 20px;
    margin-bottom: 24px;
    background: var(--color-bg-elevated);
    border: 1px solid var(--color-border-default);
    border-radius: 8px;

    .hero-logo {
      margin-bottom: 16px;

      .hero-logo-img {
        width: 90px;
        height: 90px;
      }
    }

    .hero-title {
      font-size: 1.8rem;
      font-weight: 700;
      color: var(--color-text-primary);
      margin: 0 0 8px;
    }

    .hero-version {
      font-size: 1rem;
      color: var(--color-text-secondary);
      margin: 0 0 12px;
      display: flex;
      align-items: center;
      justify-content: center;
      gap: 8px;

      .build-tag {
        font-family: monospace;
      }
    }

    .hero-desc {
      font-size: 0.95rem;
      color: var(--color-text-secondary);
      margin: 0;
    }
  }

  .about-card {
    margin-bottom: 24px;
    border: 1px solid var(--color-border-default);

    .card-header {
      display: flex;
      align-items: center;
      gap: 8px;
      font-weight: 600;
      font-size: 1.05rem;
      color: var(--color-text-primary);

      .el-icon {
        color: var(--color-primary);
      }
    }

    .intro-text {
      color: var(--color-text-secondary);
      line-height: 1.8;
      margin: 0 0 20px;
      text-indent: 2em;
    }

    .info-descriptions {
      :deep(.el-descriptions__label) {
        width: 120px;
        color: var(--color-text-secondary);
        background-color: var(--color-bg-base);
      }
      :deep(.el-descriptions__content) {
        color: var(--color-text-primary);
      }
    }

    .tech-section {
      .tech-title {
        font-size: 0.95rem;
        font-weight: 600;
        color: var(--color-text-primary);
        margin: 0 0 12px;
      }

      .tech-tags {
        display: flex;
        flex-wrap: wrap;
        gap: 8px;

        .tech-tag {
          border-radius: 4px;
        }
      }
    }

    .team-list {
      display: flex;
      flex-direction: column;
      gap: 16px;

      .team-member {
        display: flex;
        align-items: flex-start;
        gap: 12px;

        .member-avatar {
          background-color: var(--color-primary-subtle);
          color: var(--color-primary);
          flex-shrink: 0;
        }

        .member-info {
          flex: 1;

          .member-name {
            font-weight: 600;
            color: var(--color-text-primary);
            font-size: 1rem;
          }

          .member-role {
            font-size: 0.85rem;
            color: var(--color-primary);
            margin-top: 2px;
          }

          .member-desc {
            font-size: 0.85rem;
            color: var(--color-text-secondary);
            margin-top: 4px;
          }
        }
      }
    }

    .copyright-section {
      text-align: center;

      .copyright-text {
        font-size: 1rem;
        color: var(--color-text-primary);
        margin: 0 0 8px;
      }

      .copyright-sub {
        font-size: 0.85rem;
        color: var(--color-text-secondary);
        margin: 0;
        line-height: 1.6;
      }
    }

    .acknowledgments {
      .ack-title {
        font-size: 0.95rem;
        font-weight: 600;
        color: var(--color-text-primary);
        margin: 0 0 8px;
      }

      .ack-text {
        font-size: 0.85rem;
        color: var(--color-text-secondary);
        margin: 0;
        line-height: 1.6;
      }
    }

    .link-list {
      display: flex;
      flex-direction: row;
      gap: 10px;
      align-items: flex-start;

      .link-item {
        font-size: 0.9rem;
        gap: 6px;
      }
    }
  }
}
</style>
