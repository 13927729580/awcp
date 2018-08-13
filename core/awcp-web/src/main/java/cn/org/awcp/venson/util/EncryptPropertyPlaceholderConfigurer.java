package cn.org.awcp.venson.util;

import cn.org.awcp.core.utils.Security;
import cn.org.awcp.venson.common.SC;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.util.Optional;

/**
 * 解析密码类型的配置
 * @author venson
 * @version 20180511
 */
public class EncryptPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {
    /**
     * 解密指定propertyName的加密属性值
     * @param propertyName
     * @param propertyValue
     * @return
     */
    @Override
    protected String convertProperty(String propertyName, String propertyValue) {
        String value=super.convertProperty(propertyName, propertyValue);
        if(SC.ENCRYPT_PASSWORD_KEY.contains(propertyName)){
            return Optional.ofNullable(Security.decryptPassword(value)).orElse(StringUtils.EMPTY);
        }else{
            return value;
        }
    }
}
