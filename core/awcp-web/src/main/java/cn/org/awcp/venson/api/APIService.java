package cn.org.awcp.venson.api;

import cn.org.awcp.core.domain.SzcloudJdbcTemplate;
import cn.org.awcp.core.utils.Cache;
import cn.org.awcp.core.utils.MySqlSmartCountUtil;
import cn.org.awcp.core.utils.SessionUtils;
import cn.org.awcp.formdesigner.utils.ScriptEngineUtils;
import cn.org.awcp.metadesigner.application.MetaModelOperateService;
import cn.org.awcp.venson.controller.base.ControllerContext;
import cn.org.awcp.venson.controller.base.ControllerHelper;
import cn.org.awcp.venson.controller.base.ReturnResult;
import cn.org.awcp.venson.controller.base.StatusCode;
import cn.org.awcp.venson.interceptor.ExceptionHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API服务类
 *
 * @author venson
 * @version 20170404
 */
@Component("apiService")
public class APIService {

    private final static String ZERO = "0";
    private final static String SUCCESS = "success";
    private final static String METHOD_BOTH = "both";
    /**
     * 日志对象
     */
    protected final Log logger = LogFactory.getLog(getClass());


    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Autowired
    private SzcloudJdbcTemplate jdbcTemplate;


    @Resource(name = "metaModelOperateServiceImpl")
    private MetaModelOperateService meta;
    @Autowired
    private Cache cache;

    public void execute(ReturnResult result, PFMAPI api, Map<String, Object> params) {
        result.setStatus(StatusCode.SUCCESS);
        // 是否缓存
        String key = api.getAPIName() + "_" + params.toString();
        if (api.isCache()) {
            Object value = cache.get(key);
            if (value != null) {
                result.setData(value);
                return;
            }
        }
        switch (APIType.valueOf(api.getAPIType())) {
            case ADD:
                executeAdd(params, result, api);
                break;
            case UPDATE:
                executeUpdate(params, result, api);
                break;
            case GET:
                executeGet(params.get(api.getPrimaryKey()), result, api);
                break;
            case DELETE:
                executeDelete(params.get(api.getPrimaryKey()), api);
                break;
            case EXECUTE:
                executeSQL(result, api, params);
                break;
            case QUERY:
                executeQuery(result, api, params);
                break;
            case PAGE:
                executePage(result, api, params);
                break;
            case EXECUTE_SCRIPT:
                executeScript(result, api);
                break;
            default:
                break;
        }
        // 置入缓存
        if (api.isCache()) {
            cache.put(key, result.getData());
        }
    }

    /**
     * 执行脚本
     *
     * @param result 返回结果
     * @param api    api实体类
     */
    public void executeScript(ReturnResult result, PFMAPI api) {
        // 获取参数值
        String script = api.getAPISQL();
        ScriptEngine engine = getEngine();
        if (StringUtils.isNotBlank(script)) {
            jdbcTemplate.beginTranstaion();
            try {
                Object ret = engine.eval(script);
                jdbcTemplate.commit();
                result.setData(ret);
            } catch (Exception e) {
                ExceptionHandler.handler(e, result);
                jdbcTemplate.rollback();
            }
        }
    }

    /**
     * 执行分页查询
     *
     * @param result 返回结果
     * @param api    api实体类
     * @param params 请求参数
     */
    public void executePage(ReturnResult result, PFMAPI api, Map<String, Object> params) {
        String ps = (String) params.get("pageSize");
        String cp = (String) params.get("currentPage");
        int pageSize = Integer.parseInt(StringUtils.isNumeric(ps) ? ps : "5");
        int currentPage = Integer.parseInt(StringUtils.isNumeric(cp) ? cp : "1");
        String script = api.getAPISQL();
        ScriptEngine engine = getEngine();
        String sql = getSQL(result, script, engine);
        if (sql != null) {
            // 执行分页语句
            List<Map<String, Object>> data = namedParameterJdbcTemplate
                    .queryForList(sql + " limit " + (currentPage - 1) * pageSize + "," + pageSize, params);
            if (data.isEmpty()) {
                result.setData(data).setTotal(0);
                return;
            }
            // 获取总数
            Object count = namedParameterJdbcTemplate.queryForObject(MySqlSmartCountUtil.getCountSql(sql, true), params, Object.class);
            result.setData(data).setTotal(count);
        }
    }

