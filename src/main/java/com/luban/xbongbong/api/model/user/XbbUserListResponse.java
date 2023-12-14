package com.luban.xbongbong.api.model.user;

import com.luban.xbongbong.api.model.DingTalkUser;
import com.luban.common.base.model.Response;
import lombok.Data;

import java.util.List;

/**
 * @author HP 2022/12/30
 */
@Data
public class XbbUserListResponse implements Response {
    private Integer totalCount;
    private Integer totalPage;
    private List<DingTalkUser> userList;
}
