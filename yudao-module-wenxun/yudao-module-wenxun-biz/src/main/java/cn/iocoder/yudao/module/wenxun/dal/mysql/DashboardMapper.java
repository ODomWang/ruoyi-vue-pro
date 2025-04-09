package cn.iocoder.yudao.module.wenxun.dal.mysql;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 数字大屏 Mapper
 */
@Mapper
public interface DashboardMapper {

    /**
     * 获取文档总数
     * 
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 文档总数
     */
    @Select("SELECT COUNT(*) FROM wenxun_spider_crawl_detail WHERE create_time BETWEEN #{startTime} AND #{endTime} AND deleted = 0")
    Integer getTotalDocuments(@Param("startTime") String startTime, @Param("endTime") String endTime);
    
    /**
     * 获取错误文档总数
     * 
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 错误文档总数
     */
    @Select("SELECT COUNT(DISTINCT spider_id) FROM wenxun_detail_check_info WHERE create_time BETWEEN #{startTime} AND #{endTime} AND deleted = 0")
    Integer getDocumentsWithErrors(@Param("startTime") String startTime, @Param("endTime") String endTime);
    
    /**
     * 获取错误总数
     * 
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 错误总数
     */
    @Select("SELECT COUNT(*) FROM wenxun_detail_check_info WHERE create_time BETWEEN #{startTime} AND #{endTime} AND deleted = 0")
    Integer getTotalErrors(@Param("startTime") String startTime, @Param("endTime") String endTime);
    
    /**
     * 获取监测网站总数
     * 
     * @return 监测网站总数
     */
    @Select("SELECT COUNT(*) FROM wenxun_spider_source_config WHERE deleted = 0")
    Integer getMonitoredSites();
    
    /**
     * 获取网站状态数据
     * 
     * @return 网站状态数据列表
     */
    @Select("SELECT s.id, s.spider_name as name, s.spider_url as url, " +
            "CASE WHEN p.status = 1 THEN 'online' ELSE 'offline' END as status, " +
            "p.update_time as lastPingTime " +
            "FROM wenxun_spider_source_config s " +
            "LEFT JOIN wenxun_url_ping_info p ON s.spider_url = p.url " +
            "WHERE s.deleted = 0")
    List<Map<String, Object>> getSiteStatus();
    
    /**
     * 获取文档趋势数据
     * 
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param groupBy 分组方式(day, week, month)
     * @return 文档趋势数据
     */
    @Select("<script>" +
            "SELECT " +
            "<if test=\"groupBy == 'day'\">DATE_FORMAT(create_time, '%Y-%m-%d')</if>" +
            "<if test=\"groupBy == 'week'\">DATE_FORMAT(create_time, '%Y-%u')</if>" +
            "<if test=\"groupBy == 'month'\">DATE_FORMAT(create_time, '%Y-%m')</if>" +
            " as date, COUNT(*) as count " +
            "FROM wenxun_spider_crawl_detail " +
            "WHERE create_time BETWEEN #{startTime} AND #{endTime} AND deleted = 0 " +
            "GROUP BY date ORDER BY date" +
            "</script>")
    List<Map<String, Object>> getDocumentTrend(@Param("startTime") String startTime, 
                                              @Param("endTime") String endTime,
                                              @Param("groupBy") String groupBy);
    
