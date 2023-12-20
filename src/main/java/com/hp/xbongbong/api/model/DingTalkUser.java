package com.hp.xbongbong.api.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author HP 2022/12/29
 */
@NoArgsConstructor
@Data
public class DingTalkUser {

    private String avatar;
    private List<DepartmentListDTO> departmentList;
    private String name;
    private String position;
    private List<RoleListDTO> roleList;
    private Integer status;
    private String userId;

    @NoArgsConstructor
    @Data
    public static class DepartmentListDTO {
        private Integer id;
        private String name;
        private Integer isLeader;
    }

    @NoArgsConstructor
    @Data
    public static class RoleListDTO {
        private Integer id;
        private String name;
    }
}
