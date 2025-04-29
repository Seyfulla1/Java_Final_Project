package az.edu.bhos.finalProject.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Json {
    private static ObjectMapper mapper=getDefaultObjectMapper();

    private static ObjectMapper getDefaultObjectMapper(){
        mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule());
        return mapper;
    }
    public static <T> List<T> readJsonFile(String filePath, TypeReference<List<T>> typeRef) throws IOException {
        File file = new File(filePath);
        if(!file.exists() || file.length() == 0) {
            return new ArrayList<>();
        }
        return mapper.readValue(file,typeRef);
    }

    public static <T> void writeJsonFile(String filePath, List<T> data) throws IOException {
        File file = new File(filePath);
        mapper.writerWithDefaultPrettyPrinter().writeValue(file, data);
    }
}
