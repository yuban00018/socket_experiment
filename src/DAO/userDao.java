package DAO;

import Bean.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class userDao {
    private static PreparedStatement pstmt=null;
    private static Connection connection=null;
    private static ResultSet rs=null;
    private static int count=0;
    String URL="jdbc:mysql://localhost:3306/test?serverTimezone=GMT%2B8";
    String USERNAME="root";
    String PWD="123456";
    public List<User> queryUsers(){
        List<User> users=new ArrayList<>();
        User user;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");//加载具体的驱动类(新的驱动类)
            connection=DriverManager.getConnection(URL,USERNAME,PWD);
            String sql="select * from usertable";
            pstmt=connection.prepareStatement(sql);
            rs=pstmt.executeQuery();
            while(rs.next()){
                int id=rs.getInt("id");
                String name=rs.getString("username");
                String pwd=rs.getString("password");
                user=new User(id,name,pwd);
                users.add(user);
            }
            return users;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try{
                if(rs!=null)rs.close();
                if(pstmt!=null)pstmt.close();
                if(connection!=null)connection.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
    }
    public boolean addUser(User user){
            boolean flag;
            try{
                Class.forName("com.mysql.cj.jdbc.Driver");//加载具体的驱动类(新的驱动类)
                connection=DriverManager.getConnection(URL,USERNAME,PWD);
                String sql="insert into usertable value(?,?,?)";
                pstmt=connection.prepareStatement(sql);
                pstmt.setInt(3,user.getId());
                pstmt.setString(1, user.getUsername());
                pstmt.setString(2, user.getPassword());
                count=pstmt.executeUpdate();
                flag=count>0;
            } catch (Exception e) {
                e.printStackTrace();
                flag=false;
            } finally {
                try{
                    if(pstmt!=null)pstmt.close();
                    if(connection!=null)connection.close();
                }catch(SQLException e){
                    e.printStackTrace();
                    flag=false;
                }
            }
        return flag;
    }
    public String login(User user){
        String userId = "fail";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection=DriverManager.getConnection(URL,USERNAME,PWD);
            String sql = "select * from usertable";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                if ((rs.getString("username").trim().compareTo(user.getUsername()) == 0)
                        && (rs.getString("password").compareTo(user.getPassword()) == 0)) {
                    userId = rs.getString("id");
                }
            }
            rs.close();
            stmt.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userId;
    }
    public boolean deleteUser(int Id){
        boolean deleted = false;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection=DriverManager.getConnection(URL,USERNAME,PWD);
            String sql = "delete from usertable where id=?";
            pstmt=connection.prepareStatement(sql);
            pstmt.setInt(1,Id);
            count=pstmt.executeUpdate();
            if(count>0)deleted=true;
            rs.close();
            pstmt.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return deleted;
    }

}
