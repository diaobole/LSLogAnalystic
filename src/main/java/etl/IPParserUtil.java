package etl;


import etl.util.ip.IPSeeker;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * @ClassName: IPParserUtil
 * @Description:
 * @Author: Lotus
 * @Date: Created in 2018/8/15 16:02
 * @Version 1.0
 */

public class IPParserUtil extends IPSeeker {
    private static final Logger logger = Logger.getLogger(IPParserUtil.class);

    RegionInfo info = new RegionInfo();

    public RegionInfo parseIp(String ip) {
        if (StringUtils.isEmpty(ip)) {
            logger.warn("解析的ip为空！");
            return info;
        }

        try {
            // 通过ipSeeker来获取ip所对应的信息  江苏省泰州市
            String country = IPSeeker.getInstance().getCountry(ip);
            if (country.equals("局域网")) {
                info.setCountry("中国");
                info.setProvince("北京市");
                info.setCity("昌平区");
            } else if (country != null && !country.trim().isEmpty()) { //trim():用于字符串的处理，功能是去除一段字符串前后的空格，只保留中间的部分；
                // 查找返回的字符串中是否有省
                int index = country.indexOf("省");
                info.setCountry("中国");
                if (index > 0) {
                    // 证明有省份
                    info.setProvince(country.substring(0, index + 1));
                    // 查找是否有市
                    int index2 = country.indexOf("市");
                    if (index2 > 0) {
                        info.setCity(country.substring(index + 1, index2 + 1));
                    }
                } else {
                    //查找不到省
                    String flag = country.substring(0, 2);
                    switch (flag) {
                        case "内蒙":
                            info.setProvince(flag + "古");
                            country.substring(3);
                            index = country.indexOf("市");
                            if (index > 0) {
                                info.setCity(country.substring(0, index + 1));
                            }
                            break;

                        case "广西":

                        case "宁夏":

                        case "新疆":

                        case "西藏":
                            info.setProvince(flag);
                            country.substring(2);
                            index = country.indexOf("市");
                            if (index > 0) {
                                info.setCity(country.substring(0, index + 1));
                            }
                            break;

                        case "北京":
                        case "上海":
                        case "天津":
                        case "重庆":
                            info.setProvince(flag + "市");
                            country.substring(2);
                            index = country.indexOf("区");
                            if (index > 0) {
                                char ch = country.charAt(index - 1);
                                if (ch != '小' || ch != '校' || ch != '军') {
                                    info.setCity(country.substring(0, index + 1));
                                }
                            }

                            index = country.indexOf("县");
                            if (index > 0) {
                                info.setCity(country.substring(0, index + 1));
                            }
                            break;

                        case "香港":
                        case "澳门":
                        case "台湾":
                            info.setProvince(flag + "特别行政区");

                        default:
                            break;
                    }

                }
            }
        } catch (Exception e) {
            logger.warn("解析ip异常", e);
        }

        return info;
    }

    /**
     * 内部类，用于封装ip解析出来的国家、省、市信息
     */
    public static class RegionInfo {
        private static final String DEFAULT_VALUE = "unknown";
        private String country = DEFAULT_VALUE;
        private String province = DEFAULT_VALUE;
        private String city = DEFAULT_VALUE;

        public RegionInfo() {
        }

        public RegionInfo(String country, String province, String city) {
            this.country = country;
            this.province = province;
            this.city = city;
        }

        public String getCountry() {
            return country;
        }

        public String getProvince() {
            return province;
        }

        public String getCity() {
            return city;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public void setCity(String city) {
            this.city = city;
        }

        @Override
        public String toString() {
            return "RegionInfo{" +
                    "country='" + country + "'" +
                    ", province='" + province + "'" +
                    ", city='" + city + "'" +
                    "}";
        }
    }
}
