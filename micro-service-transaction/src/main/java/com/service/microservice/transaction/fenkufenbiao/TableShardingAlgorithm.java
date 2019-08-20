package com.service.microservice.transaction.fenkufenbiao;

import com.dangdang.ddframe.rdb.sharding.api.ShardingValue;
import com.dangdang.ddframe.rdb.sharding.api.strategy.table.SingleKeyTableShardingAlgorithm;
import com.google.common.collect.Range;

import java.util.Collection;
import java.util.LinkedHashSet;

/**
 * @author Simon
 * @Date 2019-08-20 10:13
 * @Describe 清风拂袖揽明月, 皓月鉴怀渡银河
 */
public class TableShardingAlgorithm implements SingleKeyTableShardingAlgorithm<Long> {
    @Override
    public String doEqualSharding(Collection<String> availableTargetNames, ShardingValue<Long> shardingValue) {
        for (String tableName : availableTargetNames){
            System.out.println("tableName + " + tableName);
            if (tableName.endsWith(shardingValue.getValue() % 2 + "")){
                return tableName;
            }
        }
        return null;
    }

    @Override
    public Collection<String> doInSharding(Collection<String> availableTargetNames, ShardingValue<Long> shardingValue) {
        Collection<String> result = new LinkedHashSet<>(availableTargetNames.size());
        for (Long value : shardingValue.getValues()) {
            for (String table : availableTargetNames) {
                if (table.endsWith(value % 2 + "")) {
                    result.add(table);
                }
            }
        }
        return result;
    }

    @Override
    public Collection<String> doBetweenSharding(Collection<String> availableTargetNames, ShardingValue<Long> shardingValue) {
        Collection<String> result = new LinkedHashSet<>(availableTargetNames.size());
        Range<Long> range = shardingValue.getValueRange();
        for (Long i = range.lowerEndpoint(); i < range.upperEndpoint(); i++) {
            for (String each : availableTargetNames) {
                if (each.endsWith(i % 2 + "")) {
                    result.add(each);
                }
            }
        }

        return result;
    }
}
