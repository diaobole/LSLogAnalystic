package etl.util;

import cz.mallat.uasparser.OnlineUpdater;
import cz.mallat.uasparser.UASparser;
import etl.IPParserUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * @ClassName: UserAgentUtil
 * @Description:
 * @Author: Lotus
 * @Date: Created in 2018/8/15 16:49
 * @Version 1.0
 */
public class UserAgentUtil {
    private static final Logger logger = Logger.getLogger(UserAgentUtil.class);

    private static UASparser ua = null;
    // 初始化
    static{
        try {
            ua = new UASparser(OnlineUpdater.getVendoredInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析浏览器代理对象
     * @param agent
     * @return
     */
    public static UserAgentInfo parseUserAgent(String agent) {
        UserAgentInfo uainfo = new UserAgentInfo();
        if (StringUtils.isEmpty(agent)) {
            logger.warn("agent is null.");
            return null;
        }

        // 正常解析
        try {
            cz.mallat.uasparser.UserAgentInfo info = ua.parse(agent);
            // 设置属性
            uainfo.setBrowserName(info.getUaFamily());
            uainfo.setBrowserVersion(info.getBrowserVersionInfo());
            uainfo.setOsName(info.getOsFamily());
            uainfo.setOsVersion(info.getOsName());
        } catch (IOException e) {
            logger.warn("UseragentParse Exception", e);
        }
        return uainfo;
    }



    public static class UserAgentInfo{
        private String browserName;
        private String browserVersion;
        private String osName;
        private String osVersion;

        public void setBrowserName(String browserName) {
            this.browserName = browserName;
        }

        public void setBrowserVersion(String browserVersion) {
            this.browserVersion = browserVersion;
        }

        public void setOsName(String osName) {
            this.osName = osName;
        }

        public void setOsVersion(String osVersion) {
            this.osVersion = osVersion;
        }

        public String getBrowserName() {

            return browserName;
        }

        public String getBrowserVersion() {
            return browserVersion;
        }

        public String getOsName() {
            return osName;
        }

        public String getOsVersion() {
            return osVersion;
        }

        @Override
        public String toString() {
            return "UserAgentInfo{" +
                    "browserName='" + browserName + "'" +
                    ", browserVersion='" + browserVersion + "'" +
                    ", osName='" + osName + "'" +
                    ", osVersion='" + osVersion + "'" +
                    "}";
        }
    }
}