    /**
     * 执行语句
     *
     * @param result 返回结果
     * @param api    api实体类
     * @param params 请求参数
     */
    public void executeSQL(ReturnResult result, PFMAPI api, Map<String, Object> params) {
        String script = api.getAPISQL();
        ScriptEngine engine = getEngine();
        String sql = getSQL(result, script, engine);
        if (sql != null) {
            // 执行语句
            int data = namedParameterJdbcTemplate.update(sql, params);
            result.setData(data);
        }
    }

    public ScriptEngine getEngine() {
        ScriptEngine engine = ScriptEngineUtils.getScriptEngine();
        HttpServletRequest request = ControllerContext.getRequest();
        engine.put("request", request);
        engine.put("session", SessionUtils.getCurrentSession());

        return engine;
    }

    /**
     * 执行查询
     *
     * @param result 返回结果
     * @param api    api实体类
     * @param params 请求参数
     */
    public void executeQuery(ReturnResult result, PFMAPI api, Map<String, Object> params) {
        String script = api.getAPISQL();
        ScriptEngine engine = getEngine();
        String sql = getSQL(result, script, engine);
        if (sql != null) {
            // 执行语句
            List<Map<String, Object>> data = namedParameterJdbcTemplate.queryForList(sql, params);
            result.setData(data);
        }
    }

    private String getSQL(ReturnResult result, String script, ScriptEngine engine) {
        String sql = null;
        if (StringUtils.isNotBlank(script)) {
            try {
                sql = (String) engine.eval(script);
            } catch (ScriptException e) {
                result.setStatus(StatusCode.FAIL).setMessage(e.toString());
                logger.info("ERROR", e);
            }
        }
        return sql;
    }

    /**
     * 执行删除
     *
     * @param id  数据id
     * @param api api实体类
     */
    public void executeDelete(Object id, PFMAPI api) {
        // 删除记录
        meta.delete(id, api.getAPITable());
    }

    /**
     * 执行获取
     *
     * @param id     数据id
     * @param result 返回结果
     * @param api    api实体类
     */
    public void executeGet(Object id, ReturnResult result, PFMAPI api) {
        // 根据获取记录
        Map<String, Object> data = meta.get(id, api.getAPITable());
        result.setData(data);
    }

    /**
     * 执行增加
     *
     * @param params 请求参数
     * @param result 返回结果
     * @param api    api实体类
     */
    public void executeUpdate(Map<String, Object> params, ReturnResult result, PFMAPI api) {
        List<TableDesc> fields = api.getAPITableFields();
        Map<String, String> map = new HashMap<>(fields.size());
        // 获取参数值
        for (TableDesc field : fields) {
            String value = String.valueOf(params.get(field.getField()));
            if (StringUtils.isNotBlank(value)) {
                map.put(field.getField(), value);
            }
        }
        // 更新
        try {
            meta.update(map, api.getAPITable());
        } catch (Exception e) {
            result.setStatus(StatusCode.FAIL).setMessage(e.toString());
            logger.info("ERROR", e);
        }
    }

