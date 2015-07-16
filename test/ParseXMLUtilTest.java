import com.taobao.datax.engine.conf.JobConf;
import com.taobao.datax.engine.conf.ParseXMLUtil;
import com.taobao.datax.engine.schedule.Engine;

/**
 * Created by longkeyy on 15/7/16.
 */
public class ParseXMLUtilTest {

    public static void main(String[] args) {
        Engine.confLog("BEFORE_CHRIST");
        String parentPath = Thread.currentThread().getClass().getResource("/").getPath();
        System.out.println(parentPath);


        JobConf jobConf = ParseXMLUtil.loadJobConfig("jobs/sample/MysqlReader_to_HdfsWriter.xml");
        System.out.println(jobConf.toString());
    }
}
