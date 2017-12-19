﻿![logo](https://static.oschina.net/uploads/space/2017/0325/120649_pqfj_13428.png)

# 1.  AWCP项目简介
  目前中小型企业信息化建设存在很大困难，外包开发成本高，业务很难延续，使用市场上已有的成熟系统又不能满足自身定制化需求，组建团队自行开发成本更高，导致中小型企业信息化建设、信息化资产沉淀都存在很大困难。程序员又是契约式的服务模式，每天加班赶进度，又在重复造轮子，针对这种行业现状和痛点，AWCP开发平台重点切入低成本，快速实施的共享定制化方向，底层架构满足SaaS软件模式下的定制化实施。通过平台聚集大量优质程序员，利用他们的空闲时间，在平台  上开发大量企业信息化基础稳定版本，免费推给企业使用，使用过程中再提出个性化需求，然后再发包到awcp社区，大量懂平台的开发人员都会进行抢单实施，同时平台又搭建整套软件生态管理云，确保系统交付质量和进度，并给程序员建立能力和评价体系，方便企业快速找到符合要求的工程师。经过一段时间运营后，平台会沉淀大量的业务系统和业务组件，后续的开发模式将改变，重复造轮子的痛点将很大程度的得到了改善，最终降低企业信息化建设成本，又能实施个性化定制，提升了企业的生存能力。
  在线演示：http://119.29.226.152:8083/awcp 用户名:test 密码:123456


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

# 4. 为何选择AWCP
- 平台有成熟的方法论支撑，基于DDD领域模型设计思想
- 平台基于元数据管理模块，贯穿整个可配置领域，做到零编码实现复杂业务
- 业务变更不需要修改任何java代码即可实现，满足需求变化频繁的系统
- 平台的页面呈现基于模板可自定义配置
- 平台后台服务可配置
- 平台基础组件可升级
- 使用目前主流的Java EE开发框架，简单易学，学习成本低。
- 数据库无限制，目前支持MySql，可扩充Oracle,SQL Server、PostgreSQL、H2等。
- 模块化设计，层次结构清晰。内置一系列企业信息管理的基础功能。
- 操作权限控制精密细致，对所有管理链接都进行权限验证，可控制到按钮。
- 提供常用工具类封装，日志、缓存、验证、字典、组织机构等，获取当前组织机构、字典等数据。
- 兼容目前最流行浏览器（IE7+、Chrome、Firefox）IE6也支持，但体验效果差。
- 平台支持移动端界面配置，PC端界面配置，流程配置
- awcp和其他平台开发不同之处，为业务人员、实施人员和开发人员搭建的一个共同协作平台

# 4. AWCP开发过程
首先由业务人员根据DDD设计思想分析业务领域模型，然后将业务领域模型通过元数据管理模块配置到系统中，之后产品经理根据业务描述设计界面，完成需求分析和界面原型，拿到界面原型后UI设计师和配置工程师分析哪些界面能做，哪些界面
不支持，不支持的交给开发人员进行模板配置和控件开发，完成后交给实施人员进行系统搭建，模板和组件随着业务系统的实施数量而逐步沉淀，后续有足够的模板和控件后，实施人员可快速轻松的完成复杂业务功能的定制，
最终为客户搭建一个可个性化定制的软件平台，节省成本，减少了人员流失带来损失，整个软件生态链都可以在awcp上做到沉淀，为产品奠定坚实的基础。

# 5.技术选型

1、后端

- 核心框架：Spring Framework 4.0
- 安全框架：Apache Shiro 1.2
- 视图框架：Spring MVC 4.0
- 工作流引擎：JFlow
- 持久层框架：MyBatis 3.2
- 数据库连接池：Alibaba Druid 1.0
- 缓存框架：Ehcache 2.6、Redis
- 日志管理：SLF4J 1.7、Log4j
- 工具类：Apache Commons、Jackson 2.2、Xstream 1.4、freemarker 5.3、POI 3.9

2、前端
- JS框架：jQuery 1.9。
- CSS框架：Twitter Bootstrap 2.3.1。
- 客户端验证：JQuery Validation Plugin 1.11。
- 富文本：CKEcitor
- 文件管理：CKFinder
- 手机端框架：ionic
- 数据表格：jqGrid
- 树结构控件：jQuery zTree
- 日期控件： My97DatePicker

3、平台部署
- 服务器中间件：在Java EE 5规范（Servlet 2.5、JSP 2.1）下开发，支持应用服务器中间件 有Tomcat 6、Jboss 7、WebLogic 10、WebSphere 8。
- 数据库支持：目前仅提供MySql数据库的支持，但不限于数据库，平台留有其它数据库支持接口， 可方便更改为其它数据库，如：Oracle、SqlServer 2008、MySql 5.5、H2等
- 开发环境：Java EE、Eclipse、Maven、Git

# 6. License

The GPL License.

Copyright (c) 2017 awcp.org.cn

# 7. 我们正在用

`Team@OSC` , `Git@OSC`

# 8. 使用案例
- 深圳市人民医院OA
- 澳大利亚欧盛协同办公系统
- 缘梦大健康平台
- 深圳市科协自动化办公系统
- 安防企业接单平台
- 内控指标评价系统
- 内控报告管理系统
- 智慧经服平台



# 9. 配置参数说明
- application.database.type=mysql
- driver=com.mysql.jdbc.Driver
- url=jdbc:mysql://数据库服务器:端口/数据库名?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true&useOldAliasMetadataBehavior=true
- username=用户名
- password=密码

# 10. 联系团队
- 联系人：曹勇
- 手机：13760484068
- QQ：64992751
- 微信：13760484068
- QQ群：274767845