    /**
     * 获取错误率趋势数据
     * 
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param groupBy 分组方式(day, week, month)
     * @return 错误率趋势数据
     */
    @Select("<script>" +
            "SELECT t.date, IFNULL(e.error_count, 0) as error_count, t.total_count, " +
            "ROUND(IFNULL(e.error_count, 0) / t.total_count * 100, 2) as error_rate FROM " +
            "(SELECT " +
            "<if test=\"groupBy == 'day'\">DATE_FORMAT(create_time, '%Y-%m-%d')</if>" +
            "<if test=\"groupBy == 'week'\">DATE_FORMAT(create_time, '%Y-%u')</if>" +
            "<if test=\"groupBy == 'month'\">DATE_FORMAT(create_time, '%Y-%m')</if>" +
            " as date, COUNT(*) as total_count " +
            "FROM wenxun_spider_crawl_detail " +
            "WHERE create_time BETWEEN #{startTime} AND #{endTime} AND deleted = 0 " +
            "GROUP BY date) t " +
            "LEFT JOIN " +
            "(SELECT " +
            "<if test=\"groupBy == 'day'\">DATE_FORMAT(create_time, '%Y-%m-%d')</if>" +
            "<if test=\"groupBy == 'week'\">DATE_FORMAT(create_time, '%Y-%u')</if>" +
            "<if test=\"groupBy == 'month'\">DATE_FORMAT(create_time, '%Y-%m')</if>" +
            " as date, COUNT(DISTINCT spider_id) as error_count " +
            "FROM wenxun_detail_check_info " +
            "WHERE create_time BETWEEN #{startTime} AND #{endTime} AND deleted = 0 " +
            "GROUP BY date) e ON t.date = e.date " +
            "ORDER BY t.date" +
            "</script>")
    List<Map<String, Object>> getErrorRateTrend(@Param("startTime") String startTime, 
                                               @Param("endTime") String endTime,
                                               @Param("groupBy") String groupBy);
    
    /**
     * 获取网站监测详情
     * 
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param siteId 网站ID
     * @return 网站监测详情列表
     */
    @Select("<script>" +
            "SELECT s.id, s.spider_name as name, s.spider_url as url, " +
            "COUNT(d.id) as documentCount, " +
            "ROUND(SUM(CASE WHEN p.status = 1 THEN 1 ELSE 0 END) / COUNT(p.id) * 100, 2) as successRate, " +
            "AVG(p.ping_code) / 1000 as avgResponseTime " +
            "FROM wenxun_spider_source_config s " +
            "LEFT JOIN wenxun_spider_crawl_detail d ON s.id = d.spider_config_id AND d.create_time BETWEEN #{startTime} AND #{endTime} AND d.deleted = 0 " +
            "LEFT JOIN wenxun_url_ping_log p ON s.spider_url = p.url AND p.create_time BETWEEN #{startTime} AND #{endTime} " +
            "<if test=\"siteId != null\">" +
            "WHERE s.id = #{siteId} AND s.deleted = 0 " +
            "</if>" +
            "<if test=\"siteId == null\">" +
            "WHERE s.deleted = 0 " +
            "</if>" +
            "GROUP BY s.id, s.spider_name, s.spider_url" +
            "</script>")
    List<Map<String, Object>> getSitesMonitoring(@Param("startTime") String startTime, 
                                                @Param("endTime") String endTime,
                                                @Param("siteId") Integer siteId);
    
    /**
     * 获取文档分布数据
     * 
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 文档分布数据
     */
    @Select("SELECT s.spider_name as name, COUNT(d.id) as count " +
            "FROM wenxun_spider_source_config s " +
            "LEFT JOIN wenxun_spider_crawl_detail d ON s.id = d.spider_config_id AND d.create_time BETWEEN #{startTime} AND #{endTime} AND d.deleted = 0 " +
            "WHERE s.deleted = 0 " +
            "GROUP BY s.id, s.spider_name")
    List<Map<String, Object>> getDocumentDistribution(@Param("startTime") String startTime, @Param("endTime") String endTime);
    
    /**
     * 获取网站可用性趋势
     * 
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param groupBy 分组方式(day, week, month)
     * @return 网站可用性趋势数据
     */
    @Select("<script>" +
            "SELECT " +
            "<if test=\"groupBy == 'day'\">DATE_FORMAT(create_time, '%Y-%m-%d')</if>" +
            "<if test=\"groupBy == 'week'\">DATE_FORMAT(create_time, '%Y-%u')</if>" +
            "<if test=\"groupBy == 'month'\">DATE_FORMAT(create_time, '%Y-%m')</if>" +
            " as date, ROUND(SUM(CASE WHEN status = 1 THEN 1 ELSE 0 END) / COUNT(*) * 100, 2) as rate " +
            "FROM wenxun_url_ping_log " +
            "WHERE create_time BETWEEN #{startTime} AND #{endTime} " +
            "GROUP BY date ORDER BY date" +
            "</script>")
    List<Map<String, Object>> getAvailabilityTrend(@Param("startTime") String startTime, 
                                                  @Param("endTime") String endTime,
                                                  @Param("groupBy") String groupBy);
    
