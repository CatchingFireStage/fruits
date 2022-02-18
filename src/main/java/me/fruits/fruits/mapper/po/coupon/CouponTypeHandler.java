package me.fruits.fruits.mapper.po.coupon;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.fruits.fruits.mapper.enums.coupon.CategoryEnum;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CouponTypeHandler implements TypeHandler<Object> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * jackson json序列化 db对象->数据库对象
     *
     * @param ps
     * @param i
     * @param parameter
     * @param jdbcType
     * @throws SQLException
     */
    @Override
    public void setParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType) throws SQLException {
        try {
            ps.setObject(i, objectMapper.writeValueAsString(parameter));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 下面三个方法都是数据库字段->java属性
     *
     * @param rs
     * @param columnName
     * @return
     * @throws SQLException
     */
    @Override
    public Object getResult(ResultSet rs, String columnName) throws SQLException {

        if (CategoryEnum.MERCHANT_MONEY_OFF.getValue().equals(rs.getInt("category"))) {
            //商家满减类型
            try {
                return objectMapper.readValue(rs.getString(columnName), MerchantMoneyOffPayload.class);
            } catch (IOException e) {
                throw new SQLException("json解析失败");
            }
        }
        throw new SQLException("暂时不支持的枚举类型");
    }

    @Override
    public Object getResult(ResultSet rs, int columnIndex) throws SQLException {

        if (CategoryEnum.MERCHANT_MONEY_OFF.getValue().equals(rs.getInt("category"))) {
            //商家满减类型
            try {
                return objectMapper.readValue(rs.getString(columnIndex), MerchantMoneyOffPayload.class);
            } catch (IOException e) {
                throw new SQLException("json解析失败");
            }
        }
        throw new SQLException("暂时不支持的枚举类型");
    }

    @Override
    public Object getResult(CallableStatement cs, int columnIndex) throws SQLException {

        if (CategoryEnum.MERCHANT_MONEY_OFF.getValue().equals(cs.getInt("category"))) {
            //商家满减类型
            try {
                return objectMapper.readValue(cs.getString(columnIndex), MerchantMoneyOffPayload.class);
            } catch (IOException e) {
                throw new SQLException("json解析失败");
            }
        }
        throw new SQLException("暂时不支持的枚举类型");
    }
}
