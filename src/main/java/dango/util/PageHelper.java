package dango.util;

import com.github.pagehelper.*;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author: DANGO
 * @date 2018/7/27 10:52
 * @Description:
 */
public class PageHelper implements Interceptor {
    private SqlUtil sqlUtil;
    private Properties properties;
    private SqlUtilConfig sqlUtilConfig;
    private boolean autoDialect = true;
    private boolean autoRuntimeDialect;
    private boolean closeConn = true;
    private Map<String, SqlUtil> urlSqlUtilMap = new ConcurrentHashMap();

    public PageHelper() {
    }

    public static long count(ISelect select) {
        Page<?> page = startPage(1, -1, true);
        select.doSelect();
        return page.getTotal();
    }

    public static <E> Page<E> startPage(int pageNum, int pageSize) {
        return startPage(pageNum, pageSize, true);
    }

    public static <E> Page<E> startPage(int pageNum, int pageSize, boolean count) {
        return startPage(pageNum, pageSize, count, (Boolean)null);
    }

    public static <E> Page<E> startPage(int pageNum, int pageSize, String orderBy) {
        Page<E> page = startPage(pageNum, pageSize);
        page.setOrderBy(orderBy);
        return page;
    }

    public static <E> Page<E> offsetPage(int offset, int limit) {
        return offsetPage(offset, limit, true);
    }

    public static <E> Page<E> offsetPage(int offset, int limit, boolean count) {
        Page<E> page = new Page(new int[]{offset, limit}, count);
        Page<E> oldPage = SqlUtil.getLocalPage();
        if (oldPage != null && oldPage.isOrderByOnly()) {
            page.setOrderBy(oldPage.getOrderBy());
        }

        SqlUtil.setLocalPage(page);
        return page;
    }

    public static <E> Page<E> offsetPage(int offset, int limit, String orderBy) {
        Page<E> page = offsetPage(offset, limit);
        page.setOrderBy(orderBy);
        return page;
    }

    public static <E> Page<E> startPage(int pageNum, int pageSize, boolean count, Boolean reasonable) {
        return startPage(pageNum, pageSize, count, reasonable, (Boolean)null);
    }

    public static <E> Page<E> startPage(int pageNum, int pageSize, boolean count, Boolean reasonable, Boolean pageSizeZero) {
        Page<E> page = new Page(pageNum, pageSize, count);
        page.setReasonable(reasonable);
        page.setPageSizeZero(pageSizeZero);
        Page<E> oldPage = SqlUtil.getLocalPage();
        if (oldPage != null && oldPage.isOrderByOnly()) {
            page.setOrderBy(oldPage.getOrderBy());
        }

        SqlUtil.setLocalPage(page);
        return page;
    }

    public static <E> Page<E> startPage(Object params) {
        Page<E> page = SqlUtil.getPageFromObject(params);
        Page<E> oldPage = SqlUtil.getLocalPage();
        if (oldPage != null && oldPage.isOrderByOnly()) {
            page.setOrderBy(oldPage.getOrderBy());
        }

        SqlUtil.setLocalPage(page);
        return page;
    }

    public static void orderBy(String orderBy) {
        Page<?> page = SqlUtil.getLocalPage();
        if (page != null) {
            page.setOrderBy(orderBy);
        } else {
            page = new Page();
            page.setOrderBy(orderBy);
            page.setOrderByOnly(true);
            SqlUtil.setLocalPage(page);
        }

    }

    public static String getOrderBy() {
        Page<?> page = SqlUtil.getLocalPage();
        if (page != null) {
            String orderBy = page.getOrderBy();
            return StringUtil.isEmpty(orderBy) ? null : orderBy;
        } else {
            return null;
        }
    }

    public Object intercept(Invocation invocation) throws Throwable {
        if (this.autoRuntimeDialect) {
            SqlUtil sqlUtil = this.getSqlUtil(invocation);
            return sqlUtil.processPage(invocation);
        } else {
            if (this.autoDialect) {
                this.initSqlUtil(invocation);
            }

            return this.sqlUtil.processPage(invocation);
        }
    }