    /**
     * 获取错误类型分布
     * 
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param deptId 部门ID
     * @return 错误类型分布数据
     */
    @Select("<script>" +
            "SELECT " +
            "  JSON_UNQUOTE(JSON_EXTRACT(t.wrongWordType, '$')) as type, " +
            "  COUNT(*) as count " +
            "FROM (" +
            "  SELECT " +
            "    JSON_EXTRACT(c.target_detail, '$[*].wrongWordType') as wrongWordTypes " +
            "  FROM wenxun_detail_check_info c " +
            "  LEFT JOIN wenxun_spider_crawl_detail d ON c.spider_id = d.id " +
            "  WHERE c.create_time BETWEEN #{startTime} AND #{endTime} " +
            "  AND c.deleted = 0 " +
            "  <if test=\"deptId != null\">" +
            "  AND d.dept_id = #{deptId} " +
            "  </if>" +
            ") s, " +
            "JSON_TABLE(s.wrongWordTypes, '$[*]' COLUMNS(wrongWordType VARCHAR(255) PATH '$')) t " +
            "GROUP BY t.wrongWordType" +
            "</script>")
    List<Map<String, Object>> getErrorTypeDistribution(@Param("startTime") String startTime, 
                                                     @Param("endTime") String endTime,
                                                     @Param("deptId") Long deptId);
    
    /**
     * 获取常见错误Top10
     * 
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param deptId 部门ID
     * @return 常见错误数据
     */
    @Select("<script>" +
            "SELECT " +
            "  JSON_UNQUOTE(JSON_EXTRACT(t.wrongWord, '$')) as wrongWord, " +
            "  JSON_UNQUOTE(JSON_EXTRACT(t.rightWord, '$')) as rightWord, " +
            "  COUNT(*) as count " +
            "FROM (" +
            "  SELECT " +
            "    JSON_EXTRACT(c.target_detail, '$[*].wrongWord') as wrongWords, " +
            "    JSON_EXTRACT(c.target_detail, '$[*].rightWord') as rightWords " +
            "  FROM wenxun_detail_check_info c " +
            "  LEFT JOIN wenxun_spider_crawl_detail d ON c.spider_id = d.id " +
            "  WHERE c.create_time BETWEEN #{startTime} AND #{endTime} " +
            "  AND c.deleted = 0 " +
            "  <if test=\"deptId != null\">" +
            "  AND d.dept_id = #{deptId} " +
            "  </if>" +
            ") s, " +
            "JSON_TABLE(s.wrongWords, '$[*]' COLUMNS(wrongWord VARCHAR(255) PATH '$')) t, " +
            "JSON_TABLE(s.rightWords, '$[*]' COLUMNS(rightWord VARCHAR(255) PATH '$')) r " +
            "WHERE t.wrongWord IS NOT NULL AND r.rightWord IS NOT NULL " +
            "GROUP BY wrongWord, rightWord " +
            "ORDER BY count DESC " +
            "LIMIT 10" +
            "</script>")
    List<Map<String, Object>> getCommonErrors(@Param("startTime") String startTime, 
                                            @Param("endTime") String endTime,
                                            @Param("deptId") Long deptId);
    
