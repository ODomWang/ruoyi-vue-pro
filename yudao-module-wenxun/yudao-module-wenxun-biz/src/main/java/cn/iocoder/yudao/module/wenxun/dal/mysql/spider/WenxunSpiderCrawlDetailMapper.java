package cn.iocoder.yudao.module.wenxun.dal.mysql.spider;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.wenxun.dal.dataobject.spider.WenxunSpiderCrawlDetailDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Mapper
public interface WenxunSpiderCrawlDetailMapper extends BaseMapperX<WenxunSpiderCrawlDetailDO> {

    /**
     * 按时间范围和维度统计数据
     *
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param dimension 统计维度
     * @param deptIds 部门编号列表
     * @return 统计结果
     */
    @Select("<script>" +
            "SELECT " +
            "<choose>" +
            "   <when test=\"dimension == 'date'\">" +
            "       DATE_FORMAT(create_time, '%Y-%m-%d') AS dimension_key" +
            "   </when>" +
            "   <otherwise>" +
            "       ${dimension} AS dimension_key" +
            "   </otherwise>" +
            "</choose>" +
            ", COUNT(*) AS count " +
            "FROM wenxun_spider_crawl_detail " +
            "WHERE deleted = 0 " +
            "<if test=\"deptIds != null and deptIds.size() > 0\">" +
            "   AND dept_id IN <foreach collection=\"deptIds\" item=\"id\" open=\"(\" separator=\",\" close=\")\">#{id}</foreach> " +
            "</if>" +
            "<if test=\"startTime != null\">" +
            "   AND create_time >= #{startTime} " +
            "</if>" +
            "<if test=\"endTime != null\">" +
            "   AND create_time &lt;= #{endTime} " +
            "</if>" +
            "GROUP BY dimension_key " +
            "ORDER BY dimension_key" +
            "</script>")
    List<Map<String, Object>> selectCountByTimeRangeAndDimension(@Param("startTime") LocalDateTime startTime,
                                                                @Param("endTime") LocalDateTime endTime,
                                                                @Param("dimension") String dimension,
                                                                @Param("deptIds") Collection<Long> deptIds);

    /**
     * 按时间分组统计数据
     *
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param timeFormat 时间格式化表达式
     * @param deptIds 部门编号列表
     * @return 统计结果
     */
    @Select("<script>" +
            "SELECT DATE_FORMAT(create_time, #{timeFormat}) AS time_key, " +
            "COUNT(*) AS total_count, " +
            "SUM(CASE WHEN status = 0 THEN 1 ELSE 0 END) AS success_count, " +
            "SUM(CASE WHEN status != 0 THEN 1 ELSE 0 END) AS fail_count " +
            "FROM wenxun_spider_crawl_detail " +
            "WHERE deleted = 0 " +
            "<if test=\"deptIds != null and deptIds.size() > 0\">" +
            "   AND dept_id IN <foreach collection=\"deptIds\" item=\"id\" open=\"(\" separator=\",\" close=\")\">#{id}</foreach> " +
            "</if>" +
            "<if test=\"startTime != null\">" +
            "   AND create_time >= #{startTime} " +
            "</if>" +
            "<if test=\"endTime != null\">" +
            "   AND create_time &lt;= #{endTime} " +
            "</if>" +
            "GROUP BY time_key " +
            "ORDER BY time_key" +
            "</script>")
    List<Map<String, Object>> selectCountGroupByTime(@Param("startTime") LocalDateTime startTime,
                                                    @Param("endTime") LocalDateTime endTime,
                                                    @Param("timeFormat") String timeFormat,
                                                    @Param("deptIds") Collection<Long> deptIds);
} 