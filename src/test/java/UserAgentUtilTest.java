import etl.util.UserAgentUtil;

/**
 * @ClassName: UserAgentUtilTest
 * @Description:
 * @Author: Lotus
 * @Date: Created in 2018/8/16 15:06
 * @Version 1.0
 */

public class UserAgentUtilTest {
    public static void main(String[] args) {
        System.out.println(UserAgentUtil.parseUserAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36"));
        System.out.println(UserAgentUtil.parseUserAgent("Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; .NET4.0C; .NET4.0E; .NET CLR 2.0.50727; .NET CLR 3.0.30729; .NET CLR 3.5.30729; rv:11.0) like Gecko"));
    }
}