    /**
     * 获取错误示例
     * 
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param deptId 部门ID
     * @return 错误示例数据
     */
    @Select("<script>" +
            "SELECT " +
            "  c.id, " +
            "  JSON_UNQUOTE(JSON_EXTRACT(c.target_detail, '$[0].wrongWord')) as wrongWord, " +
            "  JSON_UNQUOTE(JSON_EXTRACT(c.target_detail, '$[0].rightWord')) as rightWord, " +
            "  CONCAT('...', SUBSTRING(c.check_detail, GREATEST(1, LOCATE(JSON_UNQUOTE(JSON_EXTRACT(c.target_detail, '$[0].wrongWord')), c.check_detail) - 30), 60), '...') as context, " +
            "  d.title as documentTitle " +
            "FROM wenxun_detail_check_info c " +
            "LEFT JOIN wenxun_spider_crawl_detail d ON c.spider_id = d.id " +
            "WHERE c.create_time BETWEEN #{startTime} AND #{endTime} " +
            "AND c.deleted = 0 " +
            "<if test=\"deptId != null\">" +
            "AND d.dept_id = #{deptId} " +
            "</if>" +
            "LIMIT 5" +
            "</script>")
    List<Map<String, Object>> getErrorExamples(@Param("startTime") String startTime, 
                                             @Param("endTime") String endTime,
                                             @Param("deptId") Long deptId);
    
    /**
     * 获取部门工作情况
     * 
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 部门工作情况数据
     */
    @Select("SELECT " +
            "  d.dept_id as id, " +
            "  CASE " +
            "    WHEN d.dept_id = 103 THEN '连云港师范学院' " +
            "    WHEN d.dept_id = 115 THEN '中国共产党新闻部门' " +
            "    WHEN d.dept_id = 121 THEN '测试部门' " +
            "    ELSE CONCAT('部门', d.dept_id) " +
            "  END as name, " +
            "  COUNT(d.id) as documentCount, " +
            "  COUNT(e.id) as errorCount, " +
            "  ROUND(COUNT(e.id) / COUNT(d.id) * 100, 2) as errorRate, " +
            "  ROUND(AVG(TIMESTAMPDIFF(HOUR, d.create_time, e.create_time)), 1) as avgProcessingTime " +
            "FROM wenxun_spider_crawl_detail d " +
            "LEFT JOIN wenxun_detail_check_info e ON d.id = e.spider_id AND e.create_time BETWEEN #{startTime} AND #{endTime} AND e.deleted = 0 " +
            "WHERE d.create_time BETWEEN #{startTime} AND #{endTime} " +
            "AND d.deleted = 0 " +
            "GROUP BY d.dept_id")
    List<Map<String, Object>> getDepartmentStats(@Param("startTime") String startTime, @Param("endTime") String endTime);
    
    /**
     * 获取文本质量评分趋势
     * 
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param groupBy 分组方式(day, week, month)
     * @param siteId 网站ID
     * @return 文本质量评分趋势数据
     */
    @Select("<script>" +
            "SELECT " +
            "<if test=\"groupBy == 'day'\">DATE_FORMAT(d.create_time, '%Y-%m-%d')</if>" +
            "<if test=\"groupBy == 'week'\">DATE_FORMAT(d.create_time, '%Y-%u')</if>" +
            "<if test=\"groupBy == 'month'\">DATE_FORMAT(d.create_time, '%Y-%m')</if>" +
            " as date, " +
            "ROUND(100 - (COUNT(e.id) / COUNT(d.id) * 100), 2) as score " +
            "FROM wenxun_spider_crawl_detail d " +
            "LEFT JOIN wenxun_detail_check_info e ON d.id = e.spider_id AND e.create_time BETWEEN #{startTime} AND #{endTime} AND e.deleted = 0 " +
            "WHERE d.create_time BETWEEN #{startTime} AND #{endTime} " +
            "AND d.deleted = 0 " +
            "<if test=\"siteId != null\">" +
            "AND d.spider_config_id = #{siteId} " +
            "</if>" +
            "GROUP BY date " +
            "ORDER BY date" +
            "</script>")
    List<Map<String, Object>> getQualityScoreTrend(@Param("startTime") String startTime, 
                                                  @Param("endTime") String endTime,
                                                  @Param("groupBy") String groupBy,
                                                  @Param("siteId") Integer siteId);
    
