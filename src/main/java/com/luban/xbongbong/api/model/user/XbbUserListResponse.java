package com.luban.xbongbong.api.model.user;

import com.luban.xbongbong.api.model.DingTalkUser;
import lombok.Data;

import java.util.List;

/**
 * @author HP 2022/12/30
 */
@Data
public class XbbUserListResponse {
    private Integer totalCount;
    private Integer totalPage;
    private List<DingTalkUser> userList;
}
