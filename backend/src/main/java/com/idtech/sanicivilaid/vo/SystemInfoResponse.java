package com.idtech.sanicivilaid.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemInfoResponse {

    private SystemInfoVO systemInfo;
    private TechStackVO techStack;
    private List<TeamMemberVO> teamMembers;
    private List<RelatedLinkVO> relatedLinks;
}
