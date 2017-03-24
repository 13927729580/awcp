package org.szcloud.framework.workflow.core.module;

public class RegistContant {
	final int REG_OPTION_NON_VOLATILE = 0; // 当系统重新启动时，关键字被保留
	
	// 注册表关键字安全选项...
	final int READ_CONTROL = 0x20000;
	final int KEY_QUERY_VALUE = 0x1;
	final int KEY_SET_VALUE = 0x2;
	final int KEY_CREATE_SUB_KEY = 0x4;
	final int KEY_ENUMERATE_SUB_KEYS = 0x8;
	final int KEY_NOTIFY = 0x10;
	final int KEY_CREATE_LINK = 0x20;
	final float KEY_READ = KEY_QUERY_VALUE + KEY_ENUMERATE_SUB_KEYS + KEY_NOTIFY + READ_CONTROL;
	final float KEY_WRITE = KEY_SET_VALUE + KEY_CREATE_SUB_KEY + READ_CONTROL;
	final Object KEY_EXECUTE = KEY_READ;
	final double KEY_ALL_ACCESS = KEY_QUERY_VALUE + KEY_SET_VALUE + KEY_CREATE_SUB_KEY + KEY_ENUMERATE_SUB_KEYS + KEY_NOTIFY + KEY_CREATE_LINK + READ_CONTROL;
	
	// 返回值...
	final int ERROR_NONE = 0;
	final int ERROR_BADKEY = 2;
	final int ERROR_ACCESS_DENIED = 8;
	
	final int ERROR_SUCCESS = 0;
	
	// 有关导入/导出的常量
	final int REG_FORCE_RESTORE=8;
	final int TOKEN_QUERY=0x8;
	final int TOKEN_ADJUST_PRIVILEGES=0x20;
	final int SE_PRIVILEGE_ENABLED=0x2;
	final String SE_RESTORE_NAME="SeRestorePrivilege";
	final String SE_BACKUP_NAME="SeBackupPrivilege";
	
}
