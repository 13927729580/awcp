package org.szcloud.framework.formdesigner.utils;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.szcloud.framework.formdesigner.application.vo.DocumentVO;
import org.szcloud.framework.formdesigner.application.vo.DynamicPageVO;
import org.szcloud.framework.formdesigner.engine.util.MyScriptEngine;

/**
 * @author lenovo
 *
 */
public class ScriptEngineUtils {
	/**
	 * 日志对象
	 */
	private static final Logger logger = LoggerFactory.getLogger(ScriptEngineUtils.class);
	private static ScriptEngineManager manager = null;

	private static ScriptEngineManager getScriptEngineManger() {
		if (manager == null)
			manager = new ScriptEngineManager();
		return manager;
	}

	/**
	 * 获取脚本引擎 with java.util
	 * 
	 * @param docVo
	 * @param pageVo
	 * @return
	 */
	public static ScriptEngine getScriptEngine(DocumentVO docVo, DynamicPageVO pageVo) {
		// 看下缓存中是否存在，如果有则直接返回，否则重新填充,键应该和docVo,pageVo有关

		ScriptEngine engine = new MyScriptEngine(getScriptEngineManger().getEngineByName("javascript"),
				pageVo.getSystemId());
		// 初始化工具，放入到engine中
		// engine.put("DocumentUtils", new DocumentUtils(docVo, pageVo));
		DocumentUtils utils = new DocumentUtils(docVo, pageVo);
		engine.put("DocumentUtils", utils);

		try {
			String expressionImport = "importPackage(java.util);";
			engine.eval(expressionImport);

		} catch (ScriptException e) {
			e.printStackTrace();
			return null;
		}
		return engine;
	}

	/**
	 * 无法使用函数库 with no java.util
	 * 
	 * @return
	 */
	public static ScriptEngine getScriptEngine() {
		// ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = getScriptEngineManger().getEngineByName("JavaScript");
		engine.put("DocumentUtils", new DocumentUtils());
		return engine;
	}

	public static void main(String args[]) throws ScriptException {

		ScriptEngine engine = getScriptEngine();
		String js = "var map = {};map.techBase=\"techBase\";var pc=map.pc;println(pc);if((!typeof(pc)==\"undefined\")&& pc != ''){println('1');} else{println(2);}";
		Object rtn = engine.eval(js);
		logger.debug("ok");
		// logger.debug(rtn.toString());

		// ScriptEngine engine = getScriptEngine();
		// String js = "test(1,2);";
		// String func = "function test(a,b){return a+b}";
		// engine.eval(func);
		//
		// Object result = engine.eval(js);
		// logger.debug(result.toString());

		// DocumentVO docVO = new DocumentVO();
		// DynamicPageVO pageVO = new DynamicPageVO();
		// pageVO.setSystemId(87L);
		//
		// ScriptEngine engine = getScriptEngine(docVO, pageVO);
		// String js = "#include \"test1\" \n test(1,2);";
		//// String func = "function test(a,b){return a+b}";
		//// Compilable compilable = (Compilable) engine;
		//// CompiledScript JSFunction = compilable.compile(func); //解析编译脚本函数
		//// JSFunction.eval(engine.getBindings(ScriptContext.GLOBAL_SCOPE));
		// String script = "\"select * from emp\";";
		// Object result = engine.eval(script);
		// logger.debug(result.toString());

		// ScriptEngine engine2 = getScriptEngine();
		// js = "test(7,9)";
		// result =
		// engine2.eval(js,engine.getBindings(ScriptContext.GLOBAL_SCOPE));
		// logger.debug(result.toString());

		/*
		 * ScriptEngine engine = ScriptEngineUtils.getScriptEngine(null, null);
		 * try { String script="var map=new java.util.HashMap(); " +
		 * "map.put(\"dynamicPageId\",\"aldskaldf\");" +
		 * "map.put(\"recordId\",\"12346789\");" + "map;";
		 * 
		 * String script ="var list  = "; Map map = (Map)engine.eval(script);
		 * logger.debug(map.get("dynamicPageId")); } catch (ScriptException e) {
		 * e.printStackTrace(); }
		 */

		/*
		 * String script="{\"componentType\":\"1012\",\"dataItemCode\":" +
		 * "\"audit.shenpiyijian\",\"dynamicPageId\":90,\"relateId\":" +
		 * "\"xxxx\",\"layoutId\":\"22f620a6-df3f-4cbd-be3c-d1b7056a9c5a\"," +
		 * "\"layoutName\":\"layout_row3_cod2\",\"name\":" +
		 * "\"quickData1419669928982\",\"order\":2,\"pageId\":" +
		 * "\"2c2a2dfc-4580-4bf7-a475-50c3c29db2dc\",\"configures\":" +
		 * "[{\"pageId\":\"xxxx\",\"dataItemCode\":\"xxx\",\"container.name\":\"xxx\"}]}";
		 * 
		 * String script1 = "getUser();"; ScriptEngine engine
		 * =getScriptEngine(null, null); String ret; try { ret = (String)
		 * engine.eval(script1); logger.debug(ret); } catch (ScriptException e)
		 * { // TODO Auto-generated catch block e.printStackTrace(); }
		 */
		// String test = "pmi1MEMBER_NAME";
		// String[] ret = test.split("\\.");
		// logger.debug(ret.length);
		// logger.debug(ret[0]);

	}

	private static String importJavaPackages(String script) {
		String importScript = "importPackage(java.util);";
		return importScript + script;
	}

	public static String getUser() {
		return "宋和贵";
	}
}
