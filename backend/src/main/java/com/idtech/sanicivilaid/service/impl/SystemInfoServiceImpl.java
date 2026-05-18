package com.idtech.sanicivilaid.service.impl;

import com.idtech.sanicivilaid.entity.SysUser;
import com.idtech.sanicivilaid.exception.BusinessException;
import com.idtech.sanicivilaid.repository.SysUserRepository;
import com.idtech.sanicivilaid.service.SystemInfoService;
import com.idtech.sanicivilaid.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SystemInfoServiceImpl implements SystemInfoService {

    @Value("${system.version}")
    private String currentVersion;

    @Autowired
    private SysUserRepository sysUserRepository;

    private static final String ANNOUNCEMENT_TITLE = "重大更新公告";
    private static final String ANNOUNCEMENT_CONTENT = """
            【智能咨询升级】FAQ检索升级为向量语义检索
            - 集成硅基流动 BAAI/bge-m3 嵌入模型，实现语义级相似度匹配
            - 引入 hnswlib 内存向量索引，大幅提升检索效率与准确性
            - 放弃传统中文分词关键词匹配，用户提问理解更精准
            - 咨询问题将自动生成向量并与知识库进行语义比对

            【系统公告功能】新增版本更新弹窗提示
            - 登录后自动检测当前版本是否已读，未读则弹出公告
            - 公告内容包含版本号、标题及详细更新说明
            - 用户点击"我知道了"后自动标记该版本为已读，不再重复打扰
            - 后端记录每位用户的 lastReadVersion，实现个性化版本管理

            【数据模型扩展】
            - BizCivilFaq 新增 questionEmbedding 字段，缓存问题向量
            - SysUser 新增 lastReadVersion 字段，追踪用户最后阅读版本

            【版本同步】前后端版本号统一更新至 v0.3.1
            """;

    @Override
    public SystemInfoResponse getSystemInfo() {
        SystemInfoVO systemInfo = SystemInfoVO.builder()
                .systemName("典亮撒尼")
                .fullName("典亮撒尼 · 彝族民事法律援助系统")
                .version(currentVersion)
                .buildDate("20260518")
                .description("面向彝族地区的智能民事法律援助平台")
                .introduction("本系统致力于通过人工智能技术，为彝族地区群众提供便捷、专业的民事法律援助服务，助力法治社会建设与民族团结进步。")
                .releaseDate("2026-05-18")
                .environment("Web 浏览器")
                .contactEmail("support@example.com")
                .copyright("© 2026 典亮撒尼开发团队")
                .license("本系统及相关文档受知识产权保护，未经授权不得复制、传播或用于商业目的。")
                .rightsReserved("保留所有权利。")
                .fundingCN("本工作由云南省教育厅科学研究基金项目（编号：2025J1331）资助。")
                .fundingEN("This work is supported by Yunnan Provincial Education Department Science Foundation of China under Grant 2025J1331.")
                .acknowledgments("感谢所有为彝族地区法律援助事业贡献力量的组织与个人。特别感谢「洛笙云 LStudio Cloud」提供的服务器支持与基础设施保障。")
                .build();

        TechStackVO techStack = TechStackVO.builder()
                .frontend(List.of("Vue 3", "Element Plus", "Vite", "Pinia", "Vue Router", "SCSS", "Axios"))
                .backend(List.of("Spring Boot", "MySQL", "Redis", "MyBatis-Plus"))
                .infrastructure(List.of("Docker", "Nginx", "Linux"))
                .build();

        List<TeamMemberVO> teamMembers = List.of(
                TeamMemberVO.builder()
                        .name("毕志敏")
                        .role("项目负责人")
                        .description("统筹项目规划与团队协调")
                        .build(),
                TeamMemberVO.builder()
                        .name("何正方")
                        .role("算法工程师")
                        .description("负责 NLP 模型与法律知识库")
                        .build(),
                TeamMemberVO.builder()
                        .name("牟勇")
                        .role("算法工程师")
                        .description("负责 NLP 模型与法律知识库")
                        .build(),
                TeamMemberVO.builder()
                        .name("杨逍")
                        .role("全栈工程师")
                        .description("负责用户界面与交互实现、服务端架构与业务逻辑")
                        .build(),
                TeamMemberVO.builder()
                        .name("韩昊成")
                        .role("全栈工程师")
                        .description("负责用户界面与交互实现、服务端架构与业务逻辑")
                        .build(),
                TeamMemberVO.builder()
                        .name("高毕有")
                        .role("彝文支持工程师")
                        .description("负责彝文语料处理、彝汉双语交互界面支持及本地化适配")
                        .build(),
                TeamMemberVO.builder()
                        .name("毕志昇")
                        .role("法律顾问")
                        .description("负责民法典法律知识梳理、咨询逻辑校验及法律条款解读")
                        .build(),
                TeamMemberVO.builder()
                        .name("武子龙")
                        .role("网络工程师")
                        .description("负责项目的服务器部署与维护")
                        .build()
        );

        List<RelatedLinkVO> relatedLinks = List.of(
                RelatedLinkVO.builder()
                        .label("用户手册")
                        .url("https://docs.example.com/manual")
                        .build(),
                RelatedLinkVO.builder()
                        .label("隐私政策")
                        .url("https://docs.example.com/privacy")
                        .build(),
                RelatedLinkVO.builder()
                        .label("服务条款")
                        .url("https://docs.example.com/terms")
                        .build(),
                RelatedLinkVO.builder()
                        .label("问题反馈")
                        .url("https://github.com/YXandImmortal/sani-civil-aid/issues")
                        .build(),
                RelatedLinkVO.builder()
                        .label("洛笙云 LStudio Cloud")
                        .url("https://www.lstudioc.com")
                        .build()
        );

        return SystemInfoResponse.builder()
                .systemInfo(systemInfo)
                .techStack(techStack)
                .teamMembers(teamMembers)
                .relatedLinks(relatedLinks)
                .build();
    }

    @Override
    public AnnouncementVO getAnnouncement() {
        boolean needShow = false;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (isAuthenticatedUser(authentication)) {
            String username = authentication.getName();
            Optional<SysUser> userOpt = sysUserRepository.findByUsername(username);
            if (userOpt.isPresent()) {
                SysUser user = userOpt.get();
                String lastReadVersion = user.getLastReadVersion();
                needShow = !currentVersion.equals(lastReadVersion);
            }
        }

        return AnnouncementVO.builder()
                .currentVersion(currentVersion)
                .title(ANNOUNCEMENT_TITLE)
                .content(ANNOUNCEMENT_CONTENT)
                .needShow(needShow)
                .build();
    }

    @Override
    public void markAnnouncementRead() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!isAuthenticatedUser(authentication)) {
            throw BusinessException.authFailed();
        }

        String username = authentication.getName();
        SysUser user = sysUserRepository.findByUsername(username)
                .orElseThrow(BusinessException::authFailed);

        user.setLastReadVersion(currentVersion);
        sysUserRepository.save(user);
    }

    /**
     * 判断当前 SecurityContext 中的认证信息是否为已登录用户
     */
    private boolean isAuthenticatedUser(Authentication authentication) {
        return authentication != null
                && authentication.isAuthenticated()
                && authentication.getPrincipal() != null
                && !"anonymousUser".equals(authentication.getPrincipal());
    }
}
