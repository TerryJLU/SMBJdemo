import com.hierynomus.smbj.SmbConfig;
import com.hierynomus.msdtyp.FileTime;
import com.hierynomus.msfscc.fileinformation.FileIdBothDirectoryInformation;
import com.hierynomus.smbj.auth.AuthenticationContext;
import com.hierynomus.smbj.session.Session;
import com.hierynomus.smbj.share.DiskShare;
import com.hierynomus.smbj.connection.Connection;
import com.hierynomus.smbj.SMBClient;
import com.hierynomus.mssmb2.SMBApiException;
import com.hierynomus.mserref.NtStatus;
import com.hierynomus.smbj.share.File;
import com.hierynomus.msdtyp.AccessMask;
import com.hierynomus.mssmb2.SMB2CreateDisposition;
import com.hierynomus.mssmb2.SMB2ShareAccess;
import com.hierynomus.msfscc.FileAttributes;
import com.hierynomus.mssmb2.SMB2CreateOptions;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.EnumSet;
import java.io.BufferedReader;
import java.io.StringReader;

public class SMB2 {
    public static void main(String[] args) {

        String username = "username"; //用户名
        String password = "password"; //密码
        String ipAddress = "127.0.0.1";
        String shareName = "test2"; //the share folder name ,共享文件夹的名字
        SMBClient client;
        Session session;
         Connection connection;

         Long fileSize1 = null ;

        SmbConfig config = SmbConfig.builder()
                .withDfsEnabled(false)
                .build();
         client = new SMBClient(config);
        try {

            connection = client.connect(ipAddress, 445);
            AuthenticationContext authContext = new AuthenticationContext(username, password.toCharArray(), null);
            session = connection.authenticate(authContext);


            DiskShare share = (DiskShare) session.connectShare(shareName);
            System.out.print(share);

            String directoryPath = "/";
            String newestFileName = null;
            String oldFileName = null;
            FileTime oldmodified = null;
            FileTime newestLastModified = null;

            for (FileIdBothDirectoryInformation fileInformation : share.list(directoryPath)) {
                String fileName = fileInformation.getFileName();
                Long fileSize = fileInformation.getEndOfFile();
                FileTime lastModified = fileInformation.getLastWriteTime();


                fileSize1 = fileSize;

                System.out.println("File: " + fileName);
                System.out.println("Size: " + fileSize);
                System.out.println("Last Modified: " + lastModified);

                // 在这里可以执行其他文件信息处理操作

                if (newestLastModified == null || (lastModified.getWindowsTimeStamp()> newestLastModified.getWindowsTimeStamp())) {
                    if(fileSize!=0) {
                        oldFileName = newestFileName;
                        oldmodified = newestLastModified;
                        newestFileName = fileName;
                        newestLastModified = lastModified;
                    }
                }

                System.out.println("---------FileTime time1------------");
            }
            System.out.println("File name oldFileName: " + oldFileName);
            System.out.println("File name  oldmodified: " + oldmodified);
            System.out.println("File name: " + newestFileName);
            System.out.println("Last Modified: " + newestLastModified);
}
