package com.se.softengineer.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.se.softengineer.entity.IndexSym;
import com.se.softengineer.entity.TreeData;
import com.se.softengineer.mapper.UsersDataMapper;
import com.se.softengineer.entity.IndexSymNode;
import com.se.softengineer.service.IndexSymNodeService;
import com.se.softengineer.service.SampleService;
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
import java.io.*;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
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
    //每个用户都有一个tableName
    public static String indexSymTableName;
    public static String indexDataTableName;

    //文件名称（不含后缀名）
    public static String filesName;

    //创建指标数据表的sql语句
    public static String createSql=new String();

    //标记是指标体系文件还是数据文件
    public static String fileType=new String();
    //文件存储路径
    @Value("${file-save-path}")
    private String fileSavePath;
    private String file_Path;

    @Autowired
    private IndexSymNodeService nodeService;

    @Autowired
    private UsersDataMapper usersDataMapper;

    @Autowired
    private SampleService sampleService;

    @Autowired
    private IndexSymNodeService indexSymNodeService;


    private IndexSym indexSym = new IndexSym();

    /**
     * @author xiaxue
     * @param file
     * @throws IOException
     */
    @PostMapping(value = "/excel/{value}")
    //@RequestParam("file") MultipartFile file
    public void uploadFileByExcel(@RequestParam(value = "file",required = false) MultipartFile file, @PathVariable("value") String v) throws IOException {
        cells.clear();
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
     * @param file
     */
    @RequestMapping("/xml/{value}")
    public void loadFileByXML(@RequestParam(value = "file",required = false) MultipartFile file, @PathVariable("value") String v) throws IOException {
        //清空数组
        cells.clear();
        String filename = file.getOriginalFilename();

        fileType=v;
        filesName=file.getOriginalFilename().substring(0,file.getOriginalFilename().indexOf("."));
        System.out.println("文件名："+filesName);
        //保存到本地
        String filePath = savaFileByNio((FileInputStream) file.getInputStream(), filename);
        try {
            // 创建解析器工厂
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = factory.newDocumentBuilder();
            // 创建一个Document对象
            Document doc = db.parse(filePath);
            NodeList indexSymList = doc.getElementsByTagName("nodeGroups");
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
                    // 区分,去掉空格和换行
                    if(childNodes.item(k).getNodeName().equals("node")){
                        NodeList childNode=childNodes.item(k).getChildNodes();
                        moreNode(childNode);
                    }

                    else if (childNodes.item(k).getNodeType() == Node.ELEMENT_NODE) {
                        // 获取element类型的节点和节点值
                        System.out.print("节点名：" + childNodes.item(k).getNodeName());
                        //System.out.print(" --- 节点值：" + childNodes.item(k).getFirstChild().getNodeValue());
                        System.out.println(" --- 节点值："+childNodes.item(k).getTextContent());
                        temp[j++]=childNodes.item(k).getTextContent();
                    }
                }
                cells.add(temp);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @RequestMapping("/keepExcel/{username}")
    public Result keep(@PathVariable("username") String name){
        userName=name;
        System.out.println("用户名：  "+name);
        return keepData(cells);
    }
    /**
     * 保存读取的indexSym,保存到List和数据库
     * @param list
     */
    public Result keepData(List<String[]> list){
        if(fileType.equals(""))
            return Result.fail();
        System.out.println("文件类型:  "+fileType);
        if(fileType.equals("indexSym")) {
            //头结点
            List<String> headNode=new ArrayList<>();
            headNode.add(filesName);
            headNode.add("0");
            headNode.add("0");
            headNode.add("0");
            //int headNodeID=nodeService.getHeadID(indexSymTableName,filesName);
            //拼接新表名
            indexSymTableName = userName+"_"+filesName; //+ "_IndexSym";
            System.out.println("指标体系名字： "+indexSymTableName);
            nodeService.createIndexSymTable(indexSymTableName);
            if(nodeService.insertIntoTable(indexSymTableName,filesName,1,1,0)<=0)
                return Result.fail();
            for (String[] l : list) {
                System.out.println(l[0]+"  "+ Integer.parseInt(l[1])+"  "+ Double.parseDouble(l[2])+"  "+ Integer.parseInt(l[3])+1);
                if(nodeService.insertIntoTable(indexSymTableName, l[0], Integer.parseInt(l[1]), Double.parseDouble(l[2]), Integer.parseInt(l[3])+1)<=0)
                    return Result.fail();
            }
        }else if(fileType.equals("indexdata")){
            indexDataTableName=userName+"_"+filesName+"_IndexData";
            System.out.println("指标数据名字: "+indexDataTableName);
            //拼接sql语句，因为指标个数不确定。
            String[] temp=list.get(1);
            int column=temp.length-1;//指标个数
            create_data_table(indexSymTableName);
            String name = null;
            int i=0;
            for (String[] l : list) {
                List<String> ins=new ArrayList<>();
                i=0;
                for(String t:l){
                    System.out.println(t);
                    if(i==0){
                        name=t;

                    }else{
                        ins.add(t);
                    }
                    i++;
                }
                try{
                    for(String s:ins){
                        System.out.println(s);
                    }
                    if(!sampleService.insertDataTable(indexDataTableName,ins))
                        return Result.fail();
                }catch(Exception e){
                    System.out.println("数据文件和节点文件不对应！");
                    return Result.fail();
                }
            }
        }
        return Result.success();
    }

    /**
     * @author xiaxue
     * 递归调用处理嵌套的node
     */
    public void moreNode(NodeList childNodes){
        String[] temp=new String[5];
        int j=0;
        for (int k = 0; k < childNodes.getLength(); k++) {
            // 区分,去掉空格和换行符
            if (childNodes.item(k).getNodeType() == Node.ELEMENT_NODE) {
                // 获取element类型的节点和节点值
                if(!childNodes.item(k).getNodeName().equals("node")) {
                    //System.out.print("节点名：" + childNodes.item(k).getNodeName());
                    //System.out.print(" --- 节点值：" + childNodes.item(k).getFirstChild().getNodeValue());
                    //System.out.println(" --- 节点值：" + childNodes.item(k).getTextContent());
                }
                //出现了嵌套node
                if(childNodes.item(k).getNodeName().equals("node")){
                    NodeList childNode=childNodes.item(k).getChildNodes();
                    moreNode(childNode);
                }
                temp[j++]=childNodes.item(k).getTextContent();
            }
        }
        cells.add(temp);
    }

    /**
     * 获取指定数据表中存储的指标体系
     * http://localhost:8877/indexsym/loadIndexSym=indexsym
     **/
    @GetMapping("/loadIndexSym")
    public List<IndexSymNode> load_indexsym(String table_name) {
        indexSym.setNodeList(nodeService.getIndex(table_name));
        return indexSym.getNodeList();
    }

    /**
     * 根据指标体系生成对应的数据表（叶子节点是表头
     * @param table_name 指标体系表
     * @return 是否成功建表（啊？？怎么成功建了表return回来的是false啊
     * http://localhost:8877/indexsym/create_data_table?table_name=indexsym
     */
    @GetMapping("/create_data_table")
    public boolean create_data_table(String table_name) {
        load_indexsym(table_name);
        indexSym.get_leaves();
        int leaf_num = indexSym.getLeaf_num();
        List<String> columnNames = new ArrayList<>();
        for(int i = 1; i <= leaf_num; i ++)
            columnNames.add("X" + i);
        indexDataTableName=table_name+"_data";  //"_IndexData";
        return sampleService.createDataTable(indexDataTableName, columnNames);
    }

    /**
     * @author lmy
     */
    @PostMapping(value = "/json")
    //@RequestParam("file") MultipartFile file
    public Result uploadFileByJson(@RequestParam(value = "file",required = false) MultipartFile file) {
        String filename = file.getOriginalFilename();
        System.out.println(filename);
        //文件名称（不含后缀名）
        filesName=file.getOriginalFilename().substring(0,file.getOriginalFilename().indexOf("."));
        //保存到本地
        String res = null;
        try {
            res = savaFileByNio((FileInputStream) file.getInputStream(), filename);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.fail();
        }
        return res!=null?Result.success():Result.fail();
    }

    /**
     * @author xiaxue
     * @param fis
     * @param fileName
     * @return
     */
    public String savaFileByNio(FileInputStream fis, String fileName) {
        // 这个路径最后是在: 你的项目路径/upload  也就是和src同级
        this.file_Path = this.fileSavePath+fileName;
        // 判断父文件夹是否存在
        File file = new File(this.file_Path);
        //System.out.println(file.getPath());
        if (file.getParentFile() != null || !file.getParentFile().isDirectory()) {
            file.getParentFile().mkdirs();
        }
        // 通过NIO保存文件到本地磁盘
        try {
            FileOutputStream fos = new FileOutputStream(this.file_Path);
            FileChannel inChannel = fis.getChannel();
            FileChannel outChannel = fos.getChannel();
            inChannel.transferTo(0, inChannel.size(), outChannel);
            inChannel.close();
            outChannel.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.file_Path;
    }

    /**
     * @author lmy
     */
    @RequestMapping("/keepJson")
    public Result keepJson(@RequestParam String userName){
        if(this.file_Path==null)
            return Result.fail();
        List<String> headNode=new ArrayList<>();
        headNode.add(filesName);
        headNode.add("0");
        headNode.add("0");
        headNode.add("0");
        IndexSymNode t=new IndexSymNode();
        indexSymTableName = userName+"_"+filesName;  //+ "_IndexSym";
        nodeService.createIndexSymTable(indexSymTableName);
        if(nodeService.insertIntoTable(indexSymTableName,filesName,1,1,0)<=0)
            return Result.fail();
        boolean result=true;
        System.out.println(fileSavePath+filesName+".json");
        try {
            result = nodeService.saveJsonData(indexSymTableName,fileSavePath+filesName+".json");
        } catch (IOException e) {
            e.printStackTrace();
            return  Result.fail();
        }
        return result==true?Result.success():Result.fail();
    }

    /**
     * @author lmy
     */
    @RequestMapping("/downloadJson")
    public void downloadJson(HttpServletResponse response) throws Exception {
        this.download("example\\example.json",response);
    }

    @RequestMapping("/downloadExcel1")
    public void downloadExcel1(HttpServletResponse response) throws Exception {
        this.download("example\\indexsymOrigin.xlsx",response);
    }

    @RequestMapping("/downloadExcel2")
    public void downloadExcel2(HttpServletResponse response) throws Exception {
        this.download("example\\index.xlsx",response);
    }

    @RequestMapping("/downloadXml")
    public void downloadXml(HttpServletResponse response) throws Exception {
        this.download("example\\temp.xml",response);
    }

    public void download(String fileName,HttpServletResponse response) throws Exception{
        //这里写要让前端下载的文件的路径
        File file = new File(this.fileSavePath + fileName);
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
    public Result insertUsersData()  {
        int flag=0;
        try{
            usersDataMapper.createTable(userName+"_Data");
        }catch (Exception e){
            flag=-1;
            usersDataMapper.insertIntoTable(userName+"_Data",indexDataTableName,indexSymTableName);
        }
        if(flag==0){
            usersDataMapper.insertIntoTable(userName+"_Data",indexDataTableName,indexSymTableName);
        }
        return Result.success();
    }

    /**
     * @author xiaxue
     * 获取该用户所有上传的指标体系的名字
     */
    @RequestMapping("/getAllSyms/{userName}")
    public Result getAllSyms(@PathVariable("userName")String userName){
        //先找到userData表
        String userData=userName+"_Data";
        List<String> indexSymDTNames=sampleService.getUserData(userData);
        Result result=new Result();
        return indexSymDTNames==null?Result.fail():Result.success(indexSymDTNames);
    }
    /**
     * @author
     * @param dbName
     * @return
     * 获取用户indexSym表中的所有信息
     * 点击树节点显示信息
     */

    @RequestMapping("/getAllNodeInfo/{dbName}")
    public Result getAllNodeInfo(@PathVariable("dbName")String dbName){
        List<IndexSymNode> data=nodeService.getAllNodeInfo(dbName);
        return data==null?Result.fail():Result.trans(data);
    }

    /**
     * @author lmy
     */
    @GetMapping("/getOrigTreeData")
    public Result getOrigTreeData() throws Exception
    {
        List<IndexSymNode> indexSym = indexSymNodeService.getIndex(indexSymTableName);
        // 转换成画树需要的类
        List<TreeData> treeData=indexSymNodeService.getIndexSymData(indexSym);
        JSONArray jsonArray=JSONArray.parseArray(JSON.toJSONString(treeData));
        // 返回构建好的数据
        return jsonArray.size()>0?Result.success(jsonArray):Result.fail();
    }
}