package config.dataprovider;

import com.fasterxml.jackson.core.type.TypeReference;
import dataDto.TestData;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.DataProvider;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Class that provides a DataProvider for tests based on a JSON file.
 */
@EqualsAndHashCode(callSuper = true)
@Slf4j
@Data
public class TestsDataProvider extends DataProviderSource<TestData> implements
        IDataProvider<TestData> {


    private String dataJsonName;

    @DataProvider(name = "TEST_DATA")
    public Iterator<TestData[]> data(Method m) {
        return data(m, "data.json");
    }


    @Override
    protected TypeReference<List<TestData>> getTypeReference() {
        return new TypeReference<>() {
        };
    }

    /**
     * Method that loads the data from the JSON file.
     *
     * @return List of objects of type TestData.
     */
    private List<TestData> loadDataProvider() {
        setFileName(dataJsonName);
        return loadData();
    }

    /**
     * Method that obtains an iterator of the loaded data.
     *
     * @return Iterator of the loaded data.
     */
    @Override
    public Iterator<TestData[]> data(Method m, String dataJsonName) {
        setDataJsonName(dataJsonName);
        return getIterator(m);
    }

    /**
     * Method that obtains an iterator of the loaded data.
     *
     * @return Iterator of the loaded data.
     */
    @Override
    public Iterator<TestData[]> getIterator(Method m) {
        Collection<TestData> data = loadDataProvider();
        List<TestData[]> ret = new ArrayList<>();
        data.forEach(ds -> {
            if (ds.getTestName().equals(m.getName())) {
                ret.add(new TestData[]{ds});
            }
        });
        return ret.iterator();
    }
}
