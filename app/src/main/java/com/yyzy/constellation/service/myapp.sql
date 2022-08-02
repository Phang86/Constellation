/*
 Navicat Premium Data Transfer

 Source Server         : test
 Source Server Type    : MySQL
 Source Server Version : 80028
 Source Host           : localhost:3306
 Source Schema         : myapp

 Target Server Type    : MySQL
 Target Server Version : 80028
 File Encoding         : 65001

 Date: 02/08/2022 15:24:24
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for hot_list
-- ----------------------------
DROP TABLE IF EXISTS `hot_list`;
CREATE TABLE `hot_list`  (
  `vid` int NOT NULL AUTO_INCREMENT COMMENT '视频id\r\n',
  `vtitle` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '视频标题\r\n',
  `author` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '作者姓名\r\n',
  `coverUrl` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '封面图',
  `headurl` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '作者头像url\r\n',
  `comment_num` int NULL DEFAULT NULL COMMENT '用户评论数',
  `like_num` int NULL DEFAULT NULL COMMENT '点赞数',
  `collect_num` int NULL DEFAULT NULL COMMENT '收藏数',
  `playUrl` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '视频url',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `category_id` int NULL DEFAULT NULL COMMENT '视频分类ID',
  PRIMARY KEY (`vid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 29 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of hot_list
-- ----------------------------
INSERT INTO `hot_list` VALUES (26, '中国新歌声：男子开口唱得太奇怪！', '灿星音乐现场', 'https://p9-xg.byteimg.com/img/tos-cn-i-0004/19c44751e9124b069d23cddbc46e29fb~tplv-crop-center:1041:582.jpg', 'https://p9-xg.byteimg.com/img/tos-cn-i-0004/19c44751e9124b069d23cddbc46e29fb~tplv-crop-center:1041:582.jpg', 759, 563, 548, 'https://vd4.bdstatic.com/mda-kh7qv1a9dxigp5h5/v1-cae/sc/mda-kh7qv1a9dxigp5h5.mp4?v_from_s=hkapp-haokan-hnb&auth_key=1652453981-0-0-9600b8f244c11034a77836582ededae5&bcevod_channel=searchbox_feed&pd=1&cd=0&pt=3&logid=1781246590&vid=8010480158801022342&abtest=101830_1-102148_2-17451_2&klogid=1781246590', '2022-05-11 09:28:30', '2022-05-13 22:32:23', NULL);
INSERT INTO `hot_list` VALUES (27, '拇指琴演奏《琅琊榜》插曲《红颜旧》琴声动人', '比三呆', 'https://p1-xg.byteimg.com/img/tos-cn-p-0000/527b08d0f31d4705a4d8f4a72120948c~tplv-crop-center:1041:582.jpg', 'https://p1-xg.byteimg.com/img/tos-cn-p-0000/527b08d0f31d4705a4d8f4a72120948c~tplv-crop-center:1041:582.jpg', 549, 359, 637, 'https://vd2.bdstatic.com/mda-kd4u3i3jegtv4vp4/v1-cae/sc/mda-kd4u3i3jegtv4vp4.mp4?v_from_s=hkapp-haokan-hna&auth_key=1652454475-0-0-fe11a06e7e071a1383c75aaa7b57e3dd&bcevod_channel=searchbox_feed&pd=1&cd=0&pt=3&logid=2275478838&vid=5372991809595112383&abtest=101830_1-102148_2-17451_2&klogid=2275478838', '2022-05-04 09:29:31', '2022-05-13 22:38:23', NULL);
INSERT INTO `hot_list` VALUES (28, '陈志朋台上唱《大田后生仔》', '下饭音乐', 'https://p3-xg.byteimg.com/img/tos-cn-i-0004/1820c36d7a3846acaca9c24f18b01944~tplv-crop-center:1041:582.jpg', 'https://p3-xg.byteimg.com/img/tos-cn-i-0004/1820c36d7a3846acaca9c24f18b01944~tplv-crop-center:1041:582.jpg', 368, 593, 347, 'https://vd2.bdstatic.com/mda-kbpqc5nfcvtizabf/v1-cae/sc/mda-kbpqc5nfcvtizabf.mp4?v_from_s=hkapp-haokan-hnb&auth_key=1652454528-0-0-14085b1785dc8cf133749efc9bad4d45&bcevod_channel=searchbox_feed&pd=1&cd=0&pt=3&logid=2327998709&vid=9618827788963298943&abtest=101830_1-102148_2-17451_2&klogid=2327998709', '2022-05-12 09:30:37', '2022-05-13 22:39:10', NULL);
INSERT INTO `hot_list` VALUES (29, '龚喜水库下网偶遇大鱼群，收网过程惊心动魄', '游钓寻鱼之路', 'https://p3-xg.byteimg.com/img/tos-cn-p-0026/a225869a56d1715823d6f74d6a765b01~tplv-crop-center:1041:582.jpg', 'https://p3-xg.byteimg.com/img/tos-cn-p-0026/a225869a56d1715823d6f74d6a765b01~tplv-crop-center:1041:582.jpg', 589, 687, 354, 'http://vfx.mtime.cn/Video/2019/03/14/mp4/190314223540373995.mp4', '2022-04-22 09:31:56', '2022-05-12 10:30:37', NULL);

-- ----------------------------
-- Table structure for legion_list
-- ----------------------------
DROP TABLE IF EXISTS `legion_list`;
CREATE TABLE `legion_list`  (
  `vid` int NOT NULL AUTO_INCREMENT COMMENT '视频id\r\n',
  `vtitle` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '视频标题\r\n',
  `author` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '作者姓名\r\n',
  `coverUrl` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '封面图',
  `headurl` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '作者头像url\r\n',
  `comment_num` int NULL DEFAULT NULL COMMENT '用户评论数',
  `like_num` int NULL DEFAULT NULL COMMENT '点赞数',
  `collect_num` int NULL DEFAULT NULL COMMENT '收藏数',
  `playUrl` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '视频url',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `category_id` int NULL DEFAULT NULL COMMENT '视频分类ID',
  PRIMARY KEY (`vid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 32 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of legion_list
-- ----------------------------
INSERT INTO `legion_list` VALUES (26, '演兵—2021空军：全时待战随时能战 有效提高制胜能力', 'CCTV', 'http://www.news.cn/video/titlepic/121150/1211505805_1640742267174_title0h.jpg', 'http://www.news.cn/video/titlepic/121150/1211505805_1640742267174_title0h.jpg', 28941, 6549, 3671, 'https://vodpub1.v.news.cn/original/20211229/448594f0b762479ba2e9a32a6d8be880.mp4', '2022-05-11 08:29:06', '2022-05-12 12:59:13', NULL);
INSERT INTO `legion_list` VALUES (27, '也门战局趋于白热化 萨那机场成焦点', 'CCTV', 'http://www.news.cn/video/titlepic/121150/1211502613_1640565699976_title0h.png', 'http://www.news.cn/video/titlepic/121150/1211502613_1640565699976_title0h.png', 9671, 6791, 6489, 'https://vodpub1.v.news.cn/original/20211227/364969a23ae9403585d2197d6b8348f0.mp4', '2022-05-04 08:30:54', '2022-05-12 12:58:57', NULL);
INSERT INTO `legion_list` VALUES (28, '俄总统新闻秘书：“锆石”让俄对西方声明“更有说服力”', 'CCTV', 'http://www.news.cn/video/titlepic/121150/1211502598_1640565396118_title0h.png', 'http://www.news.cn/video/titlepic/121150/1211502598_1640565396118_title0h.png', 7834, 9688, 6973, 'https://vodpub1.v.news.cn/original/20211227/be82a674014746449511a048b9d72f2d.mp4', '2022-05-03 08:32:22', '2022-05-12 12:58:47', NULL);
INSERT INTO `legion_list` VALUES (29, '新疆：帕米尔高原 武警特战队员极限训练', 'CCTV', 'http://www.news.cn/video/titlepic/121148/1211486128_1639444050681_title0h.jpg', 'http://www.news.cn/video/titlepic/121148/1211486128_1639444050681_title0h.jpg', 8647, 6875, 9687, 'https://vodpub1.v.news.cn/original/20211214/de4ddf80dc044dc49584c2b66051474e.mp4', '2022-05-03 08:33:42', '2022-05-12 12:58:27', NULL);
INSERT INTO `legion_list` VALUES (30, '俄罗斯战机在黑海伴飞法国军机', 'CCTV', 'http://www.news.cn/video/titlepic/112814/1128145211_1639009069735_title0h.gif', 'http://www.news.cn/video/titlepic/112814/1128145211_1639009069735_title0h.gif', 4876, 5861, 6987, 'https://vodpub1.v.news.cn/original/20211209/20dc532ebc3c4e86982915112daeaae5.mp4', '2022-05-04 12:57:49', '2022-05-05 12:57:52', NULL);
INSERT INTO `legion_list` VALUES (31, '海军：歼-15舰载战斗机首次成功着舰航母九周年', 'CCTV', 'http://www.news.cn/video/titlepic/121145/1211459355_1637718647097_title0h.jpg', 'http://www.news.cn/video/titlepic/121145/1211459355_1637718647097_title0h.jpg', 4891, 4861, 6873, 'https://vodpub1.v.news.cn/original/20211124/6898df92f90a412bbc700eb35798e541.mp4', '2022-05-05 13:00:15', '2022-05-06 13:00:18', NULL);
INSERT INTO `legion_list` VALUES (32, '陆军：直面挑战 检验新飞行员飞行能力', 'CCTV', 'http://www.news.cn/video/titlepic/121145/1211457376_1637629410883_title0h.jpg', 'http://www.news.cn/video/titlepic/121145/1211457376_1637629410883_title0h.jpg', 6571, 8694, 6741, 'https://vodpub1.v.news.cn/original/20211123/87e3a7a2dc5448f8b94b08e6523808a4.mp4', '2022-05-05 13:02:17', '2022-05-06 13:02:20', NULL);

-- ----------------------------
-- Table structure for movie_list
-- ----------------------------
DROP TABLE IF EXISTS `movie_list`;
CREATE TABLE `movie_list`  (
  `vid` int NOT NULL AUTO_INCREMENT COMMENT '视频id\r\n',
  `vtitle` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '视频标题\r\n',
  `author` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '作者姓名\r\n',
  `coverUrl` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '封面图',
  `headurl` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '作者头像url\r\n',
  `comment_num` int NULL DEFAULT NULL COMMENT '用户评论数',
  `like_num` int NULL DEFAULT NULL COMMENT '点赞数',
  `collect_num` int NULL DEFAULT NULL COMMENT '收藏数',
  `playUrl` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '视频url',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `category_id` int NULL DEFAULT NULL COMMENT '视频分类ID',
  PRIMARY KEY (`vid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 32 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of movie_list
-- ----------------------------
INSERT INTO `movie_list` VALUES (26, '漫威系列预告！', '爱奇艺', 'https://img0.baidu.com/it/u=1861153256,2690010686&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=313', 'https://img0.baidu.com/it/u=1861153256,2690010686&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=313', 235, 754, 632, 'http://vfx.mtime.cn/Video/2019/02/04/mp4/190204084208765161.mp4', '2022-05-05 12:07:07', '2022-05-05 12:07:12', NULL);
INSERT INTO `movie_list` VALUES (27, '紧急救援重磅来袭！', '爱奇艺', 'https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fupload.taihainet.com%2F2019%2F1210%2F1575967509458.jpg&refer=http%3A%2F%2Fupload.taihainet.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1654921017&t=205bac5ef56a2d6d2083f1e6be3fe156', 'https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fupload.taihainet.com%2F2019%2F1210%2F1575967509458.jpg&refer=http%3A%2F%2Fupload.taihainet.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1654921017&t=205bac5ef56a2d6d2083f1e6be3fe156', 568, 846, 349, 'http://vfx.mtime.cn/Video/2019/03/19/mp4/190319222227698228.mp4', '2022-05-12 12:17:34', '2022-05-12 12:17:34', NULL);
INSERT INTO `movie_list` VALUES (28, '迪士尼经典动画片预告！', '爱奇艺', 'https://gimg2.baidu.com/image_search/src=http%3A%2F%2Finews.gtimg.com%2Fnewsapp_match%2F0%2F11304399797%2F0.jpg&refer=http%3A%2F%2Finews.gtimg.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1654921133&t=bb842d4527efa9ab5c34ac580c79bebe', 'https://gimg2.baidu.com/image_search/src=http%3A%2F%2Finews.gtimg.com%2Fnewsapp_match%2F0%2F11304399797%2F0.jpg&refer=http%3A%2F%2Finews.gtimg.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1654921133&t=bb842d4527efa9ab5c34ac580c79bebe', 634, 549, 864, 'http://vfx.mtime.cn/Video/2019/03/19/mp4/190319212559089721.mp4', '2022-05-04 12:19:26', '2022-05-12 12:29:59', NULL);
INSERT INTO `movie_list` VALUES (29, '叶问4预告抢先观看！', '爱奇艺', 'https://img2.baidu.com/it/u=274957521,4293633327&fm=253&fmt=auto&app=138&f=JPEG?w=833&h=500', 'https://img2.baidu.com/it/u=274957521,4293633327&fm=253&fmt=auto&app=138&f=JPEG?w=833&h=500', 963, 657, 794, 'http://vfx.mtime.cn/Video/2019/03/18/mp4/190318231014076505.mp4', '2022-05-11 12:21:43', '2022-05-13 12:21:47', NULL);
INSERT INTO `movie_list` VALUES (30, '拆弹专家2预告倒计时！', '爱奇艺', 'https://gimg2.baidu.com/image_search/src=http%3A%2F%2Finews.gtimg.com%2Fnewsapp_bt%2F0%2F13199601675%2F1000.jpg&refer=http%3A%2F%2Finews.gtimg.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1654921516&t=b3ae7ab0971dd1b3d199f55da800e263', 'https://gimg2.baidu.com/image_search/src=http%3A%2F%2Finews.gtimg.com%2Fnewsapp_bt%2F0%2F13199601675%2F1000.jpg&refer=http%3A%2F%2Finews.gtimg.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1654921516&t=b3ae7ab0971dd1b3d199f55da800e263', 694, 862, 876, 'http://vfx.mtime.cn/Video/2019/03/18/mp4/190318214226685784.mp4', '2022-05-07 12:23:40', '2022-05-13 22:51:21', NULL);
INSERT INTO `movie_list` VALUES (31, '花椒之味震撼来袭！', '爱奇艺', 'https://gss0.baidu.com/70cFfyinKgQFm2e88IuM_a/baike/pic/item/c75c10385343fbf25dedf603bf7eca8064388fc9.jpg', 'https://gss0.baidu.com/70cFfyinKgQFm2e88IuM_a/baike/pic/item/c75c10385343fbf25dedf603bf7eca8064388fc9.jpg', 873, 493, 712, 'http://vfx.mtime.cn/Video/2019/03/19/mp4/190319125415785691.mp4', '2022-05-05 12:27:38', '2022-05-07 12:27:42', NULL);
INSERT INTO `movie_list` VALUES (32, '地久天长载誉公映！', '爱奇艺', 'https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fpic1.zhimg.com%2F50%2Fv2-6c2ad09e71b18a0e5f4065291405a0b4_hd.jpg&refer=http%3A%2F%2Fpic1.zhimg.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1654922199&t=e9ff0deaef969bc57fc083eb1ed7cc61', 'https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fpic1.zhimg.com%2F50%2Fv2-6c2ad09e71b18a0e5f4065291405a0b4_hd.jpg&refer=http%3A%2F%2Fpic1.zhimg.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1654922199&t=e9ff0deaef969bc57fc083eb1ed7cc61', 531, 498, 374, 'http://vfx.mtime.cn/Video/2019/03/12/mp4/190312143927981075.mp4', '2022-05-04 12:33:38', '2022-05-06 12:33:41', NULL);

-- ----------------------------
-- Table structure for my
-- ----------------------------
DROP TABLE IF EXISTS `my`;
CREATE TABLE `my`  (
  `me_id` int NOT NULL AUTO_INCREMENT,
  `title_img` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `title_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `title_author` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `read_count` int NULL DEFAULT NULL,
  `like_count` int NULL DEFAULT NULL,
  `comment_count` int NULL DEFAULT NULL,
  `enjoy_count` int NULL DEFAULT NULL,
  PRIMARY KEY (`me_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of my
-- ----------------------------
INSERT INTO `my` VALUES (1, 'https://img0.baidu.com/it/u=1942253063,3807598283&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500', '华南一把刀', '欢迎', 745, 1249, 2346, 49);

-- ----------------------------
-- Table structure for news
-- ----------------------------
DROP TABLE IF EXISTS `news`;
CREATE TABLE `news`  (
  `news_id` int NOT NULL AUTO_INCREMENT COMMENT '资讯id',
  `news_title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '资讯标题',
  `author_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '作者名',
  `header_url` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '头像url',
  `comment_count` int NULL DEFAULT NULL COMMENT '评论数',
  `release_date` date NULL DEFAULT NULL COMMENT '发布日期',
  `type` int NULL DEFAULT NULL COMMENT '资讯显示类型',
  `picx` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `picxx` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `picxxx` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`news_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of news
-- ----------------------------
INSERT INTO `news` VALUES (1, '《忍者蛙》发售日公布 已上架Steam、支持简中', '3DMGAME', 'http://p1-tt.byteimg.com/large/pgc-image/S6KR5958Y5X2Qt?from=pc', 3, '2022-04-07', 1, NULL, NULL, NULL);
INSERT INTO `news` VALUES (2, '外媒爆料：育碧“阿瓦隆”项目胎死腹中，只因为他不喜欢奇幻游戏', '爱游戏的萌博士', 'http://p1-tt.byteimg.com/large/pgc-image/714415d37865444ca2bef51eb1706cda?from=pc', 1, '2022-04-18', 2, 'https://img1.baidu.com/it/u=1564864293,4121531010&fm=253&fmt=auto&app=120&f=JPEG?w=1280&h=720', 'https://img0.baidu.com/it/u=1022032268,144508792&fm=253&fmt=auto&app=138&f=JPEG?w=667&h=500', 'https://img0.baidu.com/it/u=2381597998,3049492285&fm=253&fmt=auto&app=138&f=JPEG?w=767&h=500');
INSERT INTO `news` VALUES (3, '索尼公布Ready for PlayStation 5电视阵容', '游戏时光VGtime', 'http://p1-tt.byteimg.com/large/pgc-image/33b9831739764bdb8a157efce048ec85?from=pc', 6, '2022-04-14', 3, NULL, NULL, NULL);
INSERT INTO `news` VALUES (4, '一部不受关注的互动电影佳作——解构《暴雨》', '瑾瑜游乐说', 'http://p3-tt.byteimg.com/large/pgc-image/c8a4e737b54d41c1a84722fc1c6d191d?from=pc', 12, '2022-04-15', 3, NULL, NULL, NULL);
INSERT INTO `news` VALUES (5, '《光环：无限》官方Q&A 无充值战利品，画质优化中', '聚玩社官方', 'http://p6-tt.byteimg.com/large/pgc-image/S6CLixgC4HSrXD?from=pc', 4, '2022-04-10', 2, 'https://img1.baidu.com/it/u=1280685513,4281840864&fm=253&fmt=auto&app=120&f=JPEG?w=998&h=800', 'https://img1.baidu.com/it/u=4178736649,4252625870&fm=253&fmt=auto&app=120&f=JPEG?w=1280&h=800', 'https://img1.baidu.com/it/u=453815093,1445171496&fm=253&fmt=auto&app=138&f=JPEG?w=1105&h=500');
INSERT INTO `news` VALUES (6, '2020小编个人力推的耐玩的养老游戏', '游戏我看看', 'http://p3-tt.byteimg.com/large/pgc-image/02973348d57d4dfba2d001f82caa3fcc?from=pc', 7, '2022-04-04', 1, NULL, NULL, NULL);
INSERT INTO `news` VALUES (7, 'NBA复赛赛况：开拓者加时擒灰熊，太阳胜奇才，魔术“主场”破网', '头条专题', 'http://p1-tt.byteimg.com/large/pgc-image/a456c50fff344122b1b20ed99026c3f8?from=pc', 23, '2022-03-31', 1, NULL, NULL, NULL);
INSERT INTO `news` VALUES (8, 'NBA最有含金量总冠军？奥拉朱旺95年4次以下克上！无冠军超过2次', '网罗篮球', 'https://img0.baidu.com/it/u=325674188,3280397254&fm=253&fmt=auto&app=138&f=JPEG?w=501&h=500', 45, '2022-04-20', 2, 'https://img1.baidu.com/it/u=2373420144,3478494326&fm=253&fmt=auto&app=120&f=JPEG?w=1200&h=680', 'https://img2.baidu.com/it/u=3213433091,1108082188&fm=253&fmt=auto&app=120&f=JPEG?w=1200&h=685', 'https://img2.baidu.com/it/u=1227952378,2858419246&fm=253&fmt=auto&app=120&f=JPEG?w=1024&h=768');

-- ----------------------------
-- Table structure for politics_list
-- ----------------------------
DROP TABLE IF EXISTS `politics_list`;
CREATE TABLE `politics_list`  (
  `vid` int NOT NULL AUTO_INCREMENT COMMENT '视频id\r\n',
  `vtitle` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '视频标题\r\n',
  `author` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '作者姓名\r\n',
  `coverUrl` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '封面图',
  `headurl` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '作者头像url\r\n',
  `comment_num` int NULL DEFAULT NULL COMMENT '用户评论数',
  `like_num` int NULL DEFAULT NULL COMMENT '点赞数',
  `collect_num` int NULL DEFAULT NULL COMMENT '收藏数',
  `playUrl` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '视频url',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `category_id` int NULL DEFAULT NULL COMMENT '视频分类ID',
  PRIMARY KEY (`vid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 31 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of politics_list
-- ----------------------------
INSERT INTO `politics_list` VALUES (26, '抗疫路上有你丨核酸采样突击队', '新华社', 'http://www.news.cn/politics/2022-05/11/1128641631_16522752760491n.jpg', 'http://www.news.cn/politics/2022-05/11/1128641631_16522752760491n.jpg', 6875, 3654, 4572, 'https://vodpub1.v.news.cn/yhfb-original/20220511/a1292fc6-beb3-4b86-bb22-0bac7dbe8a8c.mp4', '2022-05-12 10:49:35', '2022-05-12 10:53:44', NULL);
INSERT INTO `politics_list` VALUES (27, '英雄归来｜中国航天超级英雄大片', '新华社', 'http://www.news.cn/videopro/titlepic/112859/1128591999_1650799398429_title0h.jpg', 'http://www.news.cn/videopro/titlepic/112859/1128591999_1650799398429_title0h.jpg', 2534, 3246, 5214, 'https://vodpub1.v.news.cn/original/20220424/43edc3125e0d4c83b6439887651c3f74.mp4', '2022-05-12 11:09:33', '2022-05-12 11:09:48', NULL);
INSERT INTO `politics_list` VALUES (28, '了不起的人文：千年瓷韵', '新华社', 'http://www.news.cn/videopro/titlepic/112863/1128637138_1652169136285_title0h.png', 'http://www.news.cn/videopro/titlepic/112863/1128637138_1652169136285_title0h.png', 3654, 4215, 3241, 'https://vodpub1.v.news.cn/original/20220510/5668975047354426bcb7e46c5a317bc2.mp4', '2022-05-12 11:11:57', '2022-05-12 11:11:57', NULL);
INSERT INTO `politics_list` VALUES (29, '了不起的城市：南昌', '新华社', 'http://www.news.cn/videopro/titlepic/112860/1128600481_1651028535197_title0h.png', 'http://www.news.cn/videopro/titlepic/112860/1128600481_1651028535197_title0h.png', 6317, 3250, 2103, 'https://vodpub1.v.news.cn/original/20220427/93c6ca4e2d04431e8b6777914bdd666d.mp4', '2022-05-12 11:13:34', '2022-05-12 11:13:34', NULL);
INSERT INTO `politics_list` VALUES (30, '2021年全国一般公共预算收入突破20万亿元', '新华社', 'http://www.news.cn/fortune/2022-01/25/1128300148_16431173977091n.jpg', 'http://www.news.cn/fortune/2022-01/25/1128300148_16431173977091n.jpg', 30562, 6547, 8654, 'https://vodpub1.v.news.cn/yhfb-original/20220125/593bdc09-9f2a-470b-959b-8715c3ab3a73.mp4', '2022-05-12 11:15:51', '2022-05-12 11:15:51', NULL);
INSERT INTO `politics_list` VALUES (31, '教育部公布首批201所全国急救教育试点学校名单', '新华社', 'http://www.news.cn/video/titlepic/112823/1128236700_1641431564641_title0h.jpg', 'http://www.news.cn/video/titlepic/112823/1128236700_1641431564641_title0h.jpg', 36424, 7546, 6320, 'https://vodpub1.v.news.cn/original/20220126/9d97b217027149279f481fb81ecf62c6.mp4', '2022-05-12 11:18:53', '2022-05-12 11:18:53', NULL);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `user_id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '密码',
  `salt` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '盐',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机号',
  `status` tinyint NULL DEFAULT NULL COMMENT '状态  0：禁用   1：正常',
  `create_user_id` bigint NULL DEFAULT NULL COMMENT '创建者ID',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统用户' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'admin', '9ec9750e709431dad22365cabc5c625482e574c74adaebba7dd02f1129e4ce1d', 'YzcmCZNvbXocrsz9dm8e', 'root@renren.io', '13612345678', 1, 1, '2016-11-11 11:11:11');
INSERT INTO `sys_user` VALUES (4, 'user', 'c6db632acaff993431124f792982b3a84ddb67b12856adc314954a45d486795d', 'aH1XLPH0wBuZq2kl2Pas', 'abc@123.com', '18371458987', 1, 1, '2020-07-19 18:02:30');
INSERT INTO `sys_user` VALUES (5, 'super', '115dc8bb37d0925b7e6005c3081d58fa49b74d0ee0d0df98cb18aadef5023274', 'N48VinVrrKjUkdYztiJe', '123@qq.com', '18371458526', 1, 1, '2020-07-19 18:03:35');

-- ----------------------------
-- Table structure for tb_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user`  (
  `user_id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机号',
  `password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '密码',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 135 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tb_user
-- ----------------------------
INSERT INTO `tb_user` VALUES (43, 'penghang', '166 7094 0313', '7C89273267E137BFDABD2F8524A7DC8E', '2022-08-02 14:46:14');
INSERT INTO `tb_user` VALUES (47, 'testAdmin', '191 1870 7857', '7C89273267E137BFDABD2F8524A7DC8E', '2022-08-01 18:55:17');
INSERT INTO `tb_user` VALUES (139, 'pengqian', '166 7094 0313', 'DF1B3F5C412874E3C04897279B0B2F93', '2022-08-02 14:21:37');
INSERT INTO `tb_user` VALUES (141, 'pengyong', '185 2012 7270', '28BD3EF011657C10DF496DCDB465AF67', '2022-08-02 14:26:00');
INSERT INTO `tb_user` VALUES (143, 'penghaining', '151 1687 2027', '2078D35EB4E1F462E5FC5F0ADA47AA9F', '2022-08-02 14:31:23');

-- ----------------------------
-- Table structure for video_category
-- ----------------------------
DROP TABLE IF EXISTS `video_category`;
CREATE TABLE `video_category`  (
  `category_id` int NOT NULL AUTO_INCREMENT,
  `category_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`category_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of video_category
-- ----------------------------
INSERT INTO `video_category` VALUES (1, '游戏');
INSERT INTO `video_category` VALUES (2, '音乐');
INSERT INTO `video_category` VALUES (3, '美食');
INSERT INTO `video_category` VALUES (4, '农人');
INSERT INTO `video_category` VALUES (5, 'vlog');
INSERT INTO `video_category` VALUES (6, '搞笑');
INSERT INTO `video_category` VALUES (7, '宠物');
INSERT INTO `video_category` VALUES (8, '军事');

-- ----------------------------
-- Table structure for video_list
-- ----------------------------
DROP TABLE IF EXISTS `video_list`;
CREATE TABLE `video_list`  (
  `vid` int NOT NULL AUTO_INCREMENT COMMENT '视频id\r\n',
  `vtitle` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '视频标题\r\n',
  `author` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '作者姓名\r\n',
  `coverUrl` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '封面图',
  `headurl` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '作者头像url\r\n',
  `comment_num` int NULL DEFAULT NULL COMMENT '用户评论数',
  `like_num` int NULL DEFAULT NULL COMMENT '点赞数',
  `collect_num` int NULL DEFAULT NULL COMMENT '收藏数',
  `playUrl` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '视频url',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `category_id` int NULL DEFAULT NULL COMMENT '视频分类ID',
  PRIMARY KEY (`vid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 26 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of video_list
-- ----------------------------
INSERT INTO `video_list` VALUES (1, '江南第二深情', '哎！绿帽小孩', 'https://img1.baidu.com/it/u=3715687718,3093898249&fm=253&fmt=auto&app=138&f=JPEG?w=889&h=500', 'https://img0.baidu.com/it/u=1607265740,2940040876&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500', 121, 452, 356, 'http://vfx.mtime.cn/Video/2019/02/04/mp4/190204084208765161.mp4', '2020-07-14 11:21:45', '2022-05-12 08:43:09', 1);
INSERT INTO `video_list` VALUES (2, '【仁王2】视频攻略 2-3 虚幻魔城', '黑桐谷歌', 'https://lf1-xgcdn-tos.pstatp.com/img/tos-cn-p-0000/9ff7fe6c89e44ca3a22aad5744e569e3~tplv-crop-center:1041:582.jpg', 'https://img0.baidu.com/it/u=2787351444,4184612003&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500', 1300, 500, 120, 'http://vfx.mtime.cn/Video/2019/03/21/mp4/190321153853126488.mp4', NULL, '2022-04-14 08:35:48', 1);
INSERT INTO `video_list` VALUES (3, '最猛暴击吕布教学，这才是战神该有的样子', '小凡解说游戏', 'https://sf1-xgcdn-tos.pstatp.com/img/tos-cn-i-0004/83cc11d5e26047c6b0ead149f41a8266~tplv-crop-center:1041:582.jpg', 'https://img2.baidu.com/it/u=2736658488,3554866980&fm=253&fmt=auto&app=138&f=JPEG?w=606&h=451', 10, 19, 5, 'http://vfx.mtime.cn/Video/2019/03/19/mp4/190319222227698228.mp4', NULL, '2022-04-14 08:37:04', 1);
INSERT INTO `video_list` VALUES (4, '拳皇14：小孩输掉一分，印尼选手得意忘形', 'E游未尽小E', 'https://img1.baidu.com/it/u=2326497221,3706334082&fm=253&fmt=auto&app=138&f=JPEG?w=889&h=500', 'https://img1.baidu.com/it/u=2037962233,1674376593&fm=253&fmt=auto&app=138&f=JPEG?w=800&h=500', 22, 180, 963, 'http://vfx.mtime.cn/Video/2019/03/19/mp4/190319212559089721.mp4', NULL, '2022-04-14 10:04:57', 1);
INSERT INTO `video_list` VALUES (5, '阿远花210块买了条20斤的鲅鱼', '食味阿远', 'https://img0.baidu.com/it/u=803715582,2061704496&fm=253&fmt=auto&app=138&f=JPEG?w=889&h=500', 'https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F2020-07-22%2F5f17fe80ceaae.jpg&refer=http%3A%2F%2Fpic1.win4000.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1652494593&t=41c37d80ccd764d92c3bd79c167fd197', 36, 3, 56, 'http://vfx.mtime.cn/Video/2019/03/18/mp4/190318231014076505.mp4', NULL, '2022-04-14 12:28:12', 3);
INSERT INTO `video_list` VALUES (6, '10斤用新鲜牛腿肉分享', '美食作家王刚', 'https://img2.baidu.com/it/u=1733010819,1688714629&fm=253&fmt=auto&app=138&f=JPEG?w=1068&h=500', 'https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2Fd%2F59819820bd89e.jpg&refer=http%3A%2F%2Fpic1.win4000.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1652494593&t=2ebf39e44ed064d7685001821d7009ea', 96, 700, 89, 'http://vfx.mtime.cn/Video/2019/03/18/mp4/190318214226685784.mp4', NULL, '2022-04-14 12:30:23', 3);
INSERT INTO `video_list` VALUES (7, '面条这样吃才叫爽，放两斤花甲一拌', '山药视频', 'https://img2.baidu.com/it/u=2037073180,1444939679&fm=253&fmt=auto&app=120&f=JPEG?w=889&h=500', 'https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F2020-03-25%2F5e7afbdf8bf46.jpg&refer=http%3A%2F%2Fpic1.win4000.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1652494593&t=ce2f40e97ab4ea35b467fc60ed2e6fc9', 9, 56, 123, 'http://vfx.mtime.cn/Video/2019/03/19/mp4/190319104618910544.mp4', NULL, '2022-04-27 15:07:32', 3);
INSERT INTO `video_list` VALUES (8, '2320买2只蓝色龙虾，一只清蒸，一只刺身', '半吨先生', 'https://img2.baidu.com/it/u=2037073180,1444939679&fm=253&fmt=auto&app=120&f=JPEG?w=889&h=500', 'https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2Fa%2F58e4514a3f736.jpg%3Fdown&refer=http%3A%2F%2Fpic1.win4000.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1652494593&t=e3f0677e58371e349a1d92de2c0e17b0', 98, 546, 23, 'http://vfx.mtime.cn/Video/2019/03/19/mp4/190319125415785691.mp4', NULL, '2022-04-14 10:16:50', 3);
INSERT INTO `video_list` VALUES (9, '122块钱买了一大堆海螺，想试试', '韩小浪', 'https://img2.baidu.com/it/u=3144063223,1481098528&fm=253&fmt=auto&app=138&f=JPEG?w=890&h=500', 'https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F2019-02-20%2F5c6d07f411ee1.jpg&refer=http%3A%2F%2Fpic1.win4000.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1652494473&t=e7a9238fce50b7040900a91a2886e291', 156, 56, 856, 'http://vfx.mtime.cn/Video/2019/03/17/mp4/190317150237409904.mp4', NULL, '2022-04-14 12:31:03', 3);
INSERT INTO `video_list` VALUES (10, '10块钱的大鲍鱼随便搞50个来烧烤', '阿壮锅', 'https://img2.baidu.com/it/u=2464028379,4202550055&fm=253&fmt=auto&app=120&f=JPEG?w=889&h=500', 'https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F4%2F5708c735e9f1d.jpg%3Fdown&refer=http%3A%2F%2Fpic1.win4000.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1652494473&t=b7fb0d685b106ad07eb83a8e8ef07a86', 85, 4566, 100, 'http://vfx.mtime.cn/Video/2019/03/14/mp4/190314102306987969.mp4', NULL, '2022-04-17 20:02:52', 3);
INSERT INTO `video_list` VALUES (11, '萨德：有钱学森弹道就可以“为所欲为”么', '军武次位面', 'https://p3-xg.byteimg.com/img/tos-cn-i-0004/bd1c46a6e99a491cab93ae359df1a287~tplv-crop-center:1041:582.jpg', 'https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F2019-10-23%2F5daff400ea316.jpg&refer=http%3A%2F%2Fpic1.win4000.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1652494472&t=5ad0fb37861f7ede1fcecf7c31b1dfff', 123, 500, 320, 'http://vfx.mtime.cn/Video/2019/03/13/mp4/190313094901111138.mp4', '2020-07-19 16:05:38', '2022-04-17 20:03:21', 8);
INSERT INTO `video_list` VALUES (12, '美舰趁火打劫再闯南海，王洪光将军称“以其人之道还治其人之身”', '火星方阵', 'https://p1-xg.byteimg.com/img/tos-cn-i-0004/9a50e691dd2646d6983ccebb93607033~tplv-crop-center:1041:582.jpg', 'https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2Ff%2F58a3b640afe7b.jpg%3Fdown&refer=http%3A%2F%2Fpic1.win4000.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1652494348&t=8f5d1fa221b215879d006e415413e949', 12, 343, 78, 'http://vfx.mtime.cn/Video/2019/03/12/mp4/190312143927981075.mp4', '2020-07-19 16:05:38', '2022-04-17 20:03:46', 8);
INSERT INTO `video_list` VALUES (13, 'F-22偷袭能力超强，被中国王牌雷达牢牢锁定，不敢造次', '军事观察员东旭', 'https://p9-xg.byteimg.com/img/tos-cn-i-0004/e6750544a3ee4f8182c984949f966bc2~tplv-crop-center:1041:582.jpg', 'https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2Fd%2F58f845a464399.jpg%3Fdown&refer=http%3A%2F%2Fpic1.win4000.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1652494348&t=503db11969a811a5d3ddd1c764268d04', 543, 423, 22, 'http://vfx.mtime.cn/Video/2019/03/12/mp4/190312083533415853.mp4', '2020-07-19 16:05:38', '2022-04-17 20:04:02', 8);
INSERT INTO `video_list` VALUES (14, '绝美“白天鹅”，俄罗斯镇国重器，近距离感受下图-160战略轰炸机', 'YiTube', 'https://p9-xg.byteimg.com/img/tos-cn-i-0004/1a9fd82c375d4124bd860d253ca1d502~tplv-crop-center:1041:582.jpg', 'https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F0%2F56822f38a8db9.jpg%3Fdown&refer=http%3A%2F%2Fpic1.win4000.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1652494348&t=9abd7df331b33a4f2fc53f0907199f94', 654, 234, 466, 'http://vfx.mtime.cn/Video/2019/03/14/mp4/190314223540373995.mp4', '2020-07-19 16:05:38', '2022-04-14 10:13:33', 8);
INSERT INTO `video_list` VALUES (15, '中国新歌声：男子开口唱得太奇怪！', '灿星音乐现场', 'https://p9-xg.byteimg.com/img/tos-cn-i-0004/19c44751e9124b069d23cddbc46e29fb~tplv-crop-center:1041:582.jpg', 'https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg2.niutuku.com%2Fdesk%2F1208%2F1725%2Fntk-1725-68487.jpg&refer=http%3A%2F%2Fimg2.niutuku.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1652494348&t=eead6dec8a9dd625da2e8eb3a5188b42', 12, 45, 6, 'http://vfx.mtime.cn/Video/2019/03/14/mp4/190314223540373995.mp4', '2020-07-19 16:05:38', '2022-04-14 10:13:21', 2);
INSERT INTO `video_list` VALUES (16, '拇指琴演奏《琅琊榜》插曲《红颜旧》琴声动人', '比三呆', 'https://p1-xg.byteimg.com/img/tos-cn-p-0000/527b08d0f31d4705a4d8f4a72120948c~tplv-crop-center:1041:582.jpg', 'https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F9%2F59326293e0ce8.jpg%3Fdown&refer=http%3A%2F%2Fpic1.win4000.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1652494348&t=a930a4923bdd2a6930a7c84afaabc595', 34, 456, 123, 'http://vfx.mtime.cn/Video/2019/03/14/mp4/190314223540373995.mp4', '2020-07-19 16:05:38', '2022-04-14 10:13:12', 2);
INSERT INTO `video_list` VALUES (17, '陈志朋台上唱《大田后生仔》', '下饭音乐', 'https://p3-xg.byteimg.com/img/tos-cn-i-0004/1820c36d7a3846acaca9c24f18b01944~tplv-crop-center:1041:582.jpg', 'https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F2018-06-08%2F5b1a28021a60c.jpg&refer=http%3A%2F%2Fpic1.win4000.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1652494348&t=5a2333af6f7f43a9a8ba1197783c11ec', 76, 47, 768, 'http://vfx.mtime.cn/Video/2019/03/14/mp4/190314223540373995.mp4', '2020-07-19 16:05:38', '2022-04-14 10:13:03', 2);
INSERT INTO `video_list` VALUES (18, '龚喜水库下网偶遇大鱼群，收网过程惊心动魄', '游钓寻鱼之路', 'https://p3-xg.byteimg.com/img/tos-cn-p-0026/a225869a56d1715823d6f74d6a765b01~tplv-crop-center:1041:582.jpg', 'https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F2019-08-26%2F5d63a0b14ad1c.jpg&refer=http%3A%2F%2Fpic1.win4000.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1652494348&t=e96e83c6a2d599eae9baa688b669eede', 43, 46, 78, 'http://vfx.mtime.cn/Video/2019/03/14/mp4/190314223540373995.mp4', '2020-07-19 22:39:15', '2022-04-14 10:12:54', 4);
INSERT INTO `video_list` VALUES (19, '小登父女第一次吃香蕉花没经验以为是大苞米', '麦小登', 'https://p3-xg.byteimg.com/img/tos-cn-i-0004/36f0b389b3d44d5dbd60590a0adf8c2a~tplv-crop-center:1041:582.jpg', 'https://img0.baidu.com/it/u=1770675731,2717084640&fm=253&fmt=auto&app=120&f=JPEG?w=1422&h=800', 65, 66, 567, 'http://vfx.mtime.cn/Video/2019/03/14/mp4/190314223540373995.mp4', '2020-07-19 16:05:38', '2022-04-14 10:12:37', 5);
INSERT INTO `video_list` VALUES (20, '管它什么狂风暴雨昼夜不停,躲在房车里炖牛肉', '旗开得胜号', 'https://p3-xg.byteimg.com/img/tos-cn-i-0004/55386236bbf74f5794251a24fba85ef1~tplv-crop-center:1041:582.jpg', 'https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2Fa%2F574d56297e96c.jpg%3Fdown&refer=http%3A%2F%2Fpic1.win4000.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1652494207&t=c683e839d635b95f32e3f0531ac9ec34', 776, 67, 23, 'http://vfx.mtime.cn/Video/2019/03/14/mp4/190314223540373995.mp4', '2020-07-19 16:05:38', '2022-04-14 10:10:35', 5);
INSERT INTO `video_list` VALUES (21, '2020年的旅行计划：预算花25万去南极旅行', '麦小兜开车去非洲', 'https://p9-xg.byteimg.com/img/tos-cn-i-0004/7c09b805c10e44469edfb76eaf7b666b~tplv-crop-center:1041:582.jpg', 'https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F2020-03-24%2F5e79acf3a2830.jpg&refer=http%3A%2F%2Fpic1.win4000.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1652494119&t=0f1cdb5280a77131806275522c95b403', 43, 45, 1123, 'http://vfx.mtime.cn/Video/2019/03/14/mp4/190314223540373995.mp4', '2020-07-19 16:05:38', '2022-04-14 10:08:59', 5);
INSERT INTO `video_list` VALUES (22, '云南咖啡进军美国第一步：纽约最好烘焙厂愿意合作吗？', '我是郭杰瑞', 'https://p1-xg.byteimg.com/img/tos-cn-i-0004/4a482126d41c4da49c3baaa5ea65b0f6~tplv-crop-center:1041:582.jpg', 'https://img2.baidu.com/it/u=3416250246,304385782&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=281', 43, 654, 21, 'http://vfx.mtime.cn/Video/2019/03/14/mp4/190314223540373995.mp4', '2020-07-19 16:05:38', '2022-04-14 10:08:49', 5);
INSERT INTO `video_list` VALUES (23, '二货当憨头面炫耀家庭地位，谁料事后认怂惨遭打脸', '爆笑三江锅', 'https://p1-xg.byteimg.com/img/tos-cn-i-0004/975b48746c584e79b77df1a43531d4bf~tplv-crop-center:1041:582.jpg', 'https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F2019-12-31%2F5e0b05804e10b.jpg&refer=http%3A%2F%2Fpic1.win4000.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1652493791&t=e98755811e97c52cf54d16374d76b13e', 56, 3435, 74, 'http://vfx.mtime.cn/Video/2019/03/14/mp4/190314223540373995.mp4', '2020-07-19 16:05:38', '2022-04-14 10:07:41', 6);
INSERT INTO `video_list` VALUES (24, '大年初一有家人在身边，最好的朋友在对门！', '陈翔六点半', 'https://p1-xg.byteimg.com/img/tos-cn-p-0026/71b6b37e67a05c3103de521bcc1bd8cc~tplv-crop-center:1041:582.jpg', 'https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F2020-06-04%2F5ed8a4d5881c6.jpg&refer=http%3A%2F%2Fpic1.win4000.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1652493791&t=db3ac8d263c81779c41ccd0154842e65', 45, 234, 567, 'http://vfx.mtime.cn/Video/2019/03/14/mp4/190314223540373995.mp4', '2020-07-19 16:05:38', '2022-04-14 10:07:27', 6);
INSERT INTO `video_list` VALUES (25, '猫一见陌生人就跑，靠近就发抖', '肉蛋儿有个喵', 'https://p3-xg.byteimg.com/img/tos-cn-i-0004/eadd7aebf3174fa3b793acd310d2549a~tplv-crop-center:1041:582.jpg', 'https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fp2.itc.cn%2Fimages01%2F20210619%2F7494b58b284442f0b9d6ad26670d5f45.png&refer=http%3A%2F%2Fp2.itc.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1652493791&t=eb248af4ba7d7bd5576505e1555c7e90', 45, 234, 6, 'http://vfx.mtime.cn/Video/2019/03/14/mp4/190314223540373995.mp4', '2020-07-19 16:05:38', '2022-04-14 10:05:31', 7);
INSERT INTO `video_list` VALUES (26, '网红猫咪精修图vs刚睡醒，还真是两副面孔', 'papi家的大小咪', 'https://p1-xg.byteimg.com/img/tos-cn-i-0004/a2165f779651487c94b27233d162c3dc~tplv-crop-center:1041:582.jpg', 'https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F2020-07-10%2F5f08277eb034a.jpg&refer=http%3A%2F%2Fpic1.win4000.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1652493791&t=934c198bcbe5a1a2c6884a91c0b1d1e7', 211, 98, 345, 'http://vfx.mtime.cn/Video/2019/03/14/mp4/190314223540373995.mp4', '2020-07-19 16:05:38', '2022-05-12 08:27:21', 7);

SET FOREIGN_KEY_CHECKS = 1;
