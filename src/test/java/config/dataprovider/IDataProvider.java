package config.dataprovider;

import dataDto.BaseTestData;

import java.lang.reflect.Method;
import java.util.Iterator;

/**
 * Interface that provides a DataProvider for tests based on a JSON file.
 *
 * @param <T> Type of the data to be provided.
 */
public interface IDataProvider<T extends BaseTestData> {

    Iterator<T[]> data(Method m, String dataJsonName);

    Iterator<T[]> getIterator(Method m);

}

