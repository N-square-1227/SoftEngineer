package com.se.softengineer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.se.softengineer.entity.Userrole;

public interface UserroleService extends IService<Userrole> {
    public String getRoleNameByUserID(int UserID);
}
