package cn.org.awcp.formdesigner.utils;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import cn.org.awcp.extend.formdesigner.DocumentUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.org.awcp.formdesigner.application.vo.DocumentVO;
import cn.org.awcp.formdesigner.application.vo.DynamicPageVO;
import cn.org.awcp.formdesigner.engine.util.MyScriptEngine;
import cn.org.awcp.venson.controller.base.ControllerContext;

/**
 * @author lenovo
 *
 */
public class ScriptEngineUtils {
	/**
	 * 日志对象
	 */
	private static final Log logger = LogFactory.getLog(ScriptEngineUtils.class);
	private static ScriptEngineManager manager = new ScriptEngineManager();
	private static final String ENGINE_NAME = "JavaScript";

	/**
	 * 
	 * @return
	 */
	public static ScriptEngine getScriptEngine(DocumentVO docVO, DynamicPageVO pageVO) {
		ScriptEngine engine;
		String engineName;
		if (pageVO == null || docVO == null) {
			engine = manager.getEngineByName(ENGINE_NAME);
			engineName = engine.getClass().getSimpleName();
		} else {
			ControllerContext.setDoc(docVO);
			ControllerContext.setPage(pageVO);
			ScriptEngine e = manager.getEngineByName(ENGINE_NAME);
			engineName = e.getClass().getSimpleName();
			engine = new MyScriptEngine(e, pageVO.getSystemId());
		}
		engine.put("DocumentUtils", DocumentUtils.getIntance());
		if ("NashornScriptEngine".equals(engineName)) {
			logger.debug("Nashorn");
			initNashornScriptEngine(engine);
		} else {
			logger.debug("Rhino");
			initRhinoScriptEngine(engine);
		}
		return engine;
	}

	public static ScriptEngine getScriptEngine() {
		return getScriptEngine(null, null);
	}

	private static void initNashornScriptEngine(ScriptEngine engine) {
		try {
			engine.eval("var UUID = Java.type('java.util.UUID');");
		} catch (ScriptException e) {
			e.printStackTrace();
		}
	}

	private static void initRhinoScriptEngine(ScriptEngine engine) {
		try {
			String expressionImport = "importPackage(java.util);";
			engine.eval(expressionImport);
		} catch (ScriptException e) {
			logger.info("ERROR", e);
		}
	}

}
