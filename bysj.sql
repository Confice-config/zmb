/*
 Navicat Premium Dump SQL

 Source Server         : bendi
 Source Server Type    : MySQL
 Source Server Version : 50744 (5.7.44-log)
 Source Host           : localhost:3306
 Source Schema         : bysj

 Target Server Type    : MySQL
 Target Server Version : 50744 (5.7.44-log)
 File Encoding         : 65001

 Date: 04/07/2025 13:01:58
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for attendance
-- ----------------------------
DROP TABLE IF EXISTS `attendance`;
CREATE TABLE `attendance`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `employee_id` bigint(20) UNSIGNED NOT NULL COMMENT '员工ID',
  `checkin_time` datetime NULL DEFAULT NULL COMMENT '上班打卡时间',
  `checkout_time` datetime NULL DEFAULT NULL COMMENT '下班打卡时间',
  `date` date NOT NULL COMMENT '打卡日期',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `employee_id`(`employee_id`, `date`) USING BTREE,
  INDEX `idx_date`(`date`) USING BTREE,
  CONSTRAINT `attendance_ibfk_1` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '每日考勤记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of attendance
-- ----------------------------
INSERT INTO `attendance` VALUES (11, 1, '2025-05-01 08:50:00', '2025-05-01 18:30:00', '2025-05-01', '全勤，无异常');
INSERT INTO `attendance` VALUES (12, 1, '2025-05-02 09:15:00', '2025-05-02 18:10:00', '2025-05-02', '地铁故障导致迟到');
INSERT INTO `attendance` VALUES (13, 2, '2025-05-01 08:45:00', '2025-05-01 17:30:00', '2025-05-01', '家中急事提前下班');
INSERT INTO `attendance` VALUES (14, 2, '2025-05-03 08:55:00', '2025-05-03 18:20:00', '2025-05-03', '正常出勤，无备注');
INSERT INTO `attendance` VALUES (15, 3, NULL, NULL, '2025-05-04', '未打卡，无请假记录');
INSERT INTO `attendance` VALUES (16, 3, '2025-05-05 08:50:00', '2025-05-05 18:15:00', '2025-05-05', '补录4日旷工后的正常打卡');
INSERT INTO `attendance` VALUES (17, 4, '2025-05-06 09:20:00', '2025-05-06 17:45:00', '2025-05-06', '上午会议迟到，下午提前离场');
INSERT INTO `attendance` VALUES (18, 4, '2025-05-07 08:40:00', '2025-05-07 18:05:00', '2025-05-07', '准时打卡，工作正常');
INSERT INTO `attendance` VALUES (19, 5, '2025-05-08 08:55:00', '2025-05-08 18:30:00', '2025-05-08', '全勤，加班30分钟');

-- ----------------------------
-- Table structure for contract
-- ----------------------------
DROP TABLE IF EXISTS `contract`;
CREATE TABLE `contract`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `employee_id` bigint(20) UNSIGNED NOT NULL COMMENT '员工ID',
  `contract_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '合同类型（正式/实习/临时）',
  `start_date` date NOT NULL COMMENT '合同开始日期',
  `end_date` date NOT NULL COMMENT '合同结束日期',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '有效' COMMENT '状态（有效/已过期/终止）',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `employee_id`(`employee_id`) USING BTREE,
  INDEX `idx_status`(`status`) USING BTREE,
  CONSTRAINT `contract_ibfk_1` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '员工合同记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of contract
-- ----------------------------
INSERT INTO `contract` VALUES (1, 1, '正式', '2023-01-01', '2026-01-01', '有效');
INSERT INTO `contract` VALUES (2, 2, '实习', '2024-03-15', '2024-09-15', '已过期');
INSERT INTO `contract` VALUES (3, 3, '临时', '2025-05-01', '2025-07-31', '有效');
INSERT INTO `contract` VALUES (4, 4, '正式', '2022-08-01', '2027-08-01', '有效');
INSERT INTO `contract` VALUES (5, 5, '实习', '2025-02-20', '2025-08-20', '有效');
INSERT INTO `contract` VALUES (6, 6, '临时', '2025-04-10', '2025-06-30', '终止');
INSERT INTO `contract` VALUES (7, 7, '正式', '2021-12-01', '2026-12-01', '有效');
INSERT INTO `contract` VALUES (8, 8, '实习', '2025-01-05', '2025-07-05', '有效');
INSERT INTO `contract` VALUES (9, 9, '临时', '2025-03-25', '2025-05-31', '已过期');
INSERT INTO `contract` VALUES (10, 10, '正式', '2020-05-01', '2025-05-01', '已过期');

-- ----------------------------
-- Table structure for department
-- ----------------------------
DROP TABLE IF EXISTS `department`;
CREATE TABLE `department`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '部门名称',
  `manager` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '部门负责人',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '部门描述',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `name`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '部门信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of department
-- ----------------------------
INSERT INTO `department` VALUES (1, '技术研发部', '王大力', '负责软件开发与技术创新');
INSERT INTO `department` VALUES (2, '人力资源部', '李晓梅', '员工招聘、培训与绩效考核');
INSERT INTO `department` VALUES (3, '财务审计部', '张正华', '公司资金管理与财务审计');
INSERT INTO `department` VALUES (4, '市场营销部', '陈薇薇', '市场推广与客户关系维护');
INSERT INTO `department` VALUES (5, '行政后勤部', '赵国庆', '办公环境维护与物资管理');

-- ----------------------------
-- Table structure for employee
-- ----------------------------
DROP TABLE IF EXISTS `employee`;
CREATE TABLE `employee`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '姓名',
  `gender` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '性别（男/女）',
  `age` int(10) UNSIGNED NULL DEFAULT NULL COMMENT '年龄',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号（唯一）',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱（唯一）',
  `department_id` bigint(20) UNSIGNED NOT NULL COMMENT '所属部门ID',
  `hire_date` date NOT NULL COMMENT '入职日期',
  `position` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '职位',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '在职' COMMENT '状态（在职/离职/退休）',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `phone`(`phone`) USING BTREE,
  UNIQUE INDEX `email`(`email`) USING BTREE,
  INDEX `department_id`(`department_id`) USING BTREE,
  INDEX `idx_status`(`status`) USING BTREE,
  CONSTRAINT `employee_ibfk_1` FOREIGN KEY (`department_id`) REFERENCES `department` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 65 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '员工信息主表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of employee
-- ----------------------------
INSERT INTO `employee` VALUES (1, '王建军', '男', 35, '13800138001', 'wangjianjun_tech@example.com', 1, '2015-03-15', '高级软件工程师', '在职');
INSERT INTO `employee` VALUES (2, '李丽华', '女', 28, '13900139002', 'lilihua_tech@example.com', 1, '2018-08-20', '后端开发工程师', '在职');
INSERT INTO `employee` VALUES (3, '张宏伟', '男', 32, '14700147003', 'zhanghongwei_tech@example.com', 1, '2017-01-10', '前端开发工程师', '在职');
INSERT INTO `employee` VALUES (4, '陈海燕', '女', 26, '15800158004', 'chenhaiyan_tech@example.com', 1, '2020-05-25', '测试工程师', '在职');
INSERT INTO `employee` VALUES (5, '赵天宇', '男', 30, '17700177005', 'zhaotianyu_tech@example.com', 1, '2019-11-08', '系统架构师', '在职');
INSERT INTO `employee` VALUES (6, '周小美', '女', 27, '18600186006', 'zhouxiaomei_tech@example.com', 1, '2021-04-12', 'UI设计师', '在职');
INSERT INTO `employee` VALUES (7, '吴明轩', '男', 33, '13500135007', 'wumingxuan_tech@example.com', 1, '2016-09-01', '数据库管理员', '在职');
INSERT INTO `employee` VALUES (8, '郑小芳', '女', 29, '14900149008', 'zhengxiaofang_tech@example.com', 1, '2022-07-18', '运维工程师', '在职');
INSERT INTO `employee` VALUES (9, '王建国', '男', 38, '15000150009', 'wangjianguo_tech@example.com', 1, '2014-02-28', '技术总监', '在职');
INSERT INTO `employee` VALUES (10, '李建军', '男', 31, '17800178010', 'lijianjun_tech@example.com', 1, '2019-03-05', '中级开发工程师', '在职');
INSERT INTO `employee` VALUES (11, '张美丽', '女', 25, '18800188011', 'zhangmeili_tech@example.com', 1, '2023-01-10', '实习生', '在职');
INSERT INTO `employee` VALUES (12, '陈小龙', '男', 34, '13000130012', 'chenxiaolong_tech@example.com', 1, '2015-12-01', '产品经理', '在职');
INSERT INTO `employee` VALUES (13, '李晓燕', '女', 32, '13600136013', 'lixiaoyan_hr@example.com', 2, '2016-04-20', 'HR经理', '在职');
INSERT INTO `employee` VALUES (14, '王志强', '男', 29, '14800148014', 'wangzhiqiang_hr@example.com', 2, '2019-06-15', '招聘专员', '在职');
INSERT INTO `employee` VALUES (15, '张丽华', '女', 30, '15900159015', 'zhanglihua_hr@example.com', 2, '2018-03-10', '培训专员', '在职');
INSERT INTO `employee` VALUES (16, '陈俊杰', '男', 33, '17600176016', 'chenjunjie_hr@example.com', 2, '2017-09-25', '薪酬福利专员', '在职');
INSERT INTO `employee` VALUES (17, '赵雅静', '女', 28, '18900189017', 'zhaoyajing_hr@example.com', 2, '2020-11-05', '绩效考核专员', '在职');
INSERT INTO `employee` VALUES (18, '周明宇', '男', 31, '13200132018', 'zhoumingyu_hr@example.com', 2, '2019-02-20', '员工关系专员', '在职');
INSERT INTO `employee` VALUES (19, '吴小芳', '女', 27, '14600146019', 'wuxiaofang_hr@example.com', 2, '2021-08-15', '实习生', '在职');
INSERT INTO `employee` VALUES (20, '郑大海', '男', 34, '15100151020', 'zhengdahai_hr@example.com', 2, '2016-12-01', 'HR总监', '在职');
INSERT INTO `employee` VALUES (21, '王海燕', '女', 29, '17900179021', 'wanghaiyan_hr@example.com', 2, '2022-04-10', '校园招聘专员', '在职');
INSERT INTO `employee` VALUES (22, '李建国', '男', 35, '18100181022', 'lijianuo_hr@example.com', 2, '2015-07-25', '培训主管', '在职');
INSERT INTO `employee` VALUES (23, '张志强', '男', 30, '13400134023', 'zhangzhiqiang_hr@example.com', 2, '2020-03-08', '考勤管理员', '在职');
INSERT INTO `employee` VALUES (24, '陈丽华', '女', 33, '14500145024', 'chenlihua_hr@example.com', 2, '2017-05-15', '薪酬分析师', '在职');
INSERT INTO `employee` VALUES (25, '张建国', '男', 36, '13700137025', 'zhangjianguo_fin@example.com', 3, '2015-01-10', '财务总监', '在职');
INSERT INTO `employee` VALUES (26, '陈海燕', '女', 31, '14900149026', 'chenhaiyan_fin@example.com', 3, '2018-07-20', '会计主管', '在职');
INSERT INTO `employee` VALUES (27, '赵志强', '男', 29, '15200152027', 'zhaozhiqiang_fin@example.com', 3, '2019-04-15', '成本会计', '在职');
INSERT INTO `employee` VALUES (28, '周小美', '女', 28, '17800178028', 'zhouxiaomei_fin@example.com', 3, '2020-10-05', '税务会计', '在职');
INSERT INTO `employee` VALUES (29, '吴明轩', '男', 34, '18200182029', 'wumingxuan_fin@example.com', 3, '2016-12-25', '财务分析师', '在职');
INSERT INTO `employee` VALUES (30, '郑丽华', '女', 30, '13100131030', 'zhenglahua_fin@example.com', 3, '2017-09-10', '审计专员', '在职');
INSERT INTO `employee` VALUES (31, '王大海', '男', 33, '14700147031', 'wangdahai_fin@example.com', 3, '2019-06-01', '预算管理员', '在职');
INSERT INTO `employee` VALUES (32, '李雅静', '女', 27, '15800158032', 'liyjing_fin@example.com', 3, '2021-03-15', '出纳', '在职');
INSERT INTO `employee` VALUES (33, '张小龙', '男', 35, '17700177033', 'zhangxiaolong_fin@example.com', 3, '2014-08-20', '财务经理', '在职');
INSERT INTO `employee` VALUES (34, '陈志强', '男', 31, '18600186034', 'chenzhiqiang_fin@example.com', 3, '2020-02-28', '资产会计', '在职');
INSERT INTO `employee` VALUES (35, '赵美丽', '女', 26, '13300133035', 'zhaomeili_fin@example.com', 3, '2022-05-10', '实习生', '在职');
INSERT INTO `employee` VALUES (36, '周明宇', '男', 32, '14800148036', 'zhoumingyu_fin@example.com', 3, '2018-11-01', '内部审计师', '在职');
INSERT INTO `employee` VALUES (37, '陈建国', '男', 34, '13800138037', 'chenjianguo_market@example.com', 4, '2016-06-15', '市场总监', '在职');
INSERT INTO `employee` VALUES (38, '李志强', '男', 30, '13900139038', 'lizhiqiang_market@example.com', 4, '2019-09-20', '品牌经理', '在职');
INSERT INTO `employee` VALUES (39, '张海燕', '女', 29, '14700147039', 'zhanghaiyan_market@example.com', 4, '2018-04-10', '市场策划专员', '在职');
INSERT INTO `employee` VALUES (40, '陈丽华', '女', 28, '15800158040', 'chenlihua_market@example.com', 4, '2020-07-25', '活动运营专员', '在职');
INSERT INTO `employee` VALUES (41, '赵明轩', '男', 31, '17700177041', 'zhaomingxuan_market@example.com', 4, '2019-12-05', '渠道经理', '在职');
INSERT INTO `employee` VALUES (42, '周小美', '女', 27, '18600186042', 'zhouxiaomei_market@example.com', 4, '2021-03-18', '新媒体运营', '在职');
INSERT INTO `employee` VALUES (43, '吴大海', '男', 33, '13500135043', 'wudahai_market@example.com', 4, '2017-05-01', '客户关系经理', '在职');
INSERT INTO `employee` VALUES (44, '郑雅静', '女', 32, '14900149044', 'zhengyajing_market@example.com', 4, '2020-11-10', '市场调研专员', '在职');
INSERT INTO `employee` VALUES (45, '王小龙', '男', 35, '15000150045', 'wangxiaolong_market@example.com', 4, '2015-08-25', '营销总监', '在职');
INSERT INTO `employee` VALUES (46, '李美丽', '女', 26, '17800178046', 'limeili_market@example.com', 4, '2022-01-15', '实习生', '在职');
INSERT INTO `employee` VALUES (47, '张志强', '男', 30, '18800188047', 'zhangzhiqiang_market@example.com', 4, '2019-02-28', '电商运营专员', '在职');
INSERT INTO `employee` VALUES (48, '陈海燕', '女', 31, '13000130048', 'chenhaiyan_market@example.com', 4, '2018-07-10', '广告投放专员', '在职');
INSERT INTO `employee` VALUES (49, '赵建国', '男', 37, '13600136049', 'zhaojianguo_admin@example.com', 5, '2014-10-01', '行政总监', '在职');
INSERT INTO `employee` VALUES (50, '周丽华', '女', 33, '14800148050', 'zhoulihua_admin@example.com', 5, '2017-01-15', '行政经理', '在职');
INSERT INTO `employee` VALUES (51, '吴志强', '男', 31, '15900159051', 'wuzhiqiang_admin@example.com', 5, '2018-06-20', '后勤主管', '在职');
INSERT INTO `employee` VALUES (52, '郑大海', '男', 34, '17600176052', 'zhengdahai_admin@example.com', 5, '2019-03-10', '资产管理员', '在职');
INSERT INTO `employee` VALUES (53, '王雅静', '女', 29, '18900189053', 'wangyajing_admin@example.com', 5, '2020-08-25', '前台接待', '在职');
INSERT INTO `employee` VALUES (54, '李小明', '男', 28, '13200132054', 'lixiaoming_admin@example.com', 5, '2021-04-05', '办公用品采购', '在职');
INSERT INTO `employee` VALUES (55, '张美丽', '女', 27, '14600146055', 'zhangmeili_admin@example.com', 5, '2022-09-15', '车辆调度员', '在职');
INSERT INTO `employee` VALUES (56, '陈志强', '男', 32, '15100151056', 'chenzhiqiang_admin@example.com', 5, '2016-12-01', '园区管理员', '在职');
INSERT INTO `employee` VALUES (57, '赵小龙', '男', 30, '17900179057', 'zhaoxiaolong_admin@example.com', 5, '2019-07-20', '食堂管理员', '在职');
INSERT INTO `employee` VALUES (58, '周明轩', '女', 26, '18100181058', 'zhoumingxuan_admin@example.com', 5, '2023-01-10', '实习生', '在职');
INSERT INTO `employee` VALUES (59, '吴海燕', '女', 31, '13400134059', 'wuhaiyan_admin@example.com', 5, '2018-05-15', '会议协调员', '在职');
INSERT INTO `employee` VALUES (60, '郑建国', '男', 35, '14500145060', 'zhengjianguo_admin@example.com', 5, '2015-11-28', '行政助理', '在职');
INSERT INTO `employee` VALUES (61, '测试HR', '男', 35, '13111111111', 'ces_tech@example.com', 1, '2015-03-15', '高级软件工程师', '退休');
INSERT INTO `employee` VALUES (62, '测试员工', '女', 28, '22222222222', 'ces2_tech@example.com', 1, '2018-08-20', '后端开发工程师', '在职');
INSERT INTO `employee` VALUES (64, '张张', '男', 11, '15998052157', NULL, 1, '2025-05-05', '实习', '在职');

-- ----------------------------
-- Table structure for leave_request
-- ----------------------------
DROP TABLE IF EXISTS `leave_request`;
CREATE TABLE `leave_request`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `employee_id` bigint(20) UNSIGNED NOT NULL COMMENT '员工ID',
  `leave_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '请假类型（病假/事假/年假）',
  `start_date` date NOT NULL COMMENT '请假开始日期',
  `end_date` date NOT NULL COMMENT '请假结束日期',
  `reason` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '请假理由',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '待审批' COMMENT '状态（待审批/通过/驳回）',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `employee_id`(`employee_id`) USING BTREE,
  INDEX `idx_status`(`status`) USING BTREE,
  CONSTRAINT `leave_request_ibfk_1` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 31 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '请假申请记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of leave_request
-- ----------------------------
INSERT INTO `leave_request` VALUES (1, 1, '病假', '2024-01-05', '2024-01-07', '感冒发烧，需休息调理', '通过');
INSERT INTO `leave_request` VALUES (2, 2, '事假', '2024-02-09', '2024-02-11', '处理家庭紧急事务', '待审批');
INSERT INTO `leave_request` VALUES (3, 3, '年假', '2024-03-15', '2024-03-22', '计划出国旅游放松', '通过');
INSERT INTO `leave_request` VALUES (4, 1, '事假', '2024-04-08', '2024-04-08', '参加大学同学婚礼', '驳回');
INSERT INTO `leave_request` VALUES (5, 4, '病假', '2024-05-12', '2024-05-14', '急性肠胃炎住院治疗', '通过');
INSERT INTO `leave_request` VALUES (6, 5, '年假', '2024-06-19', '2024-06-27', '陪家人国内旅游', '驳回');
INSERT INTO `leave_request` VALUES (7, 2, '事假', '2024-07-03', '2024-07-05', '办理个人房产手续', '通过');
INSERT INTO `leave_request` VALUES (8, 6, '病假', '2024-08-18', '2024-08-20', '腰椎间盘突出理疗', '驳回');
INSERT INTO `leave_request` VALUES (9, 3, '年假', '2024-09-25', '2024-10-07', '长途自驾旅行', '通过');
INSERT INTO `leave_request` VALUES (10, 7, '事假', '2024-10-15', '2024-10-16', '参加职业资格考试', '待审批');
INSERT INTO `leave_request` VALUES (11, 4, '病假', '2024-11-01', '2024-11-03', '呼吸道感染需隔离', '通过');
INSERT INTO `leave_request` VALUES (12, 8, '年假', '2024-12-05', '2024-12-12', '回老家探亲', '通过');
INSERT INTO `leave_request` VALUES (13, 5, '事假', '2025-01-10', '2025-01-12', '处理宠物手术陪护', '驳回');
INSERT INTO `leave_request` VALUES (14, 9, '病假', '2025-02-15', '2025-02-17', '意外摔伤需要休养', '待审批');
INSERT INTO `leave_request` VALUES (15, 6, '年假', '2025-03-20', '2025-03-27', '参加行业技术研讨会', '通过');
INSERT INTO `leave_request` VALUES (16, 7, '事假', '2025-04-05', '2025-04-05', '参加孩子学校家长会', '通过');
INSERT INTO `leave_request` VALUES (17, 10, '病假', '2025-05-01', '2025-05-03', '慢性咽炎住院治疗', '驳回');
INSERT INTO `leave_request` VALUES (18, 1, '年假', '2024-04-20', '2024-04-28', '筹备婚礼相关事宜', '待审批');
INSERT INTO `leave_request` VALUES (19, 2, '事假', '2024-06-08', '2024-06-09', '处理车辆违章事务', '通过');
INSERT INTO `leave_request` VALUES (20, 3, '病假', '2024-07-25', '2024-07-27', '急性阑尾炎手术恢复', '通过');
INSERT INTO `leave_request` VALUES (21, 4, '年假', '2024-08-10', '2024-08-17', '前往海边度假放松', '驳回');
INSERT INTO `leave_request` VALUES (22, 5, '事假', '2024-09-15', '2024-09-16', '办理护照更换手续', '待审批');
INSERT INTO `leave_request` VALUES (23, 6, '病假', '2024-10-20', '2024-10-22', '过敏性鼻炎发作需休息', '通过');
INSERT INTO `leave_request` VALUES (24, 7, '年假', '2024-11-25', '2024-12-02', '陪伴父母体检就医', '通过');
INSERT INTO `leave_request` VALUES (25, 8, '事假', '2025-01-18', '2025-01-20', '参加亲戚婚礼庆典', '驳回');
INSERT INTO `leave_request` VALUES (26, 9, '病假', '2025-02-28', '2025-03-02', '食物中毒需要观察治疗', '待审批');
INSERT INTO `leave_request` VALUES (27, 10, '年假', '2025-04-10', '2025-04-17', '报名参加编程培训课程', '通过');
INSERT INTO `leave_request` VALUES (28, 1, '事假', '2025-05-05', '2025-05-05', '处理社区志愿者服务', '通过');
INSERT INTO `leave_request` VALUES (29, 2, '病假', '2024-12-30', '2025-01-01', '流感感染需要隔离休息', '驳回');
INSERT INTO `leave_request` VALUES (30, 3, '年假', '2025-03-30', '2025-04-06', '计划国内徒步旅行', '待审批');

-- ----------------------------
-- Table structure for resignation
-- ----------------------------
DROP TABLE IF EXISTS `resignation`;
CREATE TABLE `resignation`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `employee_id` bigint(20) UNSIGNED NOT NULL COMMENT '员工ID',
  `resignation_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '离职类型（主动离职/辞退/退休）',
  `apply_date` date NOT NULL COMMENT '申请日期',
  `last_work_date` date NOT NULL COMMENT '最后工作日',
  `reason` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '离职理由',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '待审批' COMMENT '状态（待审批/已批准/已驳回）',
  `approver_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '审批人ID（关联user表）',
  `approval_comment` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '审批意见',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `employee_id`(`employee_id`) USING BTREE,
  INDEX `approver_id`(`approver_id`) USING BTREE,
  INDEX `idx_status`(`status`) USING BTREE,
  CONSTRAINT `resignation_ibfk_1` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `resignation_ibfk_2` FOREIGN KEY (`approver_id`) REFERENCES `user` (`id`) ON DELETE SET NULL ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 63 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '员工离职申请表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of resignation
-- ----------------------------
INSERT INTO `resignation` VALUES (41, 1, '主动离职', '2023-01-15', '2023-01-31', '个人职业规划调整，寻求新的发展机会', '已批准', 1, '审批通过，流程合规');
INSERT INTO `resignation` VALUES (42, 2, '辞退', '2023-02-20', '2023-03-05', '连续三个月绩效考核不达标', '已批准', 2, '同意辞退，按规定办理手续');
INSERT INTO `resignation` VALUES (43, 3, '退休', '2023-03-10', '2023-05-01', '达到法定退休年龄', '已批准', 3, '退休申请通过，祝生活愉快');
INSERT INTO `resignation` VALUES (44, 4, '主动离职', '2023-04-25', '2023-05-15', '因家庭原因返回家乡', '待审批', NULL, NULL);
INSERT INTO `resignation` VALUES (45, 5, '辞退', '2023-05-08', '2023-05-22', '多次违反规章制度', '已驳回', 2, '证据不足，需补充材料');
INSERT INTO `resignation` VALUES (46, 6, '主动离职', '2023-06-12', '2023-06-30', '攻读硕士研究生', '待审批', NULL, NULL);
INSERT INTO `resignation` VALUES (47, 7, '辞退', '2023-07-18', '2023-08-02', '试用期无法胜任岗位', '已批准', 3, '试用期考核不通过');
INSERT INTO `resignation` VALUES (48, 8, '退休', '2023-08-25', '2023-10-01', '符合公司退休政策', '已批准', 1, '退休流程已备案');
INSERT INTO `resignation` VALUES (49, 9, '主动离职', '2023-09-05', '2023-09-20', '加入创业公司', '待审批', NULL, NULL);
INSERT INTO `resignation` VALUES (50, 10, '辞退', '2023-10-10', '2023-10-25', '泄露公司机密信息', '已驳回', 2, '证据不充分，重新调查');
INSERT INTO `resignation` VALUES (51, 11, '主动离职', '2023-11-15', '2023-12-01', '健康原因需要休养', '待审批', NULL, NULL);
INSERT INTO `resignation` VALUES (52, 12, '退休', '2023-12-20', '2024-01-15', '年满60周岁', '已批准', 3, '退休申请通过，注意交接');
INSERT INTO `resignation` VALUES (53, 13, '主动离职', '2024-01-25', '2024-02-10', '移民海外', '待审批', NULL, NULL);
INSERT INTO `resignation` VALUES (54, 14, '辞退', '2024-02-28', '2024-03-15', '无故旷工超过3天', '已批准', 2, '旷工事实清楚，同意辞退');
INSERT INTO `resignation` VALUES (55, 15, '退休', '2024-03-05', '2024-04-20', '达到公司退休年龄', '已批准', 1, '退休流程已完成');
INSERT INTO `resignation` VALUES (56, 16, '主动离职', '2024-04-10', '2024-04-30', '考取公务员', '待审批', NULL, NULL);
INSERT INTO `resignation` VALUES (57, 17, '辞退', '2024-05-15', '2024-05-31', '年度考核不合格', '已批准', 3, '调岗后仍不达标，同意辞退');
INSERT INTO `resignation` VALUES (58, 18, '退休', '2024-06-20', '2024-07-31', '特殊工种提前退休', '已批准', 1, '提前退休申请通过');
INSERT INTO `resignation` VALUES (59, 19, '主动离职', '2024-07-25', '2024-08-10', '自主创业', '待审批', NULL, NULL);
INSERT INTO `resignation` VALUES (60, 20, '辞退', '2024-08-30', '2024-09-15', '利用职务谋取私利', '已批准', 2, '调查属实，同意辞退');
INSERT INTO `resignation` VALUES (61, 3, '辞退', '2025-05-06', '2025-05-07', 'dasdasd', '已批准', 2, NULL);
INSERT INTO `resignation` VALUES (62, 3, '主动离职', '2025-05-01', '2025-05-23', 'sdadjkjk', '已驳回', 2, NULL);

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色名称（如HR、员工）',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `name`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES (2, 'HR专员');
INSERT INTO `role` VALUES (3, '普通员工');
INSERT INTO `role` VALUES (1, '系统管理员');

-- ----------------------------
-- Table structure for salary
-- ----------------------------
DROP TABLE IF EXISTS `salary`;
CREATE TABLE `salary`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `employee_id` bigint(20) UNSIGNED NOT NULL COMMENT '员工ID',
  `base_salary` decimal(10, 2) NOT NULL COMMENT '基本工资',
  `bonus` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '奖金',
  `performance` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '绩效工资',
  `month` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '发薪月份（格式：YYYY-MM）',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `employee_id`(`employee_id`, `month`) USING BTREE,
  INDEX `idx_month`(`month`) USING BTREE,
  CONSTRAINT `salary_ibfk_1` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '薪资发放记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of salary
-- ----------------------------
INSERT INTO `salary` VALUES (2, 1, 8000.00, 1500.00, 2000.00, '2025-01');
INSERT INTO `salary` VALUES (3, 2, 7500.50, 1200.30, 1800.80, '2025-02');
INSERT INTO `salary` VALUES (4, 3, 9000.00, 2000.00, 2500.00, '2025-03');
INSERT INTO `salary` VALUES (5, 4, 6500.75, 1000.50, 1500.25, '2025-04');
INSERT INTO `salary` VALUES (6, 5, 8500.00, 1800.00, 2200.00, '2025-05');
INSERT INTO `salary` VALUES (7, 6, 7000.90, 1300.60, 1900.40, '2025-06');
INSERT INTO `salary` VALUES (8, 7, 9500.00, 2200.00, 2800.00, '2025-07');
INSERT INTO `salary` VALUES (9, 8, 6000.25, 900.75, 1400.50, '2025-08');
INSERT INTO `salary` VALUES (10, 9, 8800.00, 1700.00, 2100.00, '2025-09');
INSERT INTO `salary` VALUES (11, 10, 7200.40, 1400.80, 2000.60, '2025-10');

-- ----------------------------
-- Table structure for training
-- ----------------------------
DROP TABLE IF EXISTS `training`;
CREATE TABLE `training`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '培训名称',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '培训内容',
  `date` date NOT NULL COMMENT '培训日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '培训计划表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of training
-- ----------------------------
INSERT INTO `training` VALUES (1, '新员工入职培训', '涵盖公司制度、企业文化、岗位技能', '2025-01-15');
INSERT INTO `training` VALUES (2, '安全生产培训', '安全操作规范与应急处理', '2025-02-20');
INSERT INTO `training` VALUES (3, '技术前沿分享会', 'AI技术在开发中的应用', '2025-03-25');
INSERT INTO `training` VALUES (4, '客户服务培训', '沟通技巧与投诉处理', '2025-04-10');
INSERT INTO `training` VALUES (5, '项目管理实战', '敏捷开发与进度控制', '2025-05-08');
INSERT INTO `training` VALUES (6, '财务报销培训', '报销流程与合规要求', '2025-06-12');
INSERT INTO `training` VALUES (7, '团队协作工作坊', '跨部门协作与冲突解决', '2025-07-18');
INSERT INTO `training` VALUES (8, '产品经理进阶', '需求分析与原型设计', '2025-08-22');
INSERT INTO `training` VALUES (9, '数据可视化培训', 'Tableau与Power BI使用', '2025-09-28');
INSERT INTO `training` VALUES (10, '绩效考核说明会', 'KPI指标与评价标准', '2025-10-15');

-- ----------------------------
-- Table structure for training_participant
-- ----------------------------
DROP TABLE IF EXISTS `training_participant`;
CREATE TABLE `training_participant`  (
  `training_id` bigint(20) UNSIGNED NOT NULL COMMENT '培训ID',
  `employee_id` bigint(20) UNSIGNED NOT NULL COMMENT '员工ID',
  PRIMARY KEY (`training_id`, `employee_id`) USING BTREE,
  INDEX `employee_id`(`employee_id`) USING BTREE,
  INDEX `idx_training`(`training_id`) USING BTREE,
  CONSTRAINT `training_participant_ibfk_1` FOREIGN KEY (`training_id`) REFERENCES `training` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `training_participant_ibfk_2` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '培训参与关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of training_participant
-- ----------------------------

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名（唯一）',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码（加密存储）',
  `role_id` bigint(20) UNSIGNED NOT NULL COMMENT '角色ID',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `employee_id` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '员工id',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE,
  INDEX `role_id`(`role_id`) USING BTREE,
  INDEX `idx_user_employee_id`(`employee_id`) USING BTREE,
  CONSTRAINT `fk_user_employee` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`) ON DELETE SET NULL ON UPDATE RESTRICT,
  CONSTRAINT `user_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'admin', 'admin', 1, '2023-01-01 09:00:00', 1, 'http://localhost:9999/files/download/1747668034989-1747667809218-eVnQ1JdIY4.jpg');
INSERT INTO `user` VALUES (2, '123', '123', 2, '2023-01-02 10:30:00', 2, NULL);
INSERT INTO `user` VALUES (3, '456', '456', 3, '2023-01-03 14:15:00', 3, NULL);
INSERT INTO `user` VALUES (4, '789', '789', 3, '2025-05-30 18:07:04', 4, NULL);

SET FOREIGN_KEY_CHECKS = 1;
