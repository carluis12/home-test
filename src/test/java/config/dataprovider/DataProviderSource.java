package config.dataprovider;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import dataDto.BaseTestData;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Class that provides a DataProvider for tests based on a JSON file.
 *
 * @param <T> Type of the data to be provided.
 */
@Slf4j
@Data
public abstract class DataProviderSource<T extends BaseTestData> {

    protected String fileName;
    private String testDataPath = "/testdata/";
    private String path;


    public DataProviderSource() {
        try {
            URL resource = this.getClass().getResource(testDataPath);
            assert resource != null;
            this.path = Paths.get(resource.toURI()).toFile().getAbsolutePath();
        } catch (Exception e) {
            log.error("Error while trying to get local resources.", e);
        }
    }

    protected abstract TypeReference<List<T>> getTypeReference();

    /**
     * Method that loads data from the JSON file.
     *
     * @return List of objects of type T.
     */
    protected List<T> loadData() {
        return getFile();
    }

    /**
     * Method that obtains an iterator of the loaded data.
     *
     * @return Iterator of the loaded data.
     */
    private List<T> getFile() {
        String absolutePath = String.format("%s/%s", path, this.fileName);
        try {
            String dataFile = Files.readString(Paths.get(absolutePath));
            return convertFromJson(dataFile, getTypeReference());
        } catch (IOException e) {
            throw new RuntimeException("Error reading JSON file: " + absolutePath, e);
        }
    }

    /**
     * Method that converts a JSON file to a list of objects of type T.
     *
     * @param json String representing the JSON file.
     * @param type Reference type for conversion.
     * @return List of objects of type T.
     */
    private List<T> convertFromJson(String json, TypeReference<List<T>> type) {
        try {
            ObjectMapper mapper = getMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return mapper.readValue(json, type);
        } catch (Exception e) {
            log.error("Error when mapping the data", e);
            throw new RuntimeException("Error when mapping the data", e);
        }
    }

    /**
     * Method that gets an ObjectMapper to map the data.
     *
     * @return ObjectMapper.
     */
    private ObjectMapper getMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new Jdk8Module());
        return mapper;
    }
}