package ru.rgs.csvparser.service;

import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.rgs.csvparser.feign.UserClient;
import ru.rgs.csvparser.model.UserReq;
import ru.rgs.csvparser.model.UserResp;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class CsvParserServiceImpl implements CsvParserService {
    private Service service;
    @Autowired
    private UserClient userClient;
    public CsvParserServiceImpl() {
    }

    private String getScoring(String fio, String contractDate) {
            UserReq userReq = new UserReq(fio,contractDate);
            UserResp userResp;
            try {
                userResp = userClient.getUser(userReq);
                return userResp.toString();
            }
            catch (FeignException e) {
                return "ошибка обработки";
            }
    }
    @Override
    public Path processCsv(Path source) {
        try {
            List<String> sourceLines = Files.readAllLines(source);
            StringBuilder fileLines = new StringBuilder("CLIENT_NAME,CONTRACT_DATE,SCORING\r\n");
            for (int i = 1; i < sourceLines.size(); i++) {
                try {
                    String line = sourceLines.get(i).toUpperCase();
                    String[] fields = line.split(",");
                    String fio = fields[0] + " " + fields[2] + " " + fields[1];
                    fileLines.append( fio + "," + fields[3]+","+getScoring(fio,fields[3])+"\r\n");

                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            }
            Path dest = Paths.get(source.getParent() + "/r_" + source.getFileName());
            if (!Files.exists(dest))
                Files.createFile(dest);
            Files.write(dest, fileLines.toString().getBytes(Charset.forName("UTF-8")));
            return dest;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
