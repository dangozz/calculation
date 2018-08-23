package dango.dao;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author: DANGO
 * @date 2018/7/314:36
 * @Description:
 */
public interface BaseDao<T> {
    List<T> findAllModel();
    List<T> findModelByCondition(Map<String,String> map);
    T findOneModel(Map<String,String> map);
    int insertModel(@Param("t")T t);
    int deleteModel(@Param("t")T t);
    int updateModel(@Param("t")T t);
}
