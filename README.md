﻿![logo](https://static.oschina.net/uploads/space/2017/0325/120649_pqfj_13428.png)

# 1. AWCP是什么

AWCP是一个通过全栈配置实现项目功能开发的云平台，在线演示：http://119.29.226.152:8083/awcp

# 2. AWCP的特性

- 通过无代码开发，屏蔽开发的底层技术，减少对人的依赖；
- 以平台化实现开发过程的优化，规范化缩短开发周期；
- 业务逻辑可视化配置，从容应对需求变更，降低维护成本；
- 适配不同浏览器，以及屏幕大小，同时完成移动端部署；
- 工程可复制化、可推广化、可运营化
- 工作流使用jflow，适用中国式流程，任何复杂流程都有对应的解决方案
- 平台使用模板配置，可让表单呈现多样化，更有利于和其他平台进行整合
- 平台使用了元数据管理，更加方便业务人员和领域专家参与系统的建设过程
- 底层采用了云平台架构，更有利于云平台部署，由原始的组织机构衍化成租户模式进行管理和授权
- 平台由项目模式升级为运营模式，使个人开发、开发公司、业务方、管理方高效协同



# 3. AWCP的由来

纵观所有的项目开发，一直存在各式各样的问题，比如项目开发人员投入多，周期长，项目难以复制，或者维护工作量大，因为问题处理时间、体验度等影响维护满意度，再或者遇到服务人员瓶颈，大量重复性、技术难度较低的工作仍然需要开发人员参与，使用单位难于接受维护等，我们最终开发出这款满足中小型企业、政府、IT团队快速实施项目的平台，并且开源出来，方便大家。
    平台专注于电子政务和企业信息化行业的应用，任何需求的变化都不需要修改代码来实现，90%的功能可通过配置实施完成，系统已有模板不能支撑的时候，平台提供接口或开发相应的模板和组件来支撑业务。通过AWCP开发平台可以快速推出1.0版本交给客户确认，再逐步迭代开发完成目标系统，针对一些有需求，但又不想养团队、想以最小代价来实现信息化、想找到靠谱的团队来确保项目的成功的公司，应用了AWCP开发平台后，可以事半功倍，不再担忧客户需求变化，开发人员流失等棘手问题，从而让客户专注于市场和产品需求。

# 4. License

The GPL License.

Copyright (c) 2017 OSChina.net

# 5. 我们正在用

`Team@OSC` , `Git@OSC`

# 6. 使用案例
- 深圳市人民医院OA
- 澳大利亚欧盛协同办公系统
- 缘梦大健康平台
- 深圳市科协自动化办公系统
- 安防企业接单平台
- 内控指标评价系统
- 内控报告管理系统
- 智慧经服平台
-行政事业单位内控管理平台



# 7. 配置参数说明
- application.database.type=mysql
- driver=com.mysql.jdbc.Driver
- url=jdbc:mysql://数据库服务器:端口/数据库名?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true&useOldAliasMetadataBehavior=true
- username=用户名
- password=密码

