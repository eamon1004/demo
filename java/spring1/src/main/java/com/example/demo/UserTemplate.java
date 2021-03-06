package com.example.demo;
import javax.sql.DataSource;
import javax.xml.crypto.Data;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class UserTemplate {
    private DataSource dataSource;
    private JdbcTemplate UserTemplate;
    public UserTemplate(DataSource dataSource){
        this.dataSource = dataSource;
        this.UserTemplate = new JdbcTemplate(dataSource);
    }
    public  UserTemplate(){
//        this.dataSource = new DataSourceConfig().getdataSource();
        this.UserTemplate = new JdbcTemplate(dataSource);
    }
    public void setDataSource(DataSource db){
        this.dataSource = db;
    }
    public DataSource dataSource(){
        return this.dataSource;
    }
    public boolean inseret(String name , int age , int sex,String mobile){
        String sql = "INSERT INTO USER (name,age,sex,mobile) VALUES(?,?,?,?)";
        int ret = this.UserTemplate.update(sql,new Object[]{name,age,sex,mobile});
        return ret>0 ? true:false;
    }
    public HashMap userList(long page){
        HashMap map = new HashMap();
        ArrayList<User> arr= new ArrayList<>();
        long start=0,end=0;
        if (page<=1){
            start=0;
            end = 5;
        }else {

            if (Long.MAX_VALUE /5 < page){
                end = Long.MAX_VALUE;
                start = end -5;
            }else {
                start = (page-1)*5;
                end = page *5;
            }
        }
        String sql = "select * from user limit "+start+","+end;
        this.UserTemplate.query(sql, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                User student = new User();
                student.setId(rs.getInt("id"));
                student.setName(rs.getString("name"));
                student.setAge(rs.getInt("age"));
                student.setSex(rs.getInt("sex"));
                student.setAddress(rs.getNString("address"));
                student.setMobile(rs.getNString("mobile"));
                arr.add(student);
            }
        });
        if (arr.size()>0){
            map.put("state","success");
            map.put("data",arr);
        }else {
            map.put("state","faild");
            map.put("msg","报错了");
        }
        return map;
    }
    public boolean user(String name,String mobile){
        String sql = "select count(*) from user WHERE mobile=?";
//        boolean have = false;
        List ret =this.UserTemplate.queryForList(sql,new Object[]{mobile});
        Iterator it = ret.iterator();
        if (it.hasNext()){
            return true;
        }
        return false;
    }
}
