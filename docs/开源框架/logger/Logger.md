### Logger扩展记录

#### 新增Logger本地日志存储路径配置
 1. 关联文件
 com.common.loggerx.MCsvFormatStrategy
 com.common.loggerx.MDiskLogStrategy
 
 2. 使用方式
```
 FormatStrategy diskFormatStrategy = MCsvFormatStrategy.newBuilder()
                    .tag("管理工具")
                    .defPath("ManagementTool")
                    .build();
            Logger.addLogAdapter(new DiskLogAdapter(diskFormatStrategy));
```

#### Logger日志本地存储策略
DiskLogStrategy - > WriteHandler.handleMessage(Message msg) 

```
    @Override public void handleMessage(@NonNull Message msg) {
      String content = (String) msg.obj;

      FileWriter fileWriter = null;
      File logFile = getLogFile(folder, "logs");

      try {
        fileWriter = new FileWriter(logFile, true); //通过FIleWriter的对文件进行累加写日志

        writeLog(fileWriter, content);

        fileWriter.flush();
        fileWriter.close();
      } catch (IOException e) {
        if (fileWriter != null) {
          try {
            fileWriter.flush();
            fileWriter.close();
          } catch (IOException e1) { /* fail silently */ }
        }
      }
    }
```

1.  存储路径变更
存储路径的变更会随着日志文件数量大小的变更导致写本地日志的速度越来越慢。

```
private File getLogFile(@NonNull String folderName, @NonNull String fileName) {
      checkNotNull(folderName);
      checkNotNull(fileName);

      File folder = new File(folderName);
      if (!folder.exists()) { 
        folder.mkdirs();
      }

      int newFileCount = 0;
      File newFile;
      File existingFile = null;

      newFile = new File(folder, String.format("%s_%s.csv", fileName, newFileCount));
      while (newFile.exists()) {//循环遍历文件是否已存在，已存在则递增newFileCount来创新newFile继续循环，直到newFile不存在为知。
        existingFile = newFile;
        newFileCount++;
        newFile = new File(folder, String.format("%s_%s.csv", fileName, newFileCount));
      }

      //如果有已存在的最后的日志文件，并且该日志文件大小超过设定的最大文件大小，则返回新的日志文件对象.否则继续在上次未超过大小的日志文件写日志
      if (existingFile != null) {
        if (existingFile.length() >= maxFileSize) {
          return newFile;
        }
        return existingFile;
      }

      //如果没有已存在文件，则直接返回新日志文件。
      return newFile;
    }
  }
```