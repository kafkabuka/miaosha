package com.bnz.oauth.server.domain;

import java.io.Serializable;
import lombok.Data;

/**
 * tb_role_permission
 * @author 
 */
@Data
public class RolePermission implements Serializable {
    private Long id;

    /**
     * 角色 ID
     */
    private Long roleId;

    /**
     * 权限 ID
     */
    private Long permissionId;

    private static final long serialVersionUID = 1L;
}