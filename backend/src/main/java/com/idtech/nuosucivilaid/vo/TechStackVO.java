package com.idtech.nuosucivilaid.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TechStackVO {

    private List<String> frontend;
    private List<String> backend;
    private List<String> infrastructure;
}
