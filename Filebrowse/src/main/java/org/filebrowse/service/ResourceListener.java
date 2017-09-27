package org.filebrowse.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.filebrowse.dao.FileTypeDao2;
import org.filebrowse.dao.PreviewFileDao2;
import org.filebrowse.entity.FileType;
import org.filebrowse.entity.PreviewFile;


public class ResourceListener {

    private static ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);
    private WatchService ws;
    private String listenerPath;
    private static Map<String, String> result = new HashMap<>();
    private static FileTypeDao2 fileTypeService = new FileTypeDao2();
    private static PreviewFileDao2 previewFileService = new PreviewFileDao2();

    private ResourceListener(String path) {
        try {
            ws = FileSystems.getDefault().newWatchService();
            this.listenerPath = path;
            start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void initFiles(List<FileType> initTypes) {
        List<PreviewFile> previewFiles = new ArrayList<>();
        for (FileType fileType : initTypes) {
            int type = fileType.getId();
            String typeName = fileType.getName();
            File file = new File("C:/Program Files/Microsoft Office Web Apps/OpenFromUrlWeb/docview/" + typeName + "/");
            File[] files = file.listFiles();
            if (files.length > 0) {
                for (File f : files) {
                    String fileName = f.getName();
                    Date time = new Date(f.lastModified());
                    String location = f.getAbsolutePath();
                    location=location.replaceAll("\\\\", "/");
                    PreviewFile previewFile = new PreviewFile(fileName, time, location, type);
                    previewFiles.add(previewFile);
                }
            }
        }
        previewFileService.addList(previewFiles);
    }

    private static List<FileType> initType(File[] listFiles) {
        List<FileType> fileTypes = new ArrayList<>();
        if (listFiles.length > 0) {
            for (int i = 0; i < listFiles.length; i++) {
                File listFile = listFiles[i];
                String name = listFile.getName();
                FileType fileType = new FileType(i + 1, name);
                fileTypes.add(fileType);
            }
        }
        fileTypeService.addList(fileTypes);
        return fileTypes;
    }

    private static File[] getTypeList() {
        File file = new File("C:/Program Files/Microsoft Office Web Apps/OpenFromUrlWeb/docview/");
        if (!file.exists()) {
            file.mkdirs();
        }
        File[] listFiles = file.listFiles();
        return listFiles;
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
        for (File f : listFiles) {
            Path fp = Paths.get(path+"/"+f.getName());
            System.out.println(fp);
            String fpath=fp.toString().replaceAll("\\\\", "/");
            ResourceListener listener=new ResourceListener(fpath);
            fp.register(listener.ws, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE,
                    StandardWatchEventKinds.ENTRY_CREATE);
        }
        System.out.println("init");
        File[] listFiless = getTypeList();
//        previewFileService.delAll();
        fileTypeService.delAll();
        List<FileType> initTypes = initType(listFiless);
        initFiles(initTypes);
        System.out.println("init success");
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
                        System.out.println(rootPath+"/"+event.context()+" : "+event.kind());
                        // 1.获取文件的全路径和事件触发类型
                        String filePath = rootPath + "/" + event.context();
                        String name = event.context() + "";
                        String kind = event.kind() + "";
                        // 2.根据路径获取文件
                        String[] split = filePath.split("/");
                        int len = split.length;
                        // 3.根据触发类型和文件的类型来判断CRUD操作
                        // 1.Create : 用全路径去匹配
                        if (kind.equals("ENTRY_CREATE")) {
                            if (len == 6) {// 目录
                                List<FileType> all = fileTypeService.getAll();
                                FileType fileType = new FileType(name);
                                boolean contains = all.contains(fileType);
                                if (!contains) {
                                    int addOneDefault = fileTypeService.addOneDefault(fileType);
                                    System.out.println("create dir :"+addOneDefault);
                                    List<PreviewFile> previewFiles = new ArrayList<>();
                                    fileType=fileTypeService.getByName(name).get(0);
                                    int type = fileType.getId();
                                    String typeName = fileType.getName();
                                    File file = new File("C:/Program Files/Microsoft Office Web Apps/OpenFromUrlWeb/docview/" + typeName + "/");
                                    File[] files = file.listFiles();
                                    if (files.length > 0) {
                                        for (File f : files) {
                                            String fileName = f.getName();
                                            Date time = new Date(f.lastModified());
                                            String location = f.getAbsolutePath();
                                            location=location.replaceAll("\\\\", "/");
                                            PreviewFile previewFile = new PreviewFile(fileName, time, location, type);
                                            previewFiles.add(previewFile);
                                        }
                                    }
                                    previewFileService.addList(previewFiles);
                                }
                                ResourceListener resourceListener = new ResourceListener(filePath);
                                Path p = Paths.get(filePath);
                                p.register(resourceListener.ws, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE,
                                        StandardWatchEventKinds.ENTRY_CREATE);
                            } else if (len == 7) {// 文件
                                List<PreviewFile> byLocation = previewFileService.getByLocation(filePath);
                                if (byLocation != null && byLocation.size() > 0) {
                                    // 文件存在
                                    System.out.println("文件已存在");
                                } else {
                                    File file = new File(filePath);
                                    String typeName = split[split.length - 2];
                                    int type = fileTypeService.getByName(typeName).get(0).getId();
                                    int insertOne = previewFileService.insertOne(new PreviewFile(file.getName(),
                                            new Date(file.lastModified()), filePath, type));
                                    System.out.println("create : "+insertOne);
                                }
                            }
                        } else if (kind.equals("ENTRY_DELETE")) {
                            // 2.Delete : 判断文件类型来执行
                            if (len == 6) {
                                int delByName = fileTypeService.delByName(name);
                                System.out.println("del type:"+delByName);
                            } else if (len == 7) {
                                int delByLocation = previewFileService.delByLocation(filePath);
                                System.out.println("del file:"+delByLocation);
                            }
                        } else {
                            // 3.Modified : 在是目录又是Modified的时候,不做操作
                            if (len == 6) {

                            } else if (len == 7) {
                                File file = new File(filePath);
                                int updateByLocation = previewFileService.updateByLocation(filePath, new Date(file.lastModified()));
                                System.out.println("update file :"+updateByLocation);
                            }
                        }

                    }
                    watchKey.reset();
                }
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            } finally {
                System.out.println("error");
                try {
                    service.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
