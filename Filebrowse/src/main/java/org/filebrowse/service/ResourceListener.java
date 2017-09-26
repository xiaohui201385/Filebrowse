package org.filebrowse.service;

import static org.hamcrest.CoreMatchers.nullValue;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.filebrowse.entity.FileType;
import org.filebrowse.entity.PreviewFile;


public class ResourceListener{

    private static ExecutorService fixedThreadPool = Executors.newFixedThreadPool(5);
    private WatchService ws;
    private String listenerPath;
    private static Map<String, String> result=new HashMap<>();
    private FileTypeService fileTypeService=new FileTypeService();
    private PreviewFileService previewFileService=new PreviewFileService();

    private ResourceListener(String path) {
        try {
            ws = FileSystems.getDefault().newWatchService();
            this.listenerPath = path;
            //初始化所有数据
            start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void start() {
        fixedThreadPool.execute(new Listner(ws, this.listenerPath));
    }

    public static Map<String, String> addListener(String path) throws IOException {
        ResourceListener resourceListener = new ResourceListener(path);
        Path p = Paths.get(path);
        p.register(resourceListener.ws, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE,
                StandardWatchEventKinds.ENTRY_CREATE);
        File file = new File(p.toString());
        File[] listFiles = file.listFiles();
        for(File f:listFiles){
            Path fp = Paths.get(f.getAbsolutePath());
            fp.register(resourceListener.ws, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE,
                    StandardWatchEventKinds.ENTRY_CREATE);
        }
        return result;
    }


    class Listner implements Runnable {
        private WatchService service;
        private String rootPath;

        public Listner(WatchService service, String rootPath) {
            this.service = service;
            this.rootPath = rootPath;
        }

        public void run() {
            try {
                while (true) {
                    WatchKey watchKey = service.take();
                    List<WatchEvent<?>> watchEvents = watchKey.pollEvents();
                    for (WatchEvent<?> event : watchEvents) {
                        // TODO 根据事件类型采取不同的操作。。。。。。。
                        result.put(rootPath + "/" + event.context(), event.kind()+"");
                        //1.获取文件的全路径和事件触发类型
                        String filePath=rootPath+"/"+event.context();
                        String name=event.context()+"";
                        String kind=event.kind()+"";
                        System.out.println(filePath+event.kind());
                        //2.根据路径获取文件
                        String[] split = filePath.split("/");
                        int len=split.length;
                        //3.根据触发类型和文件的类型来判断CRUD操作
                            //1.Create : 用全路径去匹配
                        if(kind.equals("ENTRY_CREATE")){
                            if(len==5){//目录
                                List<FileType> all = fileTypeService.getAll();
                                FileType fileType = new FileType(name);
                                boolean contains = all.contains(fileType);
                                if(!contains){
                                    fileTypeService.addOneDefault(fileType);
                                }
                            }else if(len==6){//文件
                                List<PreviewFile> byLocation = previewFileService.getByLocation(filePath);
                                if(byLocation!=null&&byLocation.size()>0){
                                    //文件存在
                                }else{
                                    File file = new File(filePath);
                                    String typeName=split[split.length-2];
                                    int type=fileTypeService.getByName(typeName).get(0).getId();
                                    previewFileService.insertOne(new PreviewFile(file.getName(), new Date(file.lastModified()), filePath, type));
                                }
                            }
                        }
                        else if(kind.equals("ENTRY_DELETE")){
                            //2.Delete : 判断文件类型来执行
                            if(len==5){
                                fileTypeService.delByName(name);
                            }else if(len==6){
                                previewFileService.delByLocation(filePath);
                            }
                        }else{
                            //3.Modified : 在是目录又是Modified的时候,不做操作
                            if(len==5){
                                
                            }else if(len==6){
                                File file = new File(filePath);
                                previewFileService.updateByLocation(filePath, new Date(file.lastModified()));
                            }
                        }
                        
                    }
                    System.out.println(result);
                    watchKey.reset();
                    result.clear();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println("fdsfsdf");
                try {
                    service.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}


