import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class PureNetUtil {

    public static String get(String url){
        return post(url,null);
    }

    public static <K, V> String post(String  url,Map<K,V>   param){
        HttpURLConnection conn=null;
        try {
            URL u=new URL(url);
            conn=(HttpURLConnection) u.openConnection();
            StringBuffer sb=null;
            if(param!=null){//如果请求参数不为空
                sb=new StringBuffer();
                /*A URL connection can be used for input and/or output.  Set the DoOutput
                 * flag to true if you intend to use the URL connection for output,
                 * false if not.  The default is false.*/
                //默认为false,post方法需要写入参数,设定true
                conn.setDoOutput(true);
                //设定post方法,默认get
                conn.setRequestMethod("POST");
                //获得输出流
                OutputStream out=conn.getOutputStream();
                //对输出流封装成高级输出流
                BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(out));
                //将参数封装成键值对的形式
                for(Map.Entry s:param.entrySet()){
                    sb.append(s.getKey()).append("=").append(s.getValue()).append("&");
                }
                //将参数通过输出流写入
                writer.write(sb.deleteCharAt(sb.toString().length()-1).toString());
                writer.close();
                sb=null;
            }
            conn.connect();//建立连接
            sb=new StringBuffer();
            //获取连接状态码
            int recode=conn.getResponseCode();
            BufferedReader reader=null;
            if(recode==200){
                //Returns an input stream that reads from this open connection
                //从连接中获取输入流
                InputStream in=conn.getInputStream();
                //对输入流进行封装
                reader=new BufferedReader(new InputStreamReader(in));
                String str=null;
                sb=new StringBuffer();
                //从输入流中读取数据
                while((str=reader.readLine())!=null){
                    sb.append(str).append(System.getProperty("line.separator"));
                }
                //关闭输入流
                reader.close();
                if (sb.toString().length() == 0) {
                    return null;
                }
                return sb.toString().substring(0,
                        sb.toString().length() - System.getProperty("line.separator").length());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }finally{
            if(conn!=null)
                conn.disconnect();
        }
        return null;
    }

}