    public synchronized void initSqlUtil(Invocation invocation) {
        if (this.sqlUtil == null) {
            this.sqlUtil = this.getSqlUtil(invocation);
            if (!this.autoRuntimeDialect) {
                this.properties = null;
                this.sqlUtilConfig = null;
            }

            this.autoDialect = false;
        }

    }

    public String getUrl(DataSource dataSource) {
        Connection conn = null;

        String var3;
        try {
            conn = dataSource.getConnection();
            var3 = conn.getMetaData().getURL();
        } catch (SQLException var12) {
            throw new RuntimeException(var12);
        } finally {
            if (conn != null) {
                try {
                    if (this.closeConn) {
                        conn.close();
                    }
                } catch (SQLException var11) {
                    ;
                }
            }

        }

        return var3;
    }

    public SqlUtil getSqlUtil(Invocation invocation) {
        MappedStatement ms = (MappedStatement)invocation.getArgs()[0];
        DataSource dataSource = ms.getConfiguration().getEnvironment().getDataSource();
        String url = this.getUrl(dataSource);
        if (this.urlSqlUtilMap.containsKey(url)) {
            return (SqlUtil)this.urlSqlUtilMap.get(url);
        } else {
            ReentrantLock lock = new ReentrantLock();

            SqlUtil var6;
            try {
                lock.lock();
                if (!this.urlSqlUtilMap.containsKey(url)) {
                    if (StringUtil.isEmpty(url)) {
                        throw new RuntimeException("无法自动获取jdbcUrl，请在分页插件中配置dialect参数!");
                    }

                    String dialect = Dialect.fromJdbcUrl(url);
                    if (dialect == null) {
                        throw new RuntimeException("无法自动获取数据库类型，请通过dialect参数指定!");
                    }

                    SqlUtil sqlUtil = new SqlUtil(dialect);
                    if (this.properties != null) {
                        sqlUtil.setProperties(this.properties);
                    } else if (this.sqlUtilConfig != null) {
                        sqlUtil.setSqlUtilConfig(this.sqlUtilConfig);
                    }

                    this.urlSqlUtilMap.put(url, sqlUtil);
                    SqlUtil var8 = sqlUtil;
                    return var8;
                }

                var6 = (SqlUtil)this.urlSqlUtilMap.get(url);
            } finally {
                lock.unlock();
            }

            return var6;
        }
    }

    public Object plugin(Object target) {
        return target instanceof Executor ? Plugin.wrap(target, this) : target;
    }

    private void checkVersion() {
        try {
            Class.forName("org.apache.ibatis.scripting.xmltags.SqlNode");
        } catch (ClassNotFoundException var2) {
            throw new RuntimeException("您使用的MyBatis版本太低，MyBatis分页插件PageHelper支持MyBatis3.2.0及以上版本!");
        }
    }

    public void setProperties(Properties p) {
        this.checkVersion();
        String closeConn = p.getProperty("closeConn");
        if (StringUtil.isNotEmpty(closeConn)) {
            this.closeConn = Boolean.parseBoolean(closeConn);
        }

        String dialect = p.getProperty("dialect");
        String runtimeDialect = p.getProperty("autoRuntimeDialect");
        if (StringUtil.isNotEmpty(runtimeDialect) && runtimeDialect.equalsIgnoreCase("TRUE")) {
            this.autoRuntimeDialect = true;
            this.autoDialect = false;
            this.properties = p;
        } else if (StringUtil.isEmpty(dialect)) {
            this.autoDialect = true;
            this.properties = p;
        } else {
            this.autoDialect = false;
            this.sqlUtil = new SqlUtil(dialect);
            this.sqlUtil.setProperties(p);
        }

    }

    public void setSqlUtilConfig(SqlUtilConfig config) {
        this.checkVersion();
        this.closeConn = config.isCloseConn();
        if (config.isAutoRuntimeDialect()) {
            this.autoRuntimeDialect = true;
            this.autoDialect = false;
            this.sqlUtilConfig = config;
        } else if (StringUtil.isEmpty(config.getDialect())) {
            this.autoDialect = true;
            this.sqlUtilConfig = config;
        } else {
            this.autoDialect = false;
            this.sqlUtil = new SqlUtil(config.getDialect());
            this.sqlUtil.setSqlUtilConfig(config);
        }

    }
}

