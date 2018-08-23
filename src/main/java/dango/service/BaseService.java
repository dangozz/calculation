package dango.service;

import java.util.List;
import java.util.Map;

/**
 * @author: DANGO
 * @date 2018/7/314:38
 * @Description:
 */
public interface BaseService<T> {
    void setBaseDao();
    List<T> findAllModel();
    List<T> findModelByCondition(Map<String,String> map);
    T findOneModel(Map<String,String> map);
    int insertModel(T t);
    int deleteModel(T t);
    int updateModel(T t);
}