    /**
     * 执行增加
     *
     * @param params 请求参数
     * @param result 返回结果
     * @param api    api实体类
     */
    public void executeAdd(Map<String, Object> params, ReturnResult result, PFMAPI api) {
        List<TableDesc> fields = api.getAPITableFields();
        Map<String, String> map = new HashMap<>(fields.size());
        for (TableDesc field : fields) {
            // 置入主键
            field.setDeafultValue(map);
            // 置入共同的值
            field.setPrimaryValue(map);
            String value = String.valueOf(params.get(field.getField()));
            // 获取参数值
            if (StringUtils.isNotBlank(value)) {
                map.put(field.getField(), value);
            }
        }
        // 保存
        try {
            meta.save(map, api.getAPITable());
        } catch (Exception e) {
            result.setStatus(StatusCode.FAIL).setMessage(e.toString());
            logger.info("ERROR", e);
        }
    }

    public boolean validateAPI(ReturnResult result, PFMAPI api, String method,Map<String, Object> params) {
        if (api == null) {
            result.setStatus(StatusCode.FAIL).setMessage("接口未找到，无效接口！");
            return false;
        }
        // 是否已禁用
        if (api.getAPIState() == 0) {
            result.setStatus(StatusCode.FAIL).setMessage("接口已禁用！");
            return false;
        }
        // 是否需要登录校验
        if (api.getAPIIsLogin() == 1) {
            if (ControllerHelper.getUser() == null) {
                result.setStatus(StatusCode.NO_LOGIN).setMessage("您还未登录，请先登录！");
                return false;
            }
        }
        // 判断请求方式
        if (!METHOD_BOTH.equals(api.getAPIMethod()) && !method.equals(api.getAPIMethod())) {
            result.setStatus(StatusCode.NO_ACCESS).setMessage("请使用" + api.getAPIMethod() + "请求");
            return false;
        }
        List<APIParameter> parameters = api.getParameters();
        if(parameters!=null&&!parameters.isEmpty()){
            for (APIParameter parameter : parameters) {
                String name = parameter.getName();
                Object value = params.get(name);
                //参数为空设置默认值
                if(value==null&&StringUtils.isNotBlank(parameter.getDefaultValue())&&!parameter.getDefaultValue().equals("无")){
                    value=parameter.getDefaultValue();
                    params.put(name,value);
                }
                //校验参数是否必填
                if(parameter.isRequires()&&value==null){
                    result.setStatus(StatusCode.PARAMETER_ERROR).setMessage("参数["+name+"]不能为空!");
                    return false;
                }
                //校验参数类型
                if(value!=null){
                    String type=parameter.getType();
                    String val=value.toString();
                    //判断是否是数值类型参数
                    //判断是否是布尔值类型参数
                    if("number".equals(type)&&!StringUtils.isNumeric(val)){
                        result.setStatus(StatusCode.PARAMETER_ERROR).setMessage("参数["+name+"]值["+val+"]必须为数值类型!");
                        return false;
                    }else if("boolean".equals(type)&&!"TRUE".equalsIgnoreCase(val) && !"FALSE".equalsIgnoreCase(val)){
                        result.setStatus(StatusCode.PARAMETER_ERROR).setMessage("参数["+name+"]值["+val+"]必须为布尔类型!");
                        return false;
                    }
                }

            }
        }
        List<APIRule> rules = api.getRules();
        // 接口权限认证
        if (rules != null && !rules.isEmpty()) {
            for (APIRule rule : rules) {
                if (!checkRule(result, rule)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 权限校验
     *
     * @param result 返回结果
     * @param rule   校验规则
     * @return boolean
     */
    private boolean checkRule(ReturnResult result, APIRule rule) {
        // 0为脚本规则
        if (ZERO.equals(rule.getType())) {
            ScriptEngine engine = getEngine();
            try {
                Object ret = engine.eval(rule.getRules());
                // 返回值为false则校验不通过
                if (!SUCCESS.equals(ret)) {
                    result.setStatus(StatusCode.FAIL).setMessage(String.valueOf(ret));
                    return false;
                }
            } catch (ScriptException e) {
                result.setStatus(StatusCode.NO_ACCESS).setMessage(e.toString());
                return false;
            }
        }
        return true;
    }
}
