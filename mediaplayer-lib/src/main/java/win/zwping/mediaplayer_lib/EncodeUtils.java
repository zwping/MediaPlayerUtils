package win.zwping.mediaplayer_lib;

import static android.text.TextUtils.isEmpty;

/**
 * <p>describe：编码工具集合
 * <p>    note：
 * <p> @author：zwp on 2017/12/28 0028 mail：1101558280@qq.com web: http://www.zwping.win </p>
 */
public class EncodeUtils {

    /**
     * 字符串转换为url编码格式
     *
     * @param s 等待转换的
     * @return url编码字符串
     */
    public static String toUrlEncode(String s) {
        if (isEmpty(s)) return s;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c >= 0 && c <= 255) {
                sb.append(c);
            } else {
                byte[] b;
                try {
                    b = String.valueOf(c).getBytes("utf-8");
                } catch (Exception ex) {
                    System.out.println(ex);
                    b = new byte[0];
                }
                for (int j = 0; j < b.length; j++) {
                    int k = b[j];
                    if (k < 0)
                        k += 256;
                    sb.append("%" + Integer.toHexString(k).toUpperCase());
                }
            }
        }
        return sb.toString().replaceAll(" ", "%20");
    }
}