    /**
     * 获取文本质量分布
     * 
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param siteId 网站ID
     * @return 文本质量分布数据
     */
    @Select("<script>" +
            "SELECT " +
            "  SUM(CASE WHEN errorCount = 0 THEN 1 ELSE 0 END) as excellent, " +
            "  SUM(CASE WHEN errorCount BETWEEN 1 AND 3 THEN 1 ELSE 0 END) as good, " +
            "  SUM(CASE WHEN errorCount BETWEEN 4 AND 10 THEN 1 ELSE 0 END) as average, " +
            "  SUM(CASE WHEN errorCount > 10 THEN 1 ELSE 0 END) as poor " +
            "FROM (" +
            "  SELECT d.id, COUNT(e.id) as errorCount " +
            "  FROM wenxun_spider_crawl_detail d " +
            "  LEFT JOIN wenxun_detail_check_info e ON d.id = e.spider_id AND e.create_time BETWEEN #{startTime} AND #{endTime} AND e.deleted = 0 " +
            "  WHERE d.create_time BETWEEN #{startTime} AND #{endTime} " +
            "  AND d.deleted = 0 " +
            "  <if test=\"siteId != null\">" +
            "  AND d.spider_config_id = #{siteId} " +
            "  </if>" +
            "  GROUP BY d.id " +
            ") t" +
            "</script>")
    Map<String, Object> getQualityDistribution(@Param("startTime") String startTime, 
                                             @Param("endTime") String endTime,
                                             @Param("siteId") Integer siteId);
    
    /**
     * 获取优质文档示例
     * 
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param siteId 网站ID
     * @return 优质文档示例数据
     */
    @Select("<script>" +
            "SELECT d.id, d.title, " +
            "CASE " +
            "  WHEN d.spider_config_id = 3 THEN '中国共产党新闻网' " +
            "  WHEN d.spider_config_id = 4 THEN '连云港师范新闻中心' " +
            "  WHEN d.spider_config_id = 6 THEN '酸菜测试2' " +
            "  WHEN d.spider_config_id = 7 THEN '华杰双语' " +
            "  ELSE '其他来源' " +
            "END as source, " +
            "DATE_FORMAT(d.create_time, '%Y-%m-%d') as date, " +
            "100 as qualityScore " +
            "FROM wenxun_spider_crawl_detail d " +
            "LEFT JOIN wenxun_detail_check_info e ON d.id = e.spider_id " +
            "WHERE d.create_time BETWEEN #{startTime} AND #{endTime} " +
            "AND d.deleted = 0 " +
            "AND e.id IS NULL " +
            "<if test=\"siteId != null\">" +
            "AND d.spider_config_id = #{siteId} " +
            "</if>" +
            "ORDER BY d.create_time DESC " +
            "LIMIT 5" +
            "</script>")
    List<Map<String, Object>> getExemplaryDocuments(@Param("startTime") String startTime, 
                                                  @Param("endTime") String endTime,
                                                  @Param("siteId") Integer siteId);
    
    /**
     * 获取今日抓取文档数
     * 
     * @return 今日抓取文档数
     */
    @Select("SELECT COUNT(*) FROM wenxun_spider_crawl_detail WHERE DATE(create_time) = CURDATE() AND deleted = 0")
    Integer getTodayDocuments();
    
    /**
     * 获取今日发现错误数
     * 
     * @return 今日发现错误数
     */
    @Select("SELECT COUNT(*) FROM wenxun_detail_check_info WHERE DATE(create_time) = CURDATE() AND deleted = 0")
    Integer getTodayErrors();
    
