import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author: DANGO
 * @date 2018/7/5 9:12
 * @Description:
 */
public class SMSTest {

        public static final String charset = "utf-8";
        // 用户平台API账号(非登录账号,示例:N1234567)
        public static String account = "";
        // 用户平台API密码(非登录密码)
        public static String pswd = "";

        public static void main(String[] args) {
                int result = test();
                System.out.println(result);

        }

        public static int test() {
                int t = new Integer(555);
                try {
                        return t;
                } finally {
                        t++;
                }
        }

}
