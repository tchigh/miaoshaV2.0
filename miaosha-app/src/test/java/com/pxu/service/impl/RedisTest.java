package com.pxu.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pxu.WebApplication;
import com.pxu.domain.SeckillProduct;
import com.pxu.persistence.SeckillProductsMapper;
import com.pxu.redis.impl.RedisObjectCache;
import com.pxu.redis.impl.RedisStringKeyCache;
import com.pxu.redis.impl.RedisZsetCache;
import com.pxu.service.SeckillProductService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringEscapeUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author pxu31@qq.com
 * @date 2020/7/30 14:43
 */
@Slf4j
@SpringBootTest(classes = WebApplication.class)
public class RedisTest {
    @Autowired
    SeckillProductService productService;
    //
    @Autowired
    RedisStringKeyCache stringCache;

    @Autowired
    RedisZsetCache zsetCache;

    @Autowired
    RedisObjectCache objectCache;

    @Autowired
    SeckillProductsMapper productsMapper;

    @Test
    void testZset(){
        zsetCache.zAdd("zset_test", "1001", 1);
        zsetCache.zAdd("zset_test", "1002", 2);
        zsetCache.zAdd("zset_test", "1003", 3);
        zsetCache.zAdd("zset_test", "1004", 4);
        zsetCache.zAdd("zset_test", "1005", 5);
    }

    @Test
    void testHash(){
//        //注意只能顶层key设置失效时间
//        String key = "hashtest";
//        String hashKey = "hashkey1";
//        stringCache.setHash(key, hashKey, "i am value", 2);
//        System.out.println(JSON.toJSONString(stringCache.getHashMap(key)));
//        System.out.println(stringCache.getHashValue(key, hashKey));
//        stringCache.expired(key, 2);
//        try {
//            Thread.sleep(10000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        System.out.println(stringCache.getHashValue(key, hashKey) == null ? "null" : "not null");

    }

    @Test
    void testList(){
        stringCache.delete("list");
        try {
            Thread.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stringCache.listRightPush("list", "1");
        stringCache.listRightPush("list", "2");
        stringCache.listRightPush("list", "3");
        stringCache.listRightPush("list", "4");
        //1,2,3,4 然后1leftPop
        System.out.println(stringCache.listLeftPop("list"));
        System.out.println(stringCache.listRange("list").toString());
    }

    @Test
    void testObjectCache(){
        stringCache.delete("p1");
        try {
            Thread.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        SeckillProduct product1 = new SeckillProduct();
        product1.setName("xxxppp");
        product1.setNumber(222);
        SeckillProduct product = productsMapper.selectById(1000L);
        objectCache.setObject("p1", product1, 120);
        SeckillProduct p2 = objectCache.getObject("p1", product.getClass());
        log.info(stringCache.get("p1"));
        log.info(p2.toString());
    }
}
