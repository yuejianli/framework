package top.yueshushu.learn.consumer.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.yueshushu.learn.consumer.DBDataChangeConsumer;
import top.yueshushu.learn.consumer.dto.DBDataChange;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 对表的监控
 *
 * @author yjl
 * */
@Component
@Slf4j
public class CanalUserImpl implements DBDataChangeConsumer {

    // 可以通过  @Resource 注解来引入其它的 Service 业务层

    @Override
    public String getSchemaName() {
        return "springboot";
    }

    @Override
    public String getTableName() {
        return "user";
    }

    @Override
    public void accept(List<DBDataChange> dataChanges) {
        log.info(" user 表 用户信息变动");
        for(DBDataChange dbDataChange:dataChanges){
            //只处理 Update Delete Insert 三个类型的信息即可。
            String eventType = dbDataChange.getEventType().toUpperCase();
            if(!("UPDATE".equals(eventType)||"DELETE".equals(eventType)||"INSERT".equals(eventType))){
                continue;
            }
            //只处理 改变信息数据的.
            log.info("对表 {} 进行的处理操作是:{}",getTableName(),eventType);
            // 对数据进行处理操作
            switch(eventType){
                case "UPDATE":{
                    updateHandler(dbDataChange);
                    break;
                }
                case "DELETE":{
                    deleteHandler(dbDataChange);
                    break;
                }
                case "INSERT":{
                    insertHandler(dbDataChange);
                    break;
                }
                default:{
                    break;
                }
            }
        }
    }

    /**
     * 对更新操作进行处理
     * @param dbDataChange 数据对象
     */
    private void updateHandler(DBDataChange dbDataChange) {
        List<Map<String, DBDataChange.Detail>> dataList = dbDataChange.getDataList();
        //循环数据
        for(Map<String,DBDataChange.Detail> userChangeMap:dataList){
            //对应的是信息
            Set<Map.Entry<String, DBDataChange.Detail>> entries = userChangeMap.entrySet();
            for(Map.Entry<String,DBDataChange.Detail> entry:entries){
                String column=entry.getKey();
                //获取改变的属性值
                DBDataChange.Detail value = entry.getValue();
                if (value !=null &&value.isUpdated()){
                    log.info(">>>更新字段 {},更新前的值是:{},更新后的新值是:{}",column,
                            Optional.of(value.getBefore()).map(
                                    Object::toString
                            ).orElse(null),
                            Optional.of(value.getAfter()).map(
                                    Object::toString
                            ).orElse(null));
                    //放置具体的业务处理
                }
            }
        }
    }

    /**
     * 对删除操作进行处理
     * @param dbDataChange 数据对象
     */
    private void deleteHandler(DBDataChange dbDataChange) {
        List<Map<String, DBDataChange.Detail>> dataList = dbDataChange.getDataList();
        //循环数据
        for(Map<String,DBDataChange.Detail> userChangeMap:dataList){
            //对应的是信息
            Set<Map.Entry<String, DBDataChange.Detail>> entries = userChangeMap.entrySet();
            for(Map.Entry<String,DBDataChange.Detail> entry:entries){
                String column=entry.getKey();
                //获取改变的属性值
                DBDataChange.Detail value = entry.getValue();
                if (value !=null &&value.getBefore()!=null){
                    log.info(">>>删除字段 {},删除前的值是:{}",column,value.getBefore().toString());
                    //放置具体的业务处理
                }
            }
        }
    }

    /**
     * 对插入操作进行处理
     * @param dbDataChange 数据对象
     */
    private void insertHandler(DBDataChange dbDataChange) {
        List<Map<String, DBDataChange.Detail>> dataList = dbDataChange.getDataList();
        //循环数据
        for(Map<String,DBDataChange.Detail> userChangeMap:dataList){
            //对应的是信息
            Set<Map.Entry<String, DBDataChange.Detail>> entries = userChangeMap.entrySet();
            for(Map.Entry<String,DBDataChange.Detail> entry:entries){
                String column=entry.getKey();
                //获取改变的属性值
                DBDataChange.Detail value = entry.getValue();
                if (value !=null &&value.getAfter()!=null){
                    log.info(">>>插入字段 {},插入的值是:{}",column,value.getAfter().toString());
                    //放置具体的业务处理
                }
            }
        }
    }
}
