package com.idtech.nuosucivilaid.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RelatedLinkVO {

    private String label;
    private String url;
}
