package ru.rgs.csvparser.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.rgs.csvparser.feign.UserClient;
import ru.rgs.csvparser.model.UserReq;
import ru.rgs.csvparser.model.UserResp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class CsvParserServiceImpl implements CsvParserService {
    private Service service;
    @Autowired
    private UserClient userClient;

    public CsvParserServiceImpl() {


    }

    @Override
    public Path processCsv(Path source) {
        try {
            List<String> sourceLines = Files.readAllLines(source);
            UserResp userResp=userClient.getUser(new UserReq( "АНДРЕЙ ПАВЛОВИЧ КУКСЕНКО", "2018-07-02"));
            System.out.println("userResp="+userResp);
//            Path dest = Paths.get(source.getParent()+"/r_" + source.getFileName());
//            if (!Files.exists(dest))
//                Files.createFile(dest);
//            Files.write(dest, sourceLines);
//            return dest;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
