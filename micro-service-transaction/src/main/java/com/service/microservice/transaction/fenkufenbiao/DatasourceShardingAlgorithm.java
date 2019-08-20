package com.service.microservice.transaction.fenkufenbiao;

import com.dangdang.ddframe.rdb.sharding.api.ShardingValue;
import com.dangdang.ddframe.rdb.sharding.api.strategy.database.SingleKeyDatabaseShardingAlgorithm;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.*;

/**
 * @author Simon
 * @Date 2019-08-20 10:07
 * @Describe 清风拂袖揽明月, 皓月鉴怀渡银河
 */
public class DatasourceShardingAlgorithm implements SingleKeyDatabaseShardingAlgorithm<String> {

    private static Map<String, List<String>> shardingMap = Maps.newConcurrentMap();

    static {
        shardingMap.put("ds_3", Arrays.asList("上海"));
        shardingMap.put("ds_4", Arrays.asList("杭州"));
    }

    @Override
    public String doEqualSharding(Collection<String> availableTargetNames, ShardingValue<String> shardingValue) {
        for (String name : availableTargetNames){
            if (shardingMap.get(name).contains(shardingValue.getValue())){
                return name;
            }
        }
        return "ds_3";
    }

    @Override
    public Collection<String> doInSharding(Collection<String> availableTargetNames, ShardingValue<String> shardingValue) {
        LinkedHashSet<String> objects = Sets.newLinkedHashSetWithExpectedSize(availableTargetNames.size());

        for (String name : availableTargetNames){
            if (shardingMap.get(name).contains(shardingValue.getValue())){
                objects.add(name);
            } else {
                objects.add("ds_3");
            }
        }
        return objects;
    }

    @Override
    public Collection<String> doBetweenSharding(Collection<String> availableTargetNames, ShardingValue<String> shardingValue) {
        LinkedHashSet<String> objects = Sets.newLinkedHashSetWithExpectedSize(availableTargetNames.size());
        for (String name : availableTargetNames){
            if (shardingMap.get(name).contains(shardingValue.getValue())){
                objects.add(name);
            } else {
                objects.add("ds_3");
            }
        }
        return objects;
    }
}
