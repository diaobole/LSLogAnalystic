package com.qianfeng.etl.util;

import com.qianfeng.common.EventLogConstants;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName: LogUtil
 * @Description:
 * @Author: Lotus
 * @Date: Created in 2018/8/17 9:42
 * @Version 1.0
 */

public class LogUtil {
    private static final Logger logger = Logger.getLogger(IPParserUtil.class);

    /**
     * 单行日志的解析
     *
     * @param log
     * @return
     */
    public static Map<String, String> handleLog(String log) {
        Map<String, String> info = new ConcurrentHashMap<>();
        if (StringUtils.isNotEmpty(log.trim())) {
            // 拆分单行日志
            String[] fields = log.split(EventLogConstants.LOG_SEPARTOR);

            if (fields.length == 4) {
                info.put(EventLogConstants.EVENT_COLUMN_NAME_IP, fields[0]);
                info.put(EventLogConstants.EVENT_COLUMN_NAME_SERVER_TIME, fields[1].replace("utf-8", "utf-8"));
                // 处理参数列表
                handleParams(info, fields[3]);

                handleIP(info);
                handleUserAgent(info);
            } else {
                info.clear();
            }
        }
        return info;
    }

    private static void handleUserAgent(Map<String, String> info) {
        if (info.containsKey(info.get(EventLogConstants.EVENT_COLUMN_NAME_USERAGENT))) {
            UserAgentUtil.UserAgentInfo ua = UserAgentUtil.parseUserAgent(EventLogConstants.EVENT_COLUMN_NAME_USERAGENT);
            if (ua != null) {
                info.put(EventLogConstants.EVENT_COLUMN_NAME_BROWSER_NAME, ua.getBrowserName());
                info.put(EventLogConstants.EVENT_COLUMN_NAME_BROWSER_VERSION, ua.getBrowserVersion());
                info.put(EventLogConstants.EVENT_COLUMN_NAME_OS_NAME, ua.getOsName());
                info.put(EventLogConstants.EVENT_COLUMN_NAME_OS_VERSION, ua.getOsVersion());
            }
        }
    }

    private static void handleIP(Map<String, String> info) {
        if (info.containsKey(info.get(EventLogConstants.EVENT_COLUMN_NAME_IP))) {
            IPParserUtil.RegionInfo ri = new IPParserUtil().parseIp(EventLogConstants.EVENT_COLUMN_NAME_IP);
            if (ri != null) {
                info.put(EventLogConstants.EVENT_COLUMN_NAME_COUNTRY, ri.getCountry());
                info.put(EventLogConstants.EVENT_COLUMN_NAME_PROVINCE, ri.getProvince());
                info.put(EventLogConstants.EVENT_COLUMN_NAME_CITY, ri.getCity());
            }
        }
    }

    /**
     * 处理参数
     * @param info
     * @param field
     */
    private static void handleParams(Map<String, String> info, String field) {
        if (StringUtils.isNotEmpty(field)) {
            int index = field.indexOf("?");
            if (index > 0) {
                String fields = field.substring(index + 1);
                String[] kvs = fields.split("&");
                for (String kv : kvs) {
                    String[] korv = kv.split("=");
                    try {
                        String k = korv[0];
                        String v = URLDecoder.decode(korv[1], "utf-8");
                        if (StringUtils.isNotEmpty(k)) {
                            info.put(k, v);
                        }
                    } catch (UnsupportedEncodingException e) {
                        logger.warn("url的解码异常", e);
                    }
                }
            }
        }
    }
}
