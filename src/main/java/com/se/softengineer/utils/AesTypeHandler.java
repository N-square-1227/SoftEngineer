package com.se.softengineer.utils;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import lombok.SneakyThrows;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.springframework.util.StringUtils;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class AesTypeHandler implements TypeHandler<String> {


    @SneakyThrows
    @Override
    public void setParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) {
        ps.setString(i, AESUtil.encrypt(parameter));
    }

    @Override
    public String getResult(ResultSet rs, String columnName) throws SQLException {
        return AESUtil.decrypt(rs.getString(columnName));
    }

    @Override
    public String getResult(ResultSet rs, int columnIndex) throws SQLException {
        return AESUtil.decrypt(rs.getString(columnIndex));
    }

    @Override
    public String getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return AESUtil.decrypt(cs.getString(columnIndex));
    }

    public String encrypt(String parameter) throws Exception {
        if (StringUtils.isEmpty(parameter)) {
            return null;
        }
        return AESUtil.encrypt(parameter);
    }

}


