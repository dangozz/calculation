package dango.service.impl;

import dango.dao.BaseDao;
import dango.service.BaseService;

import java.util.List;
import java.util.Map;

/**
 * @author: DANGO
 * @date 2018/7/314:40
 * @Description:
 */
public abstract class BaseServiceImpl<T> implements BaseService<T> {
    private BaseDao<T> baseDao;

    public void setBaseDao(BaseDao<T> baseDao) {
        this.baseDao=baseDao;
    }

    @Override
    public List<T> findAllModel() {
        return baseDao.findAllModel();
    }

    @Override
    public List<T> findModelByCondition(Map<String, String> map) {
        return baseDao.findModelByCondition(map);
    }

    @Override
    public T findOneModel(Map<String, String> map) {
        return baseDao.findOneModel(map);
    }

    @Override
    public int insertModel(T t) {
        return baseDao.insertModel(t);
    }

    @Override
    public int deleteModel(T t) {
        return baseDao.deleteModel(t);
    }

    @Override
    public int updateModel(T t) {
        return baseDao.updateModel(t);
    }
}
