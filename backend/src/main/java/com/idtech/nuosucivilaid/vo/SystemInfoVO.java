package com.idtech.nuosucivilaid.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemInfoVO {

    private String systemName;
    private String fullName;
    private String version;
    private String buildDate;
    private String description;
    private String introduction;
    private String releaseDate;
    private String environment;
    private String contactEmail;
    private String copyright;
    private String license;
    private String rightsReserved;
    private String acknowledgments;
}
