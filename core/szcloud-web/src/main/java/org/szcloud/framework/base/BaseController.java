package org.szcloud.framework.base;

import java.beans.PropertyEditorSupport;
import java.util.Date;

import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.szcloud.framework.core.utils.DateUtils;

/**
 * 控制器支持类
 * 
 * @author ThinkGem
 * @version 2013-3-23
 */
public abstract class BaseController {

	/**
	 * 日志对象
	 */
	protected Logger logger = LoggerFactory.getLogger(getClass());

	// protected Validator getValidator(){
	// return null;
	// }

	protected ThreadLocal<BindingResult> result = new ThreadLocal<BindingResult>();

	// 将Map<String,String[]>转换成Map<String, String>

	// protected BindingResult getErrors(Object target, String objectName){
	// if(result.get() == null || !result.get().getTarget().equals(target) ||
	// !result.get().getObjectName().equals(objectName)){
	// result.set(new BeanPropertyBindingResult(target, objectName));
	// }
	// return result.get();
	// }
	//
	//
	// /**
	// * 服务端参数有效性验证
	// * @param object 验证的实体对象
	// * @param groups 验证组
	// * @return 验证成功：返回true；严重失败：将错误信息添加到 message 中
	// */
	// protected boolean beanValidator(Model model, Object object, Class<?>...
	// groups) {
	// Validator validator = getValidator();
	// BindingResult errors = getErrors(object, "object");
	// validator.validate(object, errors);
	// if(errors.hasErrors()){
	// List<String> rtn = new LinkedList<String>();
	// List<FieldError> list = errors.getFieldErrors();
	// for(int i = 0; i < list.size(); i++){
	// FieldError e = list.get(i);
	// rtn.add(e.getDefaultMessage());
	// }
	// addMessage(model, list.toArray(new String[]{}));
	// return false;
	// }else{
	// return true;
	// }
	// }
	//
	// /**
	// * 服务端参数有效性验证
	// * @param object 验证的实体对象
	// * @param groups 验证组
	// * @return 验证成功：返回true；严重失败：将错误信息添加到 flash message 中
	// */
	// protected boolean beanValidator(RedirectAttributes redirectAttributes,
	// Object object, Class<?>... groups) {
	// Validator validator = getValidator();
	// BindingResult errors = getErrors(object, "object");
	// validator.validate(object, errors);
	// if(errors.hasErrors()){
	// List<String> rtn = new LinkedList<String>();
	// List<FieldError> list = errors.getFieldErrors();
	// for(int i = 0; i < list.size(); i++){
	// FieldError e = list.get(i);
	// rtn.add(e.getDefaultMessage());
	// }
	// addMessage(redirectAttributes, list.toArray(new String[]{}));
	// return false;
	// }else{
	// return true;
	// }
	// }

	/**
	 * 添加Model消息
	 * 
	 * @param messages
	 *            消息
	 */
	protected void addMessage(ModelAndView mv, String... messages) {
		StringBuilder sb = new StringBuilder();
		for (String message : messages) {
			sb.append(message).append(messages.length > 1 ? "<br/>" : "");
		}
		mv.addObject("message", sb.toString());
	}

	/**
	 * 添加Model消息
	 * 
	 * @param messages
	 *            消息
	 */
	protected void addMessage(Model model, String... messages) {
		StringBuilder sb = new StringBuilder();
		for (String message : messages) {
			sb.append(message).append(messages.length > 1 ? "<br/>" : "");
		}
		model.addAttribute("message", sb.toString());
	}

	/**
	 * 添加Flash消息
	 * 
	 * @param messages
	 *            消息
	 */
	protected void addMessage(RedirectAttributes redirectAttributes, String... messages) {
		StringBuilder sb = new StringBuilder();
		for (String message : messages) {
			sb.append(message).append(messages.length > 1 ? "<br/>" : "");
		}
		redirectAttributes.addFlashAttribute("message", sb.toString());
	}

	/**
	 * 初始化数据绑定 1. 将所有传递进来的String进行HTML编码，防止XSS攻击 2. 将字段中Date类型转换为String类型
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		// String类型转换，将所有传递进来的String进行HTML编码，防止XSS攻击
		binder.registerCustomEditor(String.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String text) {
				setValue(text == null ? null : StringEscapeUtils.escapeHtml4(text.trim()));
			}

			@Override
			public String getAsText() {
				Object value = getValue();
				return value != null ? value.toString() : "";
			}
		});
		// Date 类型转换
		binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String text) {
				setValue(DateUtils.parseDate(text));
			}
		});
	}

	// protected Locale getLocale(HttpServletRequest request){
	// return localeResolver.resolveLocale(request);
	// }
	//
	//
	// protected String getThemeName(HttpServletRequest request){
	// return themeResolver.resolveThemeName(request);
	// }
	//
	// protected String getMessage(String themeName, String code, Locale
	// locale){
	// return
	// themeSource.getTheme(themeName).getMessageSource().getMessage(code, null,
	// locale);
	// }

	// protected String getMessage(String code){
	// return
	// themeSource.getTheme(getThemeName(request)).getMessageSource().getMessage(code,
	// null, getLocale(request));
	// }

	// code by venson

}
