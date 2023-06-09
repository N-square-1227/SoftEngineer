package com.se.softengineer.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.se.softengineer.entity.IndexSym;
import com.se.softengineer.entity.IndexSymNode;
import com.se.softengineer.entity.UsersData;
import com.se.softengineer.mapper.IndexSymNodeMapper;
import com.se.softengineer.service.IndexSymNodeService;
import com.se.softengineer.service.IndexSymService;
import com.se.softengineer.service.SampleService;
import com.se.softengineer.service.UsersDataService;
import com.se.softengineer.utils.AnalyExcel;
import com.se.softengineer.utils.QueryPageParam;
import com.se.softengineer.utils.Result;
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
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

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
    public static List<String[]> cells = new ArrayList<>();
    //每个用户都有一个tableName
    public static String indexSymTableName;
    public static String indexDataTableName;

    //文件名称（不含后缀名）
    public static String filesName;

    //创建指标数据表的sql语句
    public static String createSql = "";

    //标记是指标体系文件还是数据文件
    public static String fileType = "";
    //文件存储路径
    @Value("${file-save-path}")
    private String fileSavePath;
    private String file_Path;
    //标记是否是XML类型
    String FT = "";

    @Autowired
    private IndexSymNodeService indexSymNodeService;

    @Autowired
    private UsersDataService usersDataService;

    @Autowired
    private SampleService sampleService;

    @Autowired
    private IndexSymService indexSymService;

    @Autowired
    IndexSymNodeMapper indexSymNodeMapper;

    private IndexSym indexSym = new IndexSym();

    /**
     * @author xiaxue
     */
    @PostMapping(value = "/excel/{value}")
    //@RequestParam("file") MultipartFile file
    public void uploadFileByExcel(@RequestParam(value = "file", required = false) MultipartFile file, @PathVariable("value") String v) throws IOException {
        FT = "";
        cells.clear();
        String filename = file.getOriginalFilename();
        fileType = v;
        System.out.println("类型：" + fileType);
        filesName = Objects.requireNonNull(file.getOriginalFilename()).substring(0, file.getOriginalFilename().indexOf("."));
        System.out.println(filesName);
        //保存到本地
        String filePath = savaFileByNio((FileInputStream) file.getInputStream(), filename);
        cells = AnalyExcel.readExcelFile(file, 1);
        //keepData(l);
    }

    /**
     * @author xiaxue
     */
    @RequestMapping("/xml/{value}")
    public void loadFileByXML(@RequestParam(value = "file", required = false) MultipartFile file, @PathVariable("value") String v) throws IOException {
        FT = "XML";
        //清空数组
        cells.clear();
        String filename = file.getOriginalFilename();

        fileType = v;
        filesName = Objects.requireNonNull(file.getOriginalFilename()).substring(0, file.getOriginalFilename().indexOf("."));
        System.out.println("文件名：" + filesName);
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
                Node book = indexSymList.item(i);
                // 获取book节点所有属性集合
                org.w3c.dom.NamedNodeMap attrs = book.getAttributes();

                //System.out.println("第" + (i + 1) + "本书共有" + attrs.getLength() + "属性");
                // 遍历book属性，不知道节点属性和属性名情况
                for (int j = 0; j < attrs.getLength(); j++) {
                    // 获取某一个属性
                    Node attr = attrs.item(j);
                    //System.out.print("属性名:" + attr.getNodeName());
                    //System.out.println(" --- 属性值:" + attr.getNodeValue());
                }
                NodeList childNodes = book.getChildNodes();
                String[] temp = new String[5];
                int j = 0;
                for (int k = 0; k < childNodes.getLength(); k++) {
                    // 区分,去掉空格和换行
                    if (childNodes.item(k).getNodeName().equals("node")) {
                        NodeList childNode = childNodes.item(k).getChildNodes();
                        moreNode(childNode);
                    } else if (childNodes.item(k).getNodeType() == Node.ELEMENT_NODE) {
                        // 获取element类型的节点和节点值
                        System.out.print("节点名：" + childNodes.item(k).getNodeName());
                        //System.out.print(" --- 节点值：" + childNodes.item(k).getFirstChild().getNodeValue());
                        System.out.println(" --- 节点值：" + childNodes.item(k).getTextContent());
                        temp[j++] = childNodes.item(k).getTextContent();
                    }
                }
                cells.add(temp);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @RequestMapping("/keepExcel/{username}")
    public Result keep(@PathVariable("username") String name) {
        userName = name;
        System.out.println("用户名：  " + name);
        return keepData(cells);
    }

    /**
     * 保存读取的indexSym,保存到List和数据库
     *
     * @param list
     */
    public Result keepData(List<String[]> list) {
        if (fileType.equals(""))
            return Result.fail();
        System.out.println("文件类型:  " + fileType);
        if (fileType.equals("indexSym")) {
            //头结点
//            List<String> headNode=new ArrayList<>();
//            headNode.add(filesName);
//            headNode.add("0");
//            headNode.add("0");
//            headNode.add("0");
            //int headNodeID=nodeService.getHeadID(indexSymTableName,filesName);
            //拼接新表名
            indexSymTableName = userName + "_" + filesName;
            System.out.println("指标体系名字： " + indexSymTableName);
            /* 如果上传了同名的指标体系，直接覆盖; 同时在user_data数据库中写入时也要判断是否有重复 by wxy*/
            indexSymNodeService.dropExistTable(indexSymTableName);
            indexSymNodeService.createIndexSymTable(indexSymTableName);
            /* 先插入根节点 */
            if (indexSymNodeService.insertIntoTable(indexSymTableName, filesName, 1, 1, 0) <= 0)
                return Result.fail();
            if (FT.equals("XML")) {
                for (String[] l : list) {
                    System.out.println("————————————————————————————————————" + l[0] + "  " + Integer.parseInt(l[1]) + "  " + Double.parseDouble(l[2]) + "  " + Integer.parseInt(l[3]) + 1);
                    indexSymNodeMapper.insertIntoSheet(indexSymTableName, Integer.parseInt(l[1]) + 1, l[0], Integer.parseInt(l[2]), Double.parseDouble(l[3]), Integer.parseInt(l[4]) + 1);
                    //return Result.fail();
                }
            } else {
                for (String[] l : list) {
                    System.out.println(l[0] + "  " + Integer.parseInt(l[1]) + "  " + Double.parseDouble(l[2]) + "  " + Integer.parseInt(l[3]) + 1);
                    if (indexSymNodeService.insertIntoTable(indexSymTableName, l[0], Integer.parseInt(l[1]), Double.parseDouble(l[2]), Integer.parseInt(l[3]) + 1) <= 0)
                        return Result.fail();
                }
            }
        } else if (fileType.equals("indexdata")) {
            indexDataTableName = userName + "_" + filesName + "_data";
            System.out.println("指标数据名字: " + indexDataTableName);
            //拼接sql语句，因为指标个数不确定。
            //指标个数
            try {
                create_data_table(indexSymTableName);
            } catch (Exception e) {
                return Result.fail();
            }
            int i;
            for (String[] l : list) {
                List<String> ins = new ArrayList<>();
                i = 0;
                for (String t : l) {
                    System.out.println(t);
                    if (i == 0) {

                    } else {
                        ins.add(t);
                    }
                    i++;
                }
                try {
                    for (String s : ins) {
                        System.out.println(s);
                    }
                    if (!sampleService.insertDataTable(indexDataTableName, ins))
                        return Result.fail();
                } catch (Exception e) {
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
    public void moreNode(NodeList childNodes) {
        String[] temp = new String[5];
        int j = 0;
        for (int k = 0; k < childNodes.getLength(); k++) {
            // 区分,去掉空格和换行符
            if (childNodes.item(k).getNodeType() == Node.ELEMENT_NODE) {
                // 获取element类型的节点和节点值
                if (!childNodes.item(k).getNodeName().equals("node")) {
                    //System.out.print("节点名：" + childNodes.item(k).getNodeName());
                    //System.out.print(" --- 节点值：" + childNodes.item(k).getFirstChild().getNodeValue());
                    //System.out.println(" --- 节点值：" + childNodes.item(k).getTextContent());
                }
                //出现了嵌套node
                if (childNodes.item(k).getNodeName().equals("node")) {
                    NodeList childNode = childNodes.item(k).getChildNodes();
                    moreNode(childNode);
                }
                temp[j++] = childNodes.item(k).getTextContent();
            }
        }
        cells.add(temp);
    }

    /**
     * 获取指定数据表中存储的指标体系
     * http://localhost:8877/import/loadIndexSym=indexsym
     **/
    @GetMapping("/loadIndexSym")
    public List<IndexSymNode> load_indexsym(String table_name) {
        indexSym.setNodeList(indexSymNodeService.getIndex(table_name));
        return indexSym.getNodeList();
    }

    /**
     * 根据指标体系生成对应的数据表（叶子节点是表头
     *
     * @param table_name 指标体系表
     * @return 是否成功建表（啊？？怎么成功建了表return回来的是false啊
     * http://localhost:8877/import/create_data_table?table_name=indexsym
     */
    @GetMapping("/create_data_table")
    public boolean create_data_table(String table_name) {
        load_indexsym(table_name);
        indexSym.get_leaves();
        int leaf_num = indexSym.getLeaf_num();
        List<String> columnNames = new ArrayList<>();
        for (int i = 1; i <= leaf_num; i++)
            columnNames.add("X" + i);
        indexDataTableName = table_name + "_data";
        /* 如果已经上传过，直接覆盖 */
        sampleService.dropExistTable(indexDataTableName);
        return sampleService.createDataTable(indexDataTableName, columnNames);
    }

    /**
     * @author lmy
     */
    @PostMapping(value = "/json")
    //@RequestParam("file") MultipartFile file
    public Result uploadFileByJson(@RequestParam(value = "file", required = false) MultipartFile file) {
        String filename = file.getOriginalFilename();
        System.out.println(filename);
        //文件名称（不含后缀名）
        filesName = Objects.requireNonNull(file.getOriginalFilename()).substring(0, file.getOriginalFilename().indexOf("."));
        //保存到本地
        String res;
        try {
            res = savaFileByNio((FileInputStream) file.getInputStream(), filename);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.fail();
        }
        return res != null ? Result.success() : Result.fail();
    }

    /**
     * @param fis
     * @param fileName
     * @return
     * @author xiaxue
     */
    public String savaFileByNio(FileInputStream fis, String fileName) {
        // 这个路径最后是在: 你的项目路径/upload  也就是和src同级
        this.file_Path = this.fileSavePath + fileName;
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
    public Result keepJson(@RequestParam String userName) {
        if (this.file_Path == null)
            return Result.fail();
        List<String> headNode = new ArrayList<>();
        headNode.add(filesName);
        headNode.add("0");
        headNode.add("0");
        headNode.add("0");
        IndexSymNode t = new IndexSymNode();
        indexSymTableName = userName + "_" + filesName;
        /* 如果上传了同名的指标体系，直接覆盖; 同时在user_data数据库中写入时也要判断是否有重复 by wxy*/
        indexSymNodeService.dropExistTable(indexSymTableName);
        indexSymNodeService.createIndexSymTable(indexSymTableName);
        if (indexSymNodeService.insertIntoTable(indexSymTableName, filesName, 1, 1, 0) <= 0)
            return Result.fail();
        boolean result;
        System.out.println(fileSavePath + filesName + ".json");
        try {
            result = indexSymNodeService.saveJsonData(indexSymTableName, fileSavePath + filesName + ".json");
        } catch (IOException e) {
            e.printStackTrace();
            return Result.fail();
        }
        return result ? Result.success() : Result.fail();
    }

    /**
     * @author lmy
     */
    @RequestMapping("/downloadJson")
    public void downloadJson(HttpServletResponse response) throws Exception {
        this.download("example\\example.json", response);
    }

    @RequestMapping("/downloadExcel1")
    public void downloadExcel1(HttpServletResponse response) throws Exception {
        this.download("example\\indexsymOrigin.xlsx", response);
    }

    @RequestMapping("/downloadExcel2")
    public void downloadExcel2(HttpServletResponse response) throws Exception {
        this.download("example\\index.xlsx", response);
    }

    @RequestMapping("/downloadXml")
    public void downloadXml(HttpServletResponse response) throws Exception {
        this.download("example\\temp.xml", response);
    }

    public void download(String fileName, HttpServletResponse response) throws Exception {
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
        //Content-Disposition的作用：告知浏览器以何种方式显示响应返回的文件，用浏览器打开还是以附件的形式下载到本地保存
        //attachment表示以附件方式下载   inline表示在线打开   "Content-Disposition: inline; filename=文件名.mp3"
        // filename表示文件的默认名称，因为网络传输只支持URL编码的相关支付，因此需要将文件名URL编码后进行传输,前端收到后需要反编码才能获取到真正的名称
        response.setHeader("Content-Disposition", "attachment;filename=" + java.net.URLEncoder.encode(realFileName.trim(), StandardCharsets.UTF_8));
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
            try {
                bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * v1.0
     *
     * @author lmy
     * v2.0
     * @author wxy
     **/
    @RequestMapping("/insertUsersData")
    public Result insertUsersData() {
        /** v 1.0 点击确认后创建表并插入数据
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
         */

        /** v 2.0
         * 调整为：
         * 用户注册时即在数据库中创建表，在上传数据界面点击确定直接插入；
         * 删除用户时连带username_Data表一起删除
         * 修改用户名时也需要修改这个表名
         *
         * 解决某未上传过数据的用户登陆后进入“指标优化”界面直接报错的问题
         * 修改register、update、delete方法
         * 同时注意当没有选择待优化的指标体系时，应该添加点击确认后的错误提示信息
         **/
        try {
            /* 相同名字的指标体系上传时覆盖原有的指标体系，记录写入user_data表时也应该去重 */
            usersDataService.insertIntoTable(userName + "_data", indexDataTableName, indexSymTableName);
        } catch (Exception e) {
            /* 插入不成功返回前端提示错误信息 */
            return Result.fail();
        }
        return Result.success();
    }

//    /**
//     * @author xiaxue
//     * 获取该用户所有上传的指标体系的名字
//     */
//    @RequestMapping("/getAllSyms/{userName}")
//    public Result getAllSyms(@PathVariable("userName")String userName){
//        //先找到userData表
//        String userData=userName+"_data";
//        List<String> indexSymDTNames=usersDataService.getIndexSymTableNames(userData);
//        return indexSymDTNames==null?Result.fail():Result.success(indexSymDTNames);
//    }

    @PostMapping("/indexSymListPage")
    public Result nodeListPage(@RequestBody QueryPageParam query) {
        HashMap param = query.getParam();
        String tableName = (String) param.get("table_name");
        String queryContent = (String) param.get("queryName");
        System.out.println(tableName);

        Page<UsersData> page = new Page();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        LambdaQueryWrapper<UsersData> lambdaQueryWrapper = new LambdaQueryWrapper();
        /* 条件 */
        if (StringUtils.isNotBlank(queryContent) && !("null".equals(queryContent)))
            lambdaQueryWrapper.like(UsersData::getIndexSymDTName, queryContent);


        IPage result = usersDataService.getISDTNamePage(page, tableName, lambdaQueryWrapper);
        System.out.println("total==" + result.getTotal());
        return Result.success(result.getRecords(), result.getTotal());
    }

    /**
     * @param dbName
     * @return 获取用户indexSym表中的所有信息
     * 点击树节点显示信息
     * @author
     */

    @RequestMapping("/getAllNodeInfo/{dbName}")
    public Result getAllNodeInfo(@PathVariable("dbName") String dbName) {
        List<IndexSymNode> data = indexSymNodeService.getAllNodeInfo(dbName);
        return data == null ? Result.fail() : Result.trans(data);
    }
}