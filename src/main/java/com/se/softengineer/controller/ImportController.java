package com.se.softengineer.controller;

import com.mysql.jdbc.exceptions.MySQLDataException;
import com.se.softengineer.dao.IndexSymMapper;
import com.se.softengineer.dao.UsersDataMapper;
import com.se.softengineer.entity.Indexsym;
import com.se.softengineer.mapper.SampleMapper;
import com.se.softengineer.service.IndexSymService;
import com.se.softengineer.utils.AnalyExcel;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.SqlSessionException;
import org.springframework.beans.factory.annotation.Autowired;
import com.se.softengineer.entity.Indexsym;
import com.se.softengineer.service.IndexSymService;
import com.se.softengineer.utils.Result;
import com.se.softengineer.utils.AnalyExcel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;

import java.io.*;

import java.util.List;

/**
 * @author xiaxue
 * 导入指标体系文件
 */
@RestController
@RequestMapping("/import")
public class ImportController {

    //获取当前用户名
    public String userName;
    //从excel中读到的临时数据
    public static List<String[]> cells=new ArrayList<>();
    //用户导入的indexSym数据，除了在数据库中保存，在此全局变量中也保存了
    public static List<Indexsym> indexSym=new ArrayList<>();
    //每个用户都有一个tableName
    public static String indexSymTableName;
    public static String indexDataTableName;

    public static String filesName;

    //创建指标数据表的sql语句
    public static String createSql=new String();

    //标记是指标体系文件还是数据文件
    public static String fileType=new String();

    //文件存储路径
    @Value("${file-save-path}")
    private String fileSavePath;
    private String filePath;

    @Autowired
    private IndexSymService indexSymService;

    @Autowired
    private IndexSymMapper indexSymMapper;

    @Autowired
    private UsersDataMapper usersDataMapper;

    /**
     * @author xiaxue
     * @param file
     * @throws IOException
     */
    @PostMapping(value = "/excel/{value}")
    //@RequestParam("file") MultipartFile file
    public void uploadFileByExcel(@RequestParam(value = "file",required = false) MultipartFile file, @PathVariable("value") String v) throws IOException {
        String filename = file.getOriginalFilename();
        fileType=v;
        System.out.println("类型："+fileType);
        filesName=file.getOriginalFilename().substring(0,file.getOriginalFilename().indexOf("."));
        System.out.println(filesName);
        //保存到本地
        String filePath = savaFileByNio((FileInputStream) file.getInputStream(), filename);
        List<String[]> l=AnalyExcel.readExcelFile(file,1);
        cells=l;
        //keepData(l);
    }

