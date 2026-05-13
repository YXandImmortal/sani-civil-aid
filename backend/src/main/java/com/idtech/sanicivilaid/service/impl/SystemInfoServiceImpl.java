package com.idtech.sanicivilaid.service.impl;

import com.idtech.sanicivilaid.service.SystemInfoService;
import com.idtech.sanicivilaid.vo.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SystemInfoServiceImpl implements SystemInfoService {

    @Override
    public SystemInfoResponse getSystemInfo() {
        SystemInfoVO systemInfo = SystemInfoVO.builder()
                .systemName("典亮撒尼")
                .fullName("典亮撒尼 · 彝族民事法律援助系统")
                .version("v0.2.0")
                .buildDate("20260513")
                .description("面向彝族地区的智能民事法律援助平台")
                .introduction("本系统致力于通过人工智能技术，为彝族地区群众提供便捷、专业的民事法律援助服务，助力法治社会建设与民族团结进步。")
                .releaseDate("2026-05-13")
                .environment("Web 浏览器")
                .contactEmail("support@example.com")
                .copyright("© 2026 典亮撒尼开发团队")
                .license("本系统及相关文档受知识产权保护，未经授权不得复制、传播或用于商业目的。")
                .rightsReserved("保留所有权利。")
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
                        .name("牟勇")
                        .role("算法工程师")
                        .description("负责 NLP 模型与法律知识库")
                        .build(),
                TeamMemberVO.builder()
                        .name("何正方")
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
                        .url("https://github.com/xxx/issues")
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
}
