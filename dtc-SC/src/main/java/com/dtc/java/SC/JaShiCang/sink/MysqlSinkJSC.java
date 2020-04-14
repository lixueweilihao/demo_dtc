package com.dtc.java.SC.JaShiCang.sink;

import com.dtc.java.SC.common.MySQLUtil;
import com.dtc.java.SC.common.PropertiesConstants;
import org.apache.flink.api.java.tuple.Tuple9;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Properties;

/**
 * Created on 2019-09-12
 *
 * @author :hao.li
 */

public class MysqlSinkJSC extends RichSinkFunction<Tuple9<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer>> {
    private Properties properties;
    private Connection connection;
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private PreparedStatement preparedStatement;
    String str;

    public MysqlSinkJSC(Properties prop) {
        this.properties = prop;
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        // 加载JDBC驱动
        Class.forName(JDBC_DRIVER);
        // 获取数据库连接
        String userName = properties.get(PropertiesConstants.MYSQL_USERNAME).toString();
        String passWord = properties.get(PropertiesConstants.MYSQL_PASSWORD).toString();
        String host = properties.get(PropertiesConstants.MYSQL_HOST).toString();
        String port = properties.get(PropertiesConstants.MYSQL_PORT).toString();
        String database = properties.get(PropertiesConstants.MYSQL_DATABASE).toString();

        String mysqlUrl = "jdbc:mysql://" + host + ":" + port + "/" + database + "?useUnicode=true&characterEncoding=UTF-8";
        connection = DriverManager.getConnection(mysqlUrl, userName
                , passWord);//写入mysql数据库
//        String sql = null;
        String sql = "replace into SC_JSC_JKDP(riqi,level_1,level_2,level_3,level_4,alarm_count,not_close_alarm,SB_ALL_count,SB_ZC_count,SB_YC_count,js_time) values(?,?,?,?,?,?,?,?,?,?,?)";
        preparedStatement = connection.prepareStatement(sql);
        //insert sql在配置文件中
        super.open(parameters);
    }

    @Override
    public void close() throws Exception {
        super.close();
        if (preparedStatement != null) {
            preparedStatement.close();
        }
        if (connection != null) {
            connection.close();
        }
        super.close();
    }

    @Override
    public void invoke(Tuple9<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer> value, Context context) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        String riqi = sdf.format(System.currentTimeMillis());
        String js_time = sdf1.format(System.currentTimeMillis());

        try {
            preparedStatement.setString(1, riqi);
            preparedStatement.setInt(2,value.f0);
            preparedStatement.setInt(3,value.f1);
            preparedStatement.setInt(4,value.f2);
            preparedStatement.setInt(5,value.f3);
            preparedStatement.setInt(6,value.f4);
            preparedStatement.setInt(7,value.f5);
            preparedStatement.setInt(8,value.f6);
            preparedStatement.setInt(9,value.f7);
            preparedStatement.setInt(10,value.f8);
            preparedStatement.setString(11,js_time);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

