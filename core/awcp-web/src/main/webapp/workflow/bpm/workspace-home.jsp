<%@page contentType="text/html;charset=UTF-8"%>
<%@include file="../common/taglibs.jsp"%>
<%pageContext.setAttribute("currentHeader", "bpm-workspace");%>
<%pageContext.setAttribute("currentMenu", "bpm-process");%>
<!doctype html>
<html lang="en">

  <head>
    <%@include file="../common/meta.jsp"%>
    <title>流程列表</title>
    <%@include file="../common/s.jsp"%>
  </head>

  <body>

    <%-- <div class="row-fluid">
    <%@include file="../menu/bpm-workspace.jsp"%> --%>

    <!-- start of main -->
    <section style="width: 98%;margin: 0 auto;">

<c:forEach items="${models}" var="bpmCategory">
      <div class="row-fluid">
      <div class="page-header">
        <h3>${bpmCategory.name}</h3>
      </div>

      <c:forEach items="${bpmCategory.bpmProcesses}" var="bpmProcess">
        <div class="well span2">
          <h4>${bpmProcess.name}&nbsp;</h4>
          <p>${bpmProcess.descn}&nbsp;</p>
          <div class="btn-group">
            <a class="btn btn-small" href="${scopePrefix}/workflow/form/form-viewStartForm.do?bpmProcessId=${bpmProcess.id}"><li class="icon-play"></li>发起</a>
            <a class="btn btn-small" href="workspace-graphProcessDefinition.do?id=${bpmProcess.id}" target="_blank"><li class="icon-picture"></li>图形</a>
          </div>
        </div>
      </c:forEach>
	  </div>

</c:forEach>

    </section>
    <!-- end of main -->
    </div>

  </body>

</html>
