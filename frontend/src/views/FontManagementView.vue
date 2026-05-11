<template>
  <div class="font-management">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2 class="page-title">
        <span class="yi-bilingual">
          <span>字体管理</span>
          <span class="yi-placeholder">[彝文占位符]</span>
        </span>
      </h2>
      <p class="page-desc">管理本地 Yi Script 彝文字体的启用状态与显示参数</p>
    </div>

    <!-- 设置卡片 -->
    <el-row :gutter="24">
      <!-- 基础设置 -->
      <el-col :xs="24" :lg="12">
        <el-card class="settings-card" shadow="sm">
          <template #header>
            <div class="card-header">
              <el-icon><Setting /></el-icon>
              <span>基础设置</span>
            </div>
          </template>

          <div class="setting-item">
            <div class="setting-label">
              <span>启用本地 Yi Script 字体</span>
              <el-tooltip content="关闭后将回退到系统默认彝文字体">
                <el-icon class="info-icon"><InfoFilled /></el-icon>
              </el-tooltip>
            </div>
            <el-switch v-model="useLocalYiFont" />
          </div>

          <div class="setting-item">
            <div class="setting-label">
              <span>字体大小</span>
              <span class="value-tag">{{ fontSize }}px</span>
            </div>
            <el-slider v-model="fontSize" :min="12" :max="32" :step="1" show-stops />
          </div>

          <div class="setting-item">
            <div class="setting-label">
              <span>行高</span>
              <span class="value-tag">{{ lineHeight }}</span>
            </div>
            <el-slider v-model="lineHeight" :min="1" :max="2.5" :step="0.1" show-stops />
          </div>

          <div class="setting-item">
            <div class="setting-label">
              <span>预览文本</span>
            </div>
            <el-input v-model="previewText" placeholder="输入用于预览的彝文文本" maxlength="50" show-word-limit />
          </div>

          <div class="setting-actions">
            <el-button type="primary" @click="resetFontSettings">
              <el-icon><RefreshLeft /></el-icon>
              恢复默认
            </el-button>
          </div>
        </el-card>
      </el-col>

      <!-- 实时预览 -->
      <el-col :xs="24" :lg="12">
        <el-card class="preview-card" shadow="sm">
          <template #header>
            <div class="card-header">
              <el-icon><View /></el-icon>
              <span>实时预览</span>
            </div>
          </template>

          <div class="preview-meta">
            <el-tag size="small" :type="useLocalYiFont ? 'success' : 'info'">
              {{ useLocalYiFont ? '本地 Yi Script' : '系统默认字体' }}
            </el-tag>
            <el-tag size="small" type="info">{{ fontSize }}px</el-tag>
            <el-tag size="small" type="info">行高 {{ lineHeight }}</el-tag>
          </div>

          <div class="preview-area" :style="yiStyle">
            <div class="preview-main">{{ previewText || 'ꆈꌠꁱꂷ' }}</div>
            <div class="preview-paragraph">
              {{ previewText || 'ꆈꌠꁱꂷ' }} — 这是一段用于预览彝文字体渲染效果的示例文本。
              本地 Yi Script 字体文件位于 <code>assets/font/Yi Script.ttf</code>。
            </div>
          </div>

          <div class="font-stack-info">
            <div class="info-title">当前字体栈</div>
            <el-input :model-value="yiFontFamily" readonly type="textarea" :rows="2" />
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 字体信息 -->
    <el-row :gutter="24" class="info-row">
      <el-col :xs="24">
        <el-card shadow="sm">
          <template #header>
            <div class="card-header">
              <el-icon><Document /></el-icon>
              <span>字体文件信息</span>
            </div>
          </template>

          <el-descriptions :column="3" border>
            <el-descriptions-item label="字体名称">Yi Script</el-descriptions-item>
            <el-descriptions-item label="文件路径">src/assets/font/Yi Script.ttf</el-descriptions-item>
            <el-descriptions-item label="格式">TrueType (.ttf)</el-descriptions-item>
            <el-descriptions-item label="用途">彝文（诺苏语）显示</el-descriptions-item>
            <el-descriptions-item label="回退字体">Microsoft Yi Baiti / Nuosu SIL</el-descriptions-item>
            <el-descriptions-item label="加载策略">font-display: swap</el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { useFont } from '@/composables/useFont'
import { Setting, View, InfoFilled, RefreshLeft, Document } from '@element-plus/icons-vue'

const {
  useLocalYiFont,
  fontSize,
  lineHeight,
  previewText,
  yiFontFamily,
  yiStyle,
  resetFontSettings
} = useFont()
</script>

<style scoped lang="scss">
.font-management {
  .page-header {
    margin-bottom: 24px;

    .page-title {
      margin: 0 0 8px;
      font-size: 1.5rem;
      color: var(--color-text-primary);
    }

    .page-desc {
      margin: 0;
      color: var(--color-text-secondary);
      font-size: 0.875rem;
    }
  }

  .settings-card,
  .preview-card {
    margin-bottom: 24px;
  }

  .card-header {
    display: flex;
    align-items: center;
    gap: 8px;
    font-weight: 600;
    color: var(--color-text-primary);
  }

  .setting-item {
    margin-bottom: 20px;

    &:last-of-type {
      margin-bottom: 16px;
    }

    .setting-label {
      display: flex;
      align-items: center;
      justify-content: space-between;
      margin-bottom: 8px;
      font-size: 0.875rem;
      color: var(--color-text-secondary);

      .info-icon {
        margin-left: 4px;
        color: var(--color-text-tertiary);
        cursor: help;
      }

      .value-tag {
        font-size: 0.75rem;
        padding: 2px 8px;
        border-radius: var(--radius-full);
        background-color: var(--color-primary-light);
        color: var(--color-primary);
      }
    }
  }

  .setting-actions {
    display: flex;
    justify-content: flex-end;
    padding-top: 8px;
    border-top: 1px solid var(--color-border-subtle);
  }

  .preview-meta {
    display: flex;
    flex-wrap: wrap;
    gap: 8px;
    margin-bottom: 16px;
  }

  .preview-area {
    padding: 20px;
    border-radius: var(--radius-md);
    background-color: var(--color-bg-inset);
    border: 1px solid var(--color-border-default);
    margin-bottom: 20px;
    transition: all 0.3s ease;

    .preview-main {
      font-size: 2.5rem;
      margin-bottom: 12px;
      word-break: break-all;
    }

    .preview-paragraph {
      font-size: 1rem;
      opacity: 0.85;
    }
  }

  .font-stack-info {
    .info-title {
      font-size: 0.75rem;
      color: var(--color-text-tertiary);
      margin-bottom: 6px;
      text-transform: uppercase;
      letter-spacing: 0.05em;
    }
  }

  .info-row {
    margin-top: 8px;
  }
}
</style>