    /**
     * 获取最新文档
     * 
     * @return 最新文档列表
     */
    @Select("SELECT d.id, d.title, " +
            "CASE " +
            "  WHEN d.spider_config_id = 3 THEN '中国共产党新闻网' " +
            "  WHEN d.spider_config_id = 4 THEN '连云港师范新闻中心' " +
            "  WHEN d.spider_config_id = 6 THEN '酸菜测试2' " +
            "  WHEN d.spider_config_id = 7 THEN '华杰双语' " +
            "  ELSE '其他来源' " +
            "END as source, " +
            "DATE_FORMAT(d.create_time, '%Y-%m-%d %H:%i:%s') as timestamp " +
            "FROM wenxun_spider_crawl_detail d " +
            "WHERE d.deleted = 0 " +
            "ORDER BY d.create_time DESC " +
            "LIMIT 5")
    List<Map<String, Object>> getLatestDocuments();
    
    /**
     * 获取最新错误
     * 
     * @return 最新错误列表
     */
    @Select("SELECT " +
            "  e.id, " +
            "  e.spider_id as documentId, " +
            "  d.title as documentTitle, " +
            "  JSON_UNQUOTE(JSON_EXTRACT(e.target_detail, '$[0].wrongWord')) as wrongWord, " +
            "  JSON_UNQUOTE(JSON_EXTRACT(e.target_detail, '$[0].rightWord')) as rightWord, " +
            "  DATE_FORMAT(e.create_time, '%Y-%m-%d %H:%i:%s') as timestamp " +
            "FROM wenxun_detail_check_info e " +
            "LEFT JOIN wenxun_spider_crawl_detail d ON e.spider_id = d.id " +
            "WHERE e.deleted = 0 " +
            "ORDER BY e.create_time DESC " +
            "LIMIT 5")
    List<Map<String, Object>> getLatestErrors();
    
    /**
     * 获取所有部门列表
     * 
     * @return 部门列表
     */
    @Select("SELECT DISTINCT dept_id as id, " +
            "CASE " +
            "  WHEN dept_id = 103 THEN '连云港师范学院' " +
            "  WHEN dept_id = 115 THEN '中国共产党新闻部门' " +
            "  WHEN dept_id = 121 THEN '测试部门' " +
            "  ELSE CONCAT('部门', dept_id) " +
            "END as name " +
            "FROM wenxun_spider_crawl_detail " +
            "WHERE dept_id IS NOT NULL " +
            "ORDER BY dept_id")
    List<Map<String, Object>> getAllDepartments();
    
    /**
     * 获取所有网站列表
     * 
     * @return 网站列表
     */
    @Select("SELECT id, spider_name as name FROM wenxun_spider_source_config WHERE deleted = 0 ORDER BY id")
    List<Map<String, Object>> getAllSites();
    
    /**
     * 获取所有错误类型
     * 
     * @return 错误类型列表
     */
    @Select("SELECT DISTINCT " +
            "  JSON_UNQUOTE(JSON_EXTRACT(t.wrongWordType, '$')) as type, " +
            "  CASE " +
            "    WHEN JSON_UNQUOTE(JSON_EXTRACT(t.wrongWordType, '$')) = 'wrong_word_configuration' THEN '词语使用错误' " +
            "    ELSE JSON_UNQUOTE(JSON_EXTRACT(t.wrongWordType, '$')) " +
            "  END as name " +
            "FROM wenxun_detail_check_info s, " +
            "JSON_TABLE(s.target_detail, '$[*]' COLUMNS(wrongWordType VARCHAR(255) PATH '$.wrongWordType')) t " +
            "WHERE s.deleted = 0")
    List<Map<String, Object>> getAllErrorTypes();
    
    /**
     * 获取最早记录时间
     * 
     * @return 最早记录时间
     */
    @Select("SELECT MIN(create_time) FROM wenxun_spider_crawl_detail WHERE deleted = 0")
    String getEarliestRecordTime();
    
    /**
     * 获取最晚记录时间
     * 
     * @return 最晚记录时间
     */
    @Select("SELECT MAX(create_time) FROM wenxun_spider_crawl_detail WHERE deleted = 0")
    String getLatestRecordTime();
} 