    /**
     * @author xiaxue
<<<<<<< HEAD
     * @param fis
     * @param fileName
     * @return
     */
    public static String savaFileByNio(FileInputStream fis, String fileName) {
        // 这个路径最后是在: 你的项目路径/FileSpace  也就是和src同级
        String path = "D:\\seData\\"+fileName;
        // 判断父文件夹是否存在
        File file = new File(path);
        //System.out.println(file.getPath());
        if (file.getParentFile() != null || !file.getParentFile().isDirectory()) {
            file.getParentFile().mkdirs();
        }
        // 通过NIO保存文件到本地磁盘
        try {
            FileOutputStream fos = new FileOutputStream(path);
            FileChannel inChannel = fis.getChannel();
            FileChannel outChannel = fos.getChannel();
            inChannel.transferTo(0, inChannel.size(), outChannel);
            inChannel.close();
            outChannel.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }


    /**
     * @author xiaxue
=======
>>>>>>> ae64deceea9317932cffef9f3ca9df382eda48db
     * @param file
     */
    @RequestMapping("/xml/{value}")
    public void loadFileByXML(@RequestParam(value = "file",required = false) MultipartFile file, @PathVariable("value") String v) throws IOException {
        String filename = file.getOriginalFilename();

        fileType=v;
        filesName=file.getOriginalFilename().substring(0,file.getOriginalFilename().indexOf("."));
        //保存到本地
        String filePath = savaFileByNio((FileInputStream) file.getInputStream(), filename);
        try {
            // 创建解析器工厂
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = factory.newDocumentBuilder();
            // 创建一个Document对象
            Document doc = db.parse(filePath);
            NodeList indexSymList = doc.getElementsByTagName("node");
            // 获取节点个数
            System.out.println("一共有" + indexSymList.getLength() + "结点");

            // 遍历每个book节点
            for (int i = 0; i < indexSymList.getLength(); i++) {
                //System.out.println("*******************************");
                // 索引从零开始
                org.w3c.dom.Node book = indexSymList.item(i);
                // 获取book节点所有属性集合
                org.w3c.dom.NamedNodeMap attrs = book.getAttributes();

                //System.out.println("第" + (i + 1) + "本书共有" + attrs.getLength() + "属性");
                // 遍历book属性，不知道节点属性和属性名情况
                for (int j = 0; j < attrs.getLength(); j++) {
                    // 获取某一个属性
                    org.w3c.dom.Node attr = attrs.item(j);
                    //System.out.print("属性名:" + attr.getNodeName());
                    //System.out.println(" --- 属性值:" + attr.getNodeValue());
                }
                List<String[]> l=new ArrayList<>();
                NodeList childNodes = book.getChildNodes();
                String[] temp=new String[5];
                int j=0;
                for (int k = 0; k < childNodes.getLength(); k++) {
                    // 区分,去掉空格和换行符
                    if (childNodes.item(k).getNodeType() == Node.ELEMENT_NODE) {
/*                        // 获取element类型的节点和节点值
                        //System.out.print("节点名：" + childNodes.item(k).getNodeName());
                        //System.out.print(" --- 节点值：" + childNodes.item(k).getFirstChild().getNodeValue());
                        //System.out.println(" --- 节点值："+childNodes.item(k).getTextContent());*/
                        temp[j++]=childNodes.item(k).getTextContent();
                        //System.out.println(" --- ："+k+" "+temp[j-1]);
                    }
                }
                cells.add(temp);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @RequestMapping("/keepExcel/{username}")
    public void keep(@PathVariable("username") String name){
        //System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        userName=name;
        System.out.println("用户名：  "+name);
        keepData(cells);
    }
    /**
     * 保存读取的indexSym,保存到List和数据库
     * @param list
     */
    public void keepData(List<String[]> list){
        System.out.println("文件类型:  "+fileType);
        if(fileType.equals("indexSym")) {
            //拼接新表名
            indexSymTableName = filesName + "IndexSym";
            System.out.println("指标体系名字： "+indexSymTableName);
            indexSymMapper.createTable(indexSymTableName);
            for (String[] l : list) {
                indexSymMapper.insertIntoTable(indexSymTableName, l[0], Integer.parseInt(l[1]), Double.parseDouble(l[2]), Integer.parseInt(l[3]));
            }

        }else if(fileType.equals("indexdata")){
            indexDataTableName=filesName+"IndexData";
            System.out.println("指标数据名字: "+indexDataTableName);
            //拼接sql语句，因为指标个数不确定。
            String[] temp=list.get(1);
            int column=temp.length-1;//指标个数
            /*createSql="create table ${tableName}(`id` int NOT NULL AUTO_INCREMENT," +
=======
            indexDataTableName=filesName+"indexData";
            //拼接sql语句，因为指标个数不确定。
            String[] temp=list.get(1);
            int column=temp.length-1;//指标个数
            createSql="create table ${tableName}(`id` int NOT NULL AUTO_INCREMENT," +
>>>>>>> ae64deceea9317932cffef9f3ca9df382eda48db
                    "  `name` varchar(50) NOT NULL";
            for(int i=0;i<column;i++){
                createSql+=","+"x"+i+" double(50,0) NOT NULL";
            }
            createSql+="PRIMARY KEY (`id`)" +
<<<<<<< HEAD
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;";*/

            //System.out.println("sql语句aaaaaaaaaaa: "+createSql);
        }
        //usersDataMapper.insertIntoTable(userName+"data",indexDataTableName,indexSymTableName);
        System.out.println("sql语句aaaaaaaaaaa: "+createSql);
        //sampleMapper.createTable(indexDataTableName);
    }

    /**
     * @author lmy
     */
    @PostMapping(value = "/json")
    //@RequestParam("file") MultipartFile file
    public Result uploadFileByJson(@RequestParam(value = "file",required = false) MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        System.out.println(filename);
        filesName=file.getOriginalFilename().substring(0,file.getOriginalFilename().indexOf("."));
        //保存到本地
        String res = savaFileByNio((FileInputStream) file.getInputStream(), filename);
        return res!=null?Result.success():Result.fail();
    }
    /**
     * @author lmy
     */

    @RequestMapping("/keepJson")
    public Result keepJson(@RequestParam String userName){
        Indexsym t=new Indexsym();
        indexSymTableName=filesName+"IndexSym";
        indexSymService.createTable(indexSymTableName);
        //System.out.println("xxxxxxxxxxxxxxx");
        boolean result=true;
        try {
            result = indexSymService.saveJson(indexSymTableName,filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result==true?Result.success():Result.fail();
    }

    /**
     * @author lmy
     */

    @RequestMapping("/downloadJson")
    public void downloadJson(HttpServletResponse response) throws Exception {
        //这里写要让前端下载的文件的路径
        File file = new File(this.fileSavePath + "example.json");
        //设置编码格式，防止下载的文件内乱码
        response.setCharacterEncoding("UTF-8");
        //获取路径文件对象
        String realFileName = file.getName();
        //设置响应头类型，这里可以根据文件类型设置，text/plain、application/vnd.ms-excel等
        response.setHeader("content-type", "application/octet-stream;charset=UTF-8");
        response.setContentType("application/octet-stream;charset=UTF-8");
        //如果不设置响应头大小，可能报错：“Excel 已完成文件级验证和修复。此工作簿的某些部分可能已被修复或丢弃”
        response.addHeader("Content-Length", String.valueOf(file.length()));
        try {
            //Content-Disposition的作用：告知浏览器以何种方式显示响应返回的文件，用浏览器打开还是以附件的形式下载到本地保存
            //attachment表示以附件方式下载   inline表示在线打开   "Content-Disposition: inline; filename=文件名.mp3"
            // filename表示文件的默认名称，因为网络传输只支持URL编码的相关支付，因此需要将文件名URL编码后进行传输,前端收到后需要反编码才能获取到真正的名称
            response.setHeader("Content-Disposition", "attachment;filename=" + java.net.URLEncoder.encode(realFileName.trim(), "UTF-8"));
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        //初始化文件流字节缓存
        byte[] buff = new byte[1024];
        //开始写入
        OutputStream os = response.getOutputStream();
        //写入完成，创建文件流
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
        try {
            // bis.read(data)：将字符读入数组。在某个输入可用、发生I/O错误或者已到达流的末尾前，此方法一直阻塞。
            // 读取的字符数，如果已到达流的末尾，则返回 -1
            int i = bis.read(buff);
            while (i != -1) {
                os.write(buff, 0, buff.length);
                os.flush();
                i = bis.read(buff);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @RequestMapping("/insertUsersData")
    public void insertUsersData()  {
        int flag=0;
        try{
            usersDataMapper.createTable(userName+"Data");
        }catch (Exception e){
            flag=-1;
            usersDataMapper.insertIntoTable(userName+"Data",indexDataTableName,indexSymTableName);

        }
        if(flag==0){
            usersDataMapper.insertIntoTable(userName+"Data",indexDataTableName,indexSymTableName);
        }
           }
}
