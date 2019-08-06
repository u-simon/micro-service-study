package com.simon.microservice.microservicezuul.cache;

import com.google.common.base.Charsets;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnel;
import com.google.common.hash.PrimitiveSink;

/**
 * @author Simon
 * @Date 2019-08-01 15:55
 * @Describe
 */
public class BloomFilterTest {

	public static void main(String[] args) {
		int total = 1000000;
		BloomFilter<String> bf = BloomFilter.create(new Funnel<String>() {
			@Override
			public void funnel(String s, PrimitiveSink primitiveSink) {
				primitiveSink.putString(s, Charsets.UTF_8);
			}
		}, total, 0.0003);

		for (int i = 0; i < total; i++) {
			bf.put("" + i);
		}

		int count = 0;
		for (int i =0; i < total + 10000; i++){
		    if (bf.mightContain("" + i)){
		        count++;
            }
        }
        System.out.println(count);
	}
}
