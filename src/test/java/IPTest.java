import etl.IPParserUtil;

/**
 * @ClassName: IPTest
 * @Description:
 * @Author: Lotus
 * @Date: Created in 2018/8/16 14:33
 * @Version 1.0
 */

public class IPTest {
    public static void main(String[] args) {
        System.out.println(new IPParserUtil().parseIp("171.36.161.123"));
    }
}
