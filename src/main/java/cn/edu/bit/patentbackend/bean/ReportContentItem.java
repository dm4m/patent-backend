package cn.edu.bit.patentbackend.bean;

import lombok.Data;

@Data
public class ReportContentItem {
    Integer reportItemId;
    Integer corrId;
    String itemType;
    String name;
}
