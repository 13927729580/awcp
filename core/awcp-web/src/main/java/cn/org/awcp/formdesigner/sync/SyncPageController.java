package cn.org.awcp.formdesigner.sync;

import cn.org.awcp.venson.controller.base.ControllerHelper;
import cn.org.awcp.venson.controller.base.ReturnResult;
import cn.org.awcp.venson.controller.base.StatusCode;
import cn.org.awcp.venson.exception.PlatformException;
import cn.org.awcp.venson.util.BeanUtil;
import cn.org.awcp.venson.util.HttpUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@RestController
@RequestMapping("syncPage")
public class SyncPageController {
    /**
     * 日志对象
     */
    protected final Log logger = LogFactory.getLog(getClass());

    @Resource(name = "syncPageService")
    private SyncPageService syncPageService;

    @RequestMapping(value = "export/{appId}")
    public void exportApps(@PathVariable("appId") String appId, HttpServletResponse response) {
        Apps app = Apps.getApp(appId);
        if (app == null || app.getAppPages() == null || app.getAppPages().isEmpty()) {
            throw new PlatformException("应用未找到，导出失败");
        }
        try {
            syncPageService.exportApps(app);
            response.setContentType(ControllerHelper.CONTENT_TYPE_STREAM);
            response.setHeader(ControllerHelper.CONTENT_DISPOSITION,
                    "attachment; filename=" + ControllerHelper.processFileName(app.getName()) + ".awcp");
            BeanUtil.writeObject(app, response.getOutputStream());
        } catch (IOException e) {
            throw new PlatformException(e.getMessage());
        }
    }

    @RequestMapping(value = "import/{appId}")
    public ReturnResult importApps(@PathVariable("appId")  String appId) {
        ReturnResult result=ReturnResult.get();
        byte[] data=HttpUtils.downLoad(appId);
        if(data!=null){
            String message=syncPageService.importApps(new ByteArrayInputStream(data));
            result.setStatus(StatusCode.SUCCESS).setMessage(message);
        }else{
            result.setStatus(StatusCode.SUCCESS).setMessage("下载地址有误，无法下载");
        }
        return result;
    }

